package com.kinandcarta.create.proxytoggle.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.kinandcarta.create.proxytoggle.core.android.DeviceSettingsManager
import com.kinandcarta.create.proxytoggle.core.intent.getAppLaunchIntent
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ToggleWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = ToggleWidget()

    @Inject
    lateinit var deviceSettingsManager: DeviceSettingsManager

    @Inject
    lateinit var appSettings: AppSettings

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> updateWidgets(context)
            ACTION_TOGGLE_PROXY -> toggleProxy(context)
        }
    }

    private fun toggleProxy(context: Context) {
        val proxy = deviceSettingsManager.proxySetting.value
        if (proxy.isEnabled) {
            deviceSettingsManager.disableProxy()
        } else {
            val lastUsedProxy = appSettings.lastUsedProxy
            if (lastUsedProxy.isEnabled) {
                deviceSettingsManager.enableProxy(lastUsedProxy)
            } else {
                // There is no last used Proxy, prompt the user to create one
                startMainActivity(context)
            }
        }
    }

    private fun updateWidgets(context: Context) {
        MainScope().launch {
            GlanceAppWidgetManager(context).getGlanceIds(ToggleWidget::class.java)
                .forEach { glanceId ->
                    updateAppWidgetState(context, glanceId) { prefs ->
                        updatePrefs(context, prefs)
                    }
                }
            glanceAppWidget.updateAll(context)
        }
    }

    private fun updatePrefs(context: Context, prefs: MutablePreferences) {
        val proxy = deviceSettingsManager.proxySetting.value
        prefs[ProxyEnabled] = proxy.isEnabled
        if (proxy.isEnabled) {
            prefs[ProxyAddress] = proxy.address
            prefs[ProxyPort] = proxy.port
        } else {
            val lastUsedProxy = appSettings.lastUsedProxy
            if (lastUsedProxy.isEnabled) {
                prefs[ProxyAddress] = lastUsedProxy.address
                prefs[ProxyPort] = lastUsedProxy.port
            } else {
                prefs[ProxyAddress] = context.getString(R.string.widget_not_set)
                prefs[ProxyPort] = context.getString(R.string.widget_not_set)
            }
        }
    }

    private fun startMainActivity(context: Context) {
        context.startActivity(getAppLaunchIntent(context))
    }

    companion object {
        const val PRIVATE_PERMISSION = "com.kinandcarta.create.proxytoggle.PRIVATE_PERMISSION"
        const val ACTION_TOGGLE_PROXY = "ACTION_TOGGLE_PROXY"

        val ProxyEnabled = booleanPreferencesKey("ProxyEnabled")
        val ProxyAddress = stringPreferencesKey("ProxyAddress")
        val ProxyPort = stringPreferencesKey("ProxyPort")
    }
}
