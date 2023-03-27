/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.aloe.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.util.isMethodCall

class LogDetector : Detector(), Detector.UastScanner {
    private val list = listOf("v", "d", "i", "w", "e", "wtf")
    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UCallExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return LogHandle(context)
    }

    inner class LogHandle(private val context: JavaContext) : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            node.methodName?.takeIf {
                node.isMethodCall() && node.receiver != null && list.contains(
                    it,
                )
            }?.let {
                node.resolve()
            }?.takeIf { context.evaluator.isMemberInClass(it, "android.util.Log") }?.also {
                context.report(ISSUE, node, context.getLocation(node), "禁止直接使用Android Log")
            }
        }
    }

    companion object {
        val ISSUE = Issue.create(
            "LogUsage",
            "Log Usage",
            "请直接使用String/Any的log方法",
            Category.CORRECTNESS,
            6,
            Severity.ERROR,
            Implementation(LogDetector::class.java, Scope.JAVA_FILE_SCOPE),
        )
    }
}
