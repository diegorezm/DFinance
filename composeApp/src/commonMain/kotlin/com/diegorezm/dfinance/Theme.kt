package com.diegorezm.dfinance

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Your Website Palette
val Background = Color(0xFFFEF0E4)
val TextPrimary = Color(0xFF1A0C04)
val Accent = Color(0xFFE8844A)
val Muted = Color(0xFF7A4A28)
val Border = Color(0xFF1A0C04)
val SurfaceText = Color(0xFFFEF0E4)

data class Spacing(
    val default: Dp = 0.dp,
    val sm: Dp = 4.dp,
    val md: Dp = 8.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 64.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

/**
 * Accessor for the spacing values.
 */
val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

// Very little border radius to mimic your website
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(8.dp),
    extraLarge = RoundedCornerShape(8.dp)
)

private val AppColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = SurfaceText,
    secondary = Muted,
    onSecondary = SurfaceText,
    background = Background,
    onBackground = TextPrimary,
    surface = Background,
    onSurface = TextPrimary,
    outline = Border,
    primaryContainer = Color(0xFF1A0C04), 
    onPrimaryContainer = SurfaceText,
)

@Composable
fun DFinanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = AppColorScheme

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = AppShapes,
            content = {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    content = content
                )
            }
        )
    }
}
