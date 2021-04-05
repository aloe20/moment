package com.aloe.moment.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun getStatusHeightDp(): Dp = LocalContext.current.resources.run {
  val statusBarId = getIdentifier("status_bar_height", "dimen", "android")
  (if (statusBarId > 0) (getDimensionPixelSize(statusBarId) / displayMetrics.density) else 0F).dp
}
