package com.kinandcarta.create.proxytoggle.android

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.model.ProxyMapper
import com.kinandcarta.create.proxytoggle.core.model.ProxyWithTime
import com.kinandcarta.create.proxytoggle.core.settings.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefsAppSettings @Inject constructor(
    @ApplicationContext private val context: Context,
    private val proxyMapper: ProxyMapper
) : AppSettings {

    companion object {
        private const val SHARED_PREF_NAME = "AppSettings"
        private const val PREF_PROXY = "proxy"
        private const val PREF_PAST_PROXIES = "past_proxies"
        private const val PREF_THEME = "theme"
        private const val MAX_SAVED_PROXIES = 5
    }

    private val prefs by lazy {
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    override var lastUsedProxy: Proxy
        get() = proxyMapper.from(prefs.getString(PREF_PROXY, null))
        set(value) = prefs.edit { putString(PREF_PROXY, value.toString()) }

    override val pastProxies: List<ProxyWithTime>
        get() = proxyMapper.from(prefs.getStringSet(PREF_PAST_PROXIES, null))

    override fun saveProxy(proxy: Proxy, timeMillis: Long) {
        val pastProxiesToKeep = pastProxies.filter {
            it.proxy != proxy
        }.map {
            "${it.proxy}@${it.timeMs}"
        }.take(MAX_SAVED_PROXIES - 1).toSet()
        val newProxyString = "$proxy@$timeMillis"
        prefs.edit { putStringSet(PREF_PAST_PROXIES, pastProxiesToKeep.plus(newProxyString)) }
    }

    override var themeMode: Int
        get() = prefs.getInt(PREF_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) = prefs.edit { putInt(PREF_THEME, value) }
}
