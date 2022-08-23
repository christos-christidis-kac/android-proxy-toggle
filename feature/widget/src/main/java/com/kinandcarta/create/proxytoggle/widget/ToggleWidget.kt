package com.kinandcarta.create.proxytoggle.widget

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import com.kinandcarta.create.proxytoggle.widget.ToggleWidgetReceiver.Companion.ProxyAddress
import com.kinandcarta.create.proxytoggle.widget.ToggleWidgetReceiver.Companion.ProxyEnabled
import com.kinandcarta.create.proxytoggle.widget.ToggleWidgetReceiver.Companion.ProxyPort
import com.kinandcarta.create.proxytoggle.widget.theme.GlanceStatusDisabledlTextStyle
import com.kinandcarta.create.proxytoggle.widget.theme.GlanceStatusEnabledTextStyle
import com.kinandcarta.create.proxytoggle.widget.theme.GlanceValueLabelTextStyle
import com.kinandcarta.create.proxytoggle.widget.theme.GlanceValueTextStyle

class ToggleWidget : GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val proxyEnabled = currentState(ProxyEnabled) ?: false
        val address = currentState(ProxyAddress) ?: ""
        val port = currentState(ProxyPort) ?: ""

        Box(
            modifier = GlanceModifier
                .background(ImageProvider(R.drawable.widget_background))
                .padding(
                    horizontal = R.dimen.widget_outer_horizontal_padding,
                    vertical = R.dimen.widget_default_margin
                )
        ) {
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                ToggleWidgetIcon(proxyEnabled)
                Column(
                    modifier = GlanceModifier.defaultWeight()
                        .padding(start = R.dimen.widget_icon_margin)
                ) {
                    Text(
                        text = getStatusText(context, proxyEnabled),
                        style = if (proxyEnabled) GlanceStatusEnabledTextStyle else GlanceStatusDisabledlTextStyle
                    )
                    Row(modifier = GlanceModifier.padding(top = R.dimen.widget_default_margin)) {
                        ValueWithLabel(
                            label = R.string.widget_label_ip_address,
                            value = address
                        )
                        ValueWithLabel(
                            label = R.string.widget_label_port,
                            value = port,

                            modifier = GlanceModifier.padding(
                                start = R.dimen.widget_xlarge_margin
                            )
                        )
                    }
                }
                Image(
                    provider = ImageProvider(R.drawable.ic_settings),
                    contentDescription = LocalContext.current.getString(R.string.a11y_settings),
                    modifier = GlanceModifier.clickable(onClick = actionRunCallback<SettingsClickAction>())
                )
            }
        }
    }
}

private fun getStatusText(context: Context, proxyEnabled: Boolean): String {
    return context.getString(
        if (proxyEnabled) R.string.connected else R.string.disconnected
    ).uppercase()
}

@Composable
fun ToggleWidgetIcon(proxyEnabled: Boolean) {
    Image(
        provider = ImageProvider(
            resId = if (proxyEnabled) {
                R.drawable.widget_toggle_enabled
            } else {
                R.drawable.widget_toggle_disabled
            }
        ),
        contentDescription = LocalContext.current.getString(R.string.a11y_toggle_proxy),
        modifier = GlanceModifier
            .size(R.dimen.widget_toggle_icon_size)
            .clickable(onClick = actionRunCallback<ToggleClickAction>())
    )
}

@Composable
fun ValueWithLabel(
    @StringRes label: Int,
    value: String,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(modifier = modifier) {
        Text(
            text = LocalContext.current.getString(label),
            style = GlanceValueLabelTextStyle
        )
        Text(text = value, style = GlanceValueTextStyle)
    }
}
