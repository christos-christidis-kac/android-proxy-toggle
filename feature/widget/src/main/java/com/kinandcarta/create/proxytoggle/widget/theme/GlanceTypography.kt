package com.kinandcarta.create.proxytoggle.widget.theme

import androidx.compose.ui.unit.sp
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.kinandcarta.create.proxytoggle.widget.R

// No way to set custom font yet. No letterSpacing either :(
val GlanceStatusEnabledTextStyle = TextStyle(
    color = ColorProvider(R.color.periwinkle),
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp
)

val GlanceStatusDisabledlTextStyle = TextStyle(
    color = ColorProvider(R.color.pale_grey),
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp
)

val GlanceValueLabelTextStyle = TextStyle(
    color = ColorProvider(R.color.white),
    fontSize = 9.sp
)

val GlanceValueTextStyle = TextStyle(
    color = ColorProvider(R.color.white),
    fontSize = 14.sp
)
