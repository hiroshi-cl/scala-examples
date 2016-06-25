package inverse_macros.mixin

import scala.tools.nsc.Mode

trait InverseMacrosPlugins extends inverse_macros.UseGlobal
  with inverse_macros.pieces.UseRepairOwnerChain {

  import global._
  import analyzer._

  val inverseMacrosAnalyzerPlugin = new analyzer.AnalyzerPlugin {}

  val inverseMacrosMacroPlugin = new analyzer.MacroPlugin {
    override def pluginsMacroExpand(typer: Typer, expandee: Tree, mode: Mode, pt: Type): Option[Tree] = {
      val expanded = standardMacroExpand(typer, expandee, mode, pt)
      repairOwnerChain.repairOwnerChainTraverse(typer, expanded)
      Some(expanded)
    }
  }
}
