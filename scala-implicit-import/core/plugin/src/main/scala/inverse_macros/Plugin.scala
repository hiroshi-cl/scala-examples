package inverse_macros

class Plugin(override val global: scala.tools.nsc.Global) extends AbstractPlugin
  with mixin.InverseMacrosPlugins
  with pieces.mixin.ImplicitImport
  with pieces.mixin.SavedTreeExtractor {

  val name = "invmacs"
  val description = "Inverse macros, macro paradise, etc."
  val components = Nil

  override def init(options: List[String], error: (String) => Unit): Boolean = {
    global.analyzer.addAnalyzerPlugin(inverseMacrosAnalyzerPlugin)
    global.analyzer.addMacroPlugin(inverseMacrosMacroPlugin)
    true
  }
}
