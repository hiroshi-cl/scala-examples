package inverse_macros

trait UseGlobal {
  val global: scala.tools.nsc.Global
}

trait UseParadisePlugins extends UseGlobal {
  def paradiseAnalyzerPlugin: global.analyzer.AnalyzerPlugin

  def paradiseMacroPlugin: global.analyzer.MacroPlugin
}

trait UseInverseMacrosPlugins extends UseGlobal {
  def inverseMacrosAnalyzerPlugin: global.analyzer.AnalyzerPlugin

  def inverseMacrosMacroPlugin: global.analyzer.MacroPlugin
}

trait AbstractPlugin extends scala.tools.nsc.plugins.Plugin
  with UseGlobal
  with pieces.UseRepairOwnerChain
