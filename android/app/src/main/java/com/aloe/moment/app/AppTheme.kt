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

package com.aloe.moment.app

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.aloe.moment.R

fun colorScheme(context: Context): ColorScheme = ColorScheme(
    primary = Color(ContextCompat.getColor(context, R.color.primary)),
    onPrimary = Color(ContextCompat.getColor(context, R.color.onPrimary)),
    primaryContainer = Color(ContextCompat.getColor(context, R.color.primaryContainer)),
    onPrimaryContainer = Color(ContextCompat.getColor(context, R.color.primary)),
    inversePrimary = Color(ContextCompat.getColor(context, R.color.primary)),
    secondary = Color(ContextCompat.getColor(context, R.color.secondary)),
    onSecondary = Color(ContextCompat.getColor(context, R.color.primary)),
    secondaryContainer = Color(ContextCompat.getColor(context, R.color.primary)),
    onSecondaryContainer = Color(ContextCompat.getColor(context, R.color.primary)),
    tertiary = Color(ContextCompat.getColor(context, R.color.tertiary)),
    onTertiary = Color(ContextCompat.getColor(context, R.color.primary)),
    tertiaryContainer = Color(ContextCompat.getColor(context, R.color.primary)),
    onTertiaryContainer = Color(ContextCompat.getColor(context, R.color.primary)),
    background = Color(ContextCompat.getColor(context, R.color.primary)),
    onBackground = Color(ContextCompat.getColor(context, R.color.primary)),
    surface = Color(ContextCompat.getColor(context, R.color.primary)),
    onSurface = Color(ContextCompat.getColor(context, R.color.primary)),
    surfaceVariant = Color(ContextCompat.getColor(context, R.color.primary)),
    onSurfaceVariant = Color(ContextCompat.getColor(context, R.color.primary)),
    surfaceTint = Color(ContextCompat.getColor(context, R.color.primary)),
    inverseSurface = Color(ContextCompat.getColor(context, R.color.primary)),
    inverseOnSurface = Color(ContextCompat.getColor(context, R.color.primary)),
    error = Color(ContextCompat.getColor(context, R.color.primary)),
    onError = Color(ContextCompat.getColor(context, R.color.primary)),
    errorContainer = Color(ContextCompat.getColor(context, R.color.primary)),
    onErrorContainer = Color(ContextCompat.getColor(context, R.color.primary)),
    outline = Color(ContextCompat.getColor(context, R.color.primary)),
    outlineVariant = Color(ContextCompat.getColor(context, R.color.primary)),
    scrim = Color(ContextCompat.getColor(context, R.color.primary)),
)

@Composable
fun MomentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // darkTheme -> DarkColorScheme
        else -> colorScheme(view.context)
    }
    if (!view.isInEditMode) {
        SideEffect {
            with((view.context as Activity).window) {
                statusBarColor = Color.Transparent.toArgb()
                WindowCompat.setDecorFitsSystemWindows(this, false)
                WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            bodyLarge = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        ),
        content = content,
    )
}
