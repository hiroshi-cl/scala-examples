package inverse_macros

import scala.annotation.{Annotation, TypeConstraint, StaticAnnotation}

// internal use
class IMEngineApplied extends Annotation

// internal use
class IMAnnotated extends Annotation

class IMImport extends StaticAnnotation with TypeConstraint