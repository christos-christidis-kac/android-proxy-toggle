package com.kinandcarta.create.proxytoggle.widget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.kinandcarta.create.proxytoggle.core.intent.getAppLaunchIntent
import com.kinandcarta.create.proxytoggle.widget.ToggleWidgetReceiver.Companion.ACTION_TOGGLE_PROXY

class SettingsClickAction : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        context.startActivity(getAppLaunchIntent(context))
    }
}

class ToggleClickAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = Intent(context, ToggleWidgetReceiver::class.java).apply {
            action = ACTION_TOGGLE_PROXY
        }
        context.sendBroadcast(intent, ToggleWidgetReceiver.PRIVATE_PERMISSION)
    }
}
