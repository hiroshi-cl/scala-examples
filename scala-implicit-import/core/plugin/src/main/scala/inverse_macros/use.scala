package inverse_macros

trait UseGlobal {
  val global: scala.tools.nsc.Global
}

trait UseInverseMacrosPlugins extends UseGlobal {
  def inverseMacrosAnalyzerPlugin: global.analyzer.AnalyzerPlugin

  def inverseMacrosMacroPlugin: global.analyzer.MacroPlugin
}

trait AbstractPlugin extends scala.tools.nsc.plugins.Plugin
  with UseGlobal
  with UseInverseMacrosPlugins
  with pieces.UseImplicitImport
  with pieces.UseSavedTreeExtractor

