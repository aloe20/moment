package com.aloe.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
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
      node.methodName?.takeIf { node.isMethodCall() && node.receiver != null && list.contains(it) }?.let {
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
      Implementation(LogDetector::class.java, Scope.JAVA_FILE_SCOPE)
    )
  }
}