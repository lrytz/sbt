/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt
package internal

import java.nio.file.Path
import sbt.internal.inc.MixedAnalyzingCompiler
import xsbti.compile.{ AnalysisStore => XAnalysisStore }
import xsbti.compile.analysis.ReadWriteMappers

private[sbt] object AnalysisUtil {
  // some machines have many cores.
  // we don't want to occupy them all for analysis serialization.
  lazy val parallelism: Int =
    scala.math.min(
      Runtime.getRuntime.availableProcessors(),
      8,
    )
  def staticCachedStore(
      analysisFile: Path,
      useTextAnalysis: Boolean,
      useConsistent: Boolean,
  ): XAnalysisStore =
    MixedAnalyzingCompiler.staticCachedStore(
      analysisFile = analysisFile,
      useTextAnalysis = useTextAnalysis,
      useConsistent = useConsistent,
      mappers = ReadWriteMappers.getEmptyMappers(),
      // reproducisble = true will wipe out the timestamp, which we need for sbt 1.x
      reproducible = false,
      parallelism = parallelism,
    )
}
