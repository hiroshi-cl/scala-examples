package byname_macro

import scala.reflect.macros.whitebox.Context

case class ByNameProxy[Dispatch, Transformed](a: Transformed) extends AnyVal

class ByNameBundle(override val c: Context) extends AbstractBundle with internal.Extractors {

  import c.universe._

  def byName = {
    c.enclosingImplicits match {
      case head :: (_ :+ last) =>
        getByNames(last.tree) match {
          case List(byNameArg) =>
            val res = q"${c.prefix.tree} ($byNameArg)"
            val r = try {
              c.typecheck(res)
            } catch {
              case e: Throwable =>
                suitableApplyMethodNotFoundError(e, makeThunk(res))
            }
            smartMakeThunk(r, head.pt)

          case argList =>
            multipleByNameParameterError(last.tree, argList)
        }
      case _ =>
        outOfImplicitsError
    }
  }

  def byNameProxy = {
    c.enclosingImplicits match {
      case list@(head :: (_ :+ last)) =>
        getByNames(last.tree) match {
          case List(byNameArg) =>
            if (head.pt.typeConstructor =:= typeOf[ByNameProxy[_, _]].typeConstructor) {
              val dispatchType = head.pt.typeArgs.head
              val argType = head.pt.typeArgs.tail.head
              val dispatch = c.inferImplicitValue(dispatchType)
              if (dispatch != EmptyTree) {
                val r = smartDispatch(c.prefix.tree, dispatch, byNameArg)
                val sym = typeOf[ByNameProxy.type].termSymbol
                c.typecheck(q"$sym[$dispatchType, $argType](${smartMakeThunk(r, argType)})")
              }
              else suitableImplicitValueNotFoundError(head.tree, dispatchType)
            }
            else invalidUsageOfByNameProxyError(list)

          case argList =>
            multipleByNameParameterError(last.tree, argList)
        }
      case _ =>
        outOfImplicitsError
    }
  }

  def smartDispatch(prefix: Tree, dispatch: Tree, byNameArg: Tree) = {
    val res = q"${c.prefix.tree} ($dispatch, $byNameArg)"
    val r =
      try {
        c.typecheck(res)
      } catch {
        case e: Throwable =>
          val simple = q"$dispatch($byNameArg)"
          try {
            c.typecheck(simple)
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
        for ((arg, sym) <- list.flatten if sym.isByName) yield {
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
    val thunk = c.typecheck(q"() => $tree")
    c.internal.changeOwner(thunk, c.internal.enclosingOwner, thunk.symbol)
    c.internal.setOwner(thunk.symbol, c.internal.enclosingOwner)
    thunk
  }

  def smartMakeThunk(tree: Tree, expectedType: Type): Tree = {
    val typed = c.typecheck(tree)
    if (typed.tpe weak_<:< expectedType)
      typed
    else {
      val thunked = makeThunk(typed)
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

  private[this] def error(tree: Tree)(message: String = "ERROR") = {
    c.abort(tree.pos,
      List(message
        , "** symbol **", Option(tree.symbol).getOrElse(NoSymbol).toString
        , "** code **", show(tree)
        , "** tree **", showRaw(tree)
        , "** stack trace **", Thread.getAllStackTraces.get(Thread.currentThread()).mkString("\n\t")
      ).mkString("\n\t"))
  }
}