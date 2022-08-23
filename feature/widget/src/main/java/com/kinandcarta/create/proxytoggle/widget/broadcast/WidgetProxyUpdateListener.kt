package com.kinandcarta.create.proxytoggle.widget.broadcast

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import com.kinandcarta.create.proxytoggle.core.broadcast.ProxyUpdateListener
import com.kinandcarta.create.proxytoggle.widget.ToggleWidgetReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WidgetProxyUpdateListener @Inject constructor(
    @ApplicationContext private val context: Context
) : ProxyUpdateListener {

    override fun onProxyUpdate() {
        val intent = Intent(context, ToggleWidgetReceiver::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        context.sendBroadcast(intent, ToggleWidgetReceiver.PRIVATE_PERMISSION)
    }
}
