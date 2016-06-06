package byname_macro

import scala.reflect.macros.whitebox.Context

class ByNameBundle(override val c: Context) extends EnhancedMacroBundleBase(c) {

  import c.universe._

  def byName = {
    val list = c.enclosingImplicits
    if (!list.isEmpty)
      getByNames(list.last.tree) match {
        case List(byNameArg) =>
          val res = q"${c.prefix.tree} ($byNameArg)"
          val r = try {
            res *^ refresh
          } catch {
            case e: Throwable => suitableApplyMethodNotFoundError(e, makeThunk(res))
          }
          smartMakeThunk(r, list.head.pt) *^ refresh
        case argList => multipleByNameParameterError(list.last.tree, argList)
      }
    else outOfImplicitsError
  }

  def byNameProxy = {
    val list = c.enclosingImplicits
    if (!list.isEmpty)
      getByNames(list.last.tree) match {
        case List(byNameArg) =>
          if (list.head.pt.typeConstructor =:= typeOf[ByNameProxy[_, _]].typeConstructor) {
            val dispatchType = list.head.pt.typeArgs(0)
            val argType = list.head.pt.typeArgs(1)
            val dispatch = c.inferImplicitValue(dispatchType)
            if (dispatch != EmptyTree) {
              val r = smartDispatch(c.prefix.tree, dispatch, byNameArg)
              val sym = typeOf[ByNameProxy.type].termSymbol
              q"$sym[$dispatchType, $argType](${smartMakeThunk(r, argType)})" *^ refresh
            }
            else suitableImplicitValueNotFoundError(list.head.tree, dispatchType)
          }
          else invalidUsageOfByNameProxyError(list)

        case argList => multipleByNameParameterError(list.last.tree, argList)
      }
    else outOfImplicitsError
  }

  def smartDispatch(prefix: Tree, dispatch: Tree, byNameArg: Tree) = {
    val res = q"${c.prefix.tree} ($dispatch, $byNameArg)"
    val r = try {
      res *^ refresh
    } catch {
      case e: Throwable =>
        val simple = q"$dispatch($byNameArg)"
        try {
          simple *^ refresh
        } catch {
          case e: Throwable =>
            suitableApplyMethodNotFoundError(e, makeThunk(res))
        }
    }
    r
  }

  def getByNames(tree: Tree): List[Tree] = {
    tree match {
      case FineGrainedApply(_, _, _, list) =>
        for ((arg, isByName) <- list.flatten if isByName) yield {
          for (i@Ident(name) <- arg if i.symbol.asTerm.isByNameParam)
            error(tree)(
              "bad news: ordinal macros cannot collaborate with the by-name variable "
                + name + " directly"
            )

          arg
        }

      case _ =>
        error(tree)("given tree must be instance of Apply")
    }
  }

  def makeThunk(tree: Tree): Tree = {
    val paramName = TermName(c.freshName())
    q"($paramName : Unit) => $tree"
  }

  def smartMakeThunk(tree: Tree, expectedType: Type): Tree = {
    val typed = tree *^ refresh
    if (typed.tpe weak_<:< expectedType)
      typed
    else {
      val thunked = makeThunk(typed) *^ refresh
      if (thunked.tpe weak_<:< expectedType)
        thunked
      else
        error(tree)("the result of apply is not compatible with the expected type")
    }
  }

  private[this] def outOfImplicitsError = error(EmptyTree)("this macro cannot use out of implicit parameters")

  private[this] def multipleByNameParameterError(tree: Tree, list: List[Tree]) =
    error(q"{$tree; {..$list}}")("by name parameter must be just one")

  private[this] def suitableApplyMethodNotFoundError(e: Throwable, tree: Tree) =
    error(tree)("suitable apply method not found or something wrong:\n\t" +
      e.getMessage + e.getStackTrace.mkString("\n\t"))

  private[this] def suitableImplicitValueNotFoundError(tree: Tree, tpe: Type) =
    error(tree)("suitable implicit value not found:\t" + tpe)

  private[this] def invalidUsageOfByNameProxyError(list: List[c.ImplicitCandidate]) = {
    val iformat =
      list.map(i =>
        "pre = %s, pt = %s, sym = %s, sym.info = %s,\n\t\tcode = %s".format(i.pre, i.pt, i.sym, i.sym.info, i.tree)
      ).mkString("\t", "\n\t", "")

    error(q"{..${list.map(_.tree)}}")(
      """return type in by name proxy must be used like:
        |  implicit def proxy(implicit d: Dispatch, r: Transformed): ByNameProxy[Dispatch, Transformed]
        |for implicit macro r
        |%s""".stripMargin.format(iformat))
  }
}