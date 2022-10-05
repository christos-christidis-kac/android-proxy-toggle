package com.kinandcarta.create.proxytoggle.core.settings

import com.kinandcarta.create.proxytoggle.core.model.Proxy
import com.kinandcarta.create.proxytoggle.core.model.ProxyWithTime

interface AppSettings {

    var lastUsedProxy: Proxy

    var themeMode: Int

    val pastProxies: List<ProxyWithTime>

    fun saveProxy(proxy: Proxy, timeMillis: Long)
}
