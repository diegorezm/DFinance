package com.diegorezm.dfinance.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dfinance.composeapp.generated.resources.*
import org.jetbrains.compose.resources.Font

@Composable
fun openSansFontFamily() = FontFamily(
    Font(Res.font.OpenSans_Regular, FontWeight.Normal, FontStyle.Normal),
    Font(Res.font.OpenSans_Italic, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.OpenSans_Light, FontWeight.Light, FontStyle.Normal),
    Font(Res.font.OpenSans_LightItalic, FontWeight.Light, FontStyle.Italic),
    Font(Res.font.OpenSans_Medium, FontWeight.Medium, FontStyle.Normal),
    Font(Res.font.OpenSans_MediumItalic, FontWeight.Medium, FontStyle.Italic),
    Font(Res.font.OpenSans_SemiBold, FontWeight.SemiBold, FontStyle.Normal),
    Font(Res.font.OpenSans_SemiBoldItalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(Res.font.OpenSans_Bold, FontWeight.Bold, FontStyle.Normal),
    Font(Res.font.OpenSans_BoldItalic, FontWeight.Bold, FontStyle.Italic),
    Font(Res.font.OpenSans_ExtraBold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(Res.font.OpenSans_ExtraBoldItalic, FontWeight.ExtraBold, FontStyle.Italic),
)
