/*-
 * #%L
 * ktlint-maven-plugin
 * %%
 * Copyright (C) 2018 GantSign Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.gantsign.maven.plugin.ktlint.internal

import com.github.gantsign.maven.plugin.ktlint.ReporterConfig
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugin.logging.Log
import java.io.File
import java.nio.charset.Charset

internal class Check(
    log: Log,
    basedir: File,
    sourceRoots: Set<File>,
    charset: Charset,
    includes: Set<String>,
    excludes: Set<String>,
    android: Boolean,
    reporterConfig: Set<ReporterConfig>,
    verbose: Boolean,
    private val failOnViolation: Boolean
) : AbstractCheckSupport(
    log,
    basedir,
    sourceRoots,
    charset,
    includes,
    excludes,
    android,
    addMavenReporter(reporterConfig),
    verbose
) {
    operator fun invoke() {
        val reporter = reporter

        val hasErrors = hasErrors(reporter)
        if (hasErrors && failOnViolation) {
            throw MojoFailureException("Kotlin source failed ktlint check.")
        }
    }

    companion object {

        @JvmStatic
        fun addMavenReporter(reporterConfig: Set<ReporterConfig>): Set<ReporterConfig> {
            return if (reporterConfig.any { it.name == MavenLogReporter.NAME }) {
                reporterConfig
            } else {
                reporterConfig + ReporterConfig(MavenLogReporter.NAME)
            }
        }
    }
}