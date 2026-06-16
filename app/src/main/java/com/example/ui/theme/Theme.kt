package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AntiqueBrass,
    onPrimary = DeepNavy,
    primaryContainer = NavyBlue,
    onPrimaryContainer = ClassicCream,
    secondary = RacingGreen,
    onSecondary = ClassicCream,
    tertiary = CrimsonBurgundy,
    onTertiary = ClassicCream,
    background = DeepNavy,
    onBackground = ClassicCream,
    surface = NavyBlue,
    onSurface = ClassicCream,
    surfaceVariant = FieldDarkBg,
    onSurfaceVariant = MutedTextDark,
    outline = AntiqueBrass
)

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = ClassicCream,
    primaryContainer = PaleGoldBg,
    onPrimaryContainer = NavyBlue,
    secondary = RacingGreen,
    onSecondary = ClassicCream,
    tertiary = CrimsonBurgundy,
    onTertiary = ClassicCream,
    background = ClassicCream,
    onBackground = NavyBlue,
    surface = SoftCardWhite,
    onSurface = NavyBlue,
    surfaceVariant = PaleGoldBg,
    onSurfaceVariant = NavyBlue,
    outline = AntiqueBrass

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set dynamicColor to false by default to ensure our stunning brand-aligned
    // Cream, Navy, Brass, Racing Green, & Burgundy colors are strictly visible and never overridden!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
