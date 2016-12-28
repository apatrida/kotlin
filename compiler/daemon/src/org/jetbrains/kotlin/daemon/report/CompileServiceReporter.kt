/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.daemon.report

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.daemon.common.AdditionalCompilerArguments
import org.jetbrains.kotlin.daemon.common.CompilerServicesFacadeBase
import org.jetbrains.kotlin.daemon.common.ReportCategory
import org.jetbrains.kotlin.daemon.report.FilteringReporterBase
import java.io.PrintStream

// For messages of about compile daemon
internal interface CompileServiceReporter {
    fun info(message: String) {
        report(CompilerMessageSeverity.INFO, message)
    }

    fun report(severity: CompilerMessageSeverity, message: String)
}

internal class CompileServiceReporterStreamAdapter(private val out: PrintStream) : CompileServiceReporter {
    override fun report(severity: CompilerMessageSeverity, message: String) {
        out.print("[Kotlin daemon][$severity] $message")
    }
}

internal class CompileServiceReporterImpl(
        servicesFacade: CompilerServicesFacadeBase,
        additionalCompilerArguments: AdditionalCompilerArguments
) : FilteringReporterBase(servicesFacade, additionalCompilerArguments, ReportCategory.DAEMON_MESSAGE), CompileServiceReporter {

    override fun report(severity: CompilerMessageSeverity, message: String) {
        report(severity.value, message)
    }
}