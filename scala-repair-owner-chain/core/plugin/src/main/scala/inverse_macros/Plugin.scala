package inverse_macros

class Plugin(override val global: scala.tools.nsc.Global) extends AbstractPlugin
  with mixin.InverseMacrosPlugins
  with pieces.mixin.RepairOwnerChain {

  val name = "repair"
  val description = "Repair owner chain."
  val components = Nil

  override def init(options: List[String], error: (String) => Unit): Boolean = {
    global.analyzer.addAnalyzerPlugin(inverseMacrosAnalyzerPlugin)
    global.analyzer.addMacroPlugin(inverseMacrosMacroPlugin)
    true
  }
}
