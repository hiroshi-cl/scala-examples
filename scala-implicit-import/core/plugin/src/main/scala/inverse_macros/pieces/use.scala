package inverse_macros
package pieces

trait UseImplicitImport extends UseGlobal {
  def implicitImport: global.analyzer.AnalyzerPlugin with global.analyzer.MacroPlugin
}

trait UseSavedTreeExtractor extends UseGlobal {

  trait SavedTreeExtractor {
    def extract(tree: global.Tree): global.Tree
  }

  def savedTreeExtractor: SavedTreeExtractor
}
