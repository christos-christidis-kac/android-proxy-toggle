package com.kinandcarta.create.proxytoggle.core.model

import javax.inject.Inject

class ProxyMapper @Inject constructor() {

    companion object {
        private const val ADDRESS_PORT_DELIMITER = ":"
        private const val TIME_DELIMITER = "@"
    }

    fun from(proxy: String?) = proxy?.let {
        val (address, port) = proxy.split(ADDRESS_PORT_DELIMITER)
        if (address.isBlank() || port.isBlank()) {
            Proxy.Disabled
        } else {
            Proxy(address, port)
        }
    } ?: Proxy.Disabled

    fun from(proxies: Set<String>?): List<ProxyWithTime> {
        return proxies?.mapNotNull {
            val (proxyString, timeString) = it.split(TIME_DELIMITER)
            val proxy = from(proxyString)
            val time = timeString.toLong()
            if (proxy != Proxy.Disabled) ProxyWithTime(proxy, time) else null
        }?.sortedByDescending {
            it.timeMs
        } ?: emptyList()
    }
}
