package inverse_macros.mixin

import scala.tools.nsc.Mode

trait InverseMacrosPlugins extends inverse_macros.UseGlobal
  with inverse_macros.pieces.UseImplicitImport
  with inverse_macros.pieces.UseSavedTreeExtractor {

  import global._
  import analyzer._

  val inverseMacrosAnalyzerPlugin = new analyzer.AnalyzerPlugin {
    override def pluginsPt(pt: Type, typer: Typer, tree: Tree, mode: Mode): Type =
      implicitImport.pluginsPt(pt, typer, tree, mode)
  }

  val inverseMacrosMacroPlugin = new analyzer.MacroPlugin {
    override def pluginsEnterStats(typer: Typer, stats: List[Tree]): List[Tree] =
      stats.flatMap { t =>
        t match {
          // "!= null" is dummy. this comparing invokes pluginTypeSig
          case ddef: DefDef if ddef.symbol.info != null && ddef.symbol.hasAttachment[List[Tree]] =>
            ddef :: ddef.symbol.attachments.get[List[Tree]].get
          case _ =>
            List(t)
        }
      }

    override def pluginsMacroExpand(typer: Typer, expandee: Tree, mode: Mode, pt: Type): Option[Tree] =
      Some(standardMacroExpand(typer, savedTreeExtractor.extract(expandee), mode, pt))
  }
}