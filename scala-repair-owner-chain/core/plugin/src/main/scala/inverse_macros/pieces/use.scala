package inverse_macros
package pieces

trait UseRepairOwnerChain extends UseGlobal {

  trait RepairOwnerChainTraverser {
    def repairOwnerChainTraverse(typer: global.analyzer.Typer, tree: global.Tree): Unit
  }

  def repairOwnerChain: global.analyzer.MacroPlugin with RepairOwnerChainTraverser
}
