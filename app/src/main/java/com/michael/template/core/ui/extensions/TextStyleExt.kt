package com.michael.template.core.ui.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.TextUnit
import com.michael.template.R
import com.michael.template.core.ui.util.Size
import com.michael.template.core.ui.util.Style

// Primary text style method
@Composable
fun primaryTextStyle(
    color: Color = Color.White,
    fontSize: TextUnit = Size.textPrimarySizeGlobal,
    fontWeight: FontWeight? = Style.fontWeightPrimaryGlobal,
    fontStyle: FontStyle? = FontStyle.Normal,
    fontFamily: FontFamily? = FontFamily(Font(R.font.dmsansregular)),
    letterSpacing: TextUnit = TextUnit.Unspecified,
    background: Color = Color.Unspecified,
    textDecoration: TextDecoration? = TextDecoration.None,
    textAlign: TextAlign? = TextAlign.Start,
    textDirection: TextDirection? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
): TextStyle {
    return TextStyle(
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        background = background,
        textDecoration = textDecoration,
        textAlign = textAlign,
        textDirection = textDirection,
        lineHeight = lineHeight,
    )
}

// Bold text style method

@Composable
fun boldTextStyle(
    color: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = Size.textBoldSizeGlobal,
    fontWeight: FontWeight? = Style.fontWeightBoldGlobal,
    fontStyle: FontStyle? = FontStyle.Normal,
    fontFamily: FontFamily? = FontFamily(Font(R.font.dmsansbold)),
    letterSpacing: TextUnit = TextUnit.Unspecified,
    background: Color = Color.Unspecified,
    textDecoration: TextDecoration? = TextDecoration.None,
    textAlign: TextAlign? = TextAlign.Start,
    textDirection: TextDirection? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
): TextStyle {
    return TextStyle(
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        background = background,
        textDecoration = textDecoration,
        textAlign = textAlign,
        textDirection = textDirection,
        lineHeight = lineHeight,
    )
}

/* Secondary text style method */
@Composable
fun secondaryTextStyle(
    color: Color = MaterialTheme.colorScheme.onSecondary,
    fontSize: TextUnit = Size.textSecondarySizeGlobal,
    fontWeight: FontWeight? = Style.fontWeightSecondaryGlobal,
    fontStyle: FontStyle? = FontStyle.Normal,
    fontFamily: FontFamily? = FontFamily(Font(R.font.dmsansregular)),
    letterSpacing: TextUnit = TextUnit.Unspecified,
    background: Color = Color.Unspecified,
    textDecoration: TextDecoration? = TextDecoration.None,
    textAlign: TextAlign? = TextAlign.Start,
    textDirection: TextDirection? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
): TextStyle {
    return TextStyle(
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        background = background,
        textDecoration = textDecoration,
        textAlign = textAlign,
        textDirection = textDirection,
        lineHeight = lineHeight,
    )
}
