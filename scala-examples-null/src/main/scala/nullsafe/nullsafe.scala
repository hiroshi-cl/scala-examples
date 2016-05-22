package nullsafe

import inverse_macros.{IMAnnotation, IMTransformer}

class nullsafe extends IMAnnotation

object nullsafe extends IMTransformer {

  import scala.reflect.macros.blackbox

  override def transform(c: blackbox.Context)(targs: List[c.Type], argss: List[List[c.Tree]])
                        (api: c.internal.TypingTransformApi)
                        (head: c.Tree, cont: List[c.Tree]): (List[c.Tree], List[c.Tree]) = {
    import c.universe._

    head match {
      case ValDef(mods, name, tpt, rhs) =>
        val sym = head.symbol
        val newCont = cont.collect {
          case v@ValDef(cmods, cname, ctpt, crhs) if crhs.exists(_.symbol == head.symbol) =>
            treeCopy.ValDef(v, cmods, cname, ctpt,
              c.typecheck(q"if(${head.symbol} != null) $crhs else null"))
          case t if t.exists(_.symbol == head.symbol) =>
            c.typecheck(q"if(${head.symbol} != null) $t else null")
          case t =>
            t
        }
        List(head) -> newCont

      case _ =>
        c.warning(head.pos, show(head) + " is not used. (not binded) Is it expected?")
        List(head) -> cont
    }
  }

}
