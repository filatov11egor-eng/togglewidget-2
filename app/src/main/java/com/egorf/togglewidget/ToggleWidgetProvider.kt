package com.egorf.togglewidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.RemoteViews

class ToggleWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            ensureDefaultState(context, appWidgetId)
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_TOGGLE) {
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                toggleState(context, appWidgetId)
                val mgr = AppWidgetManager.getInstance(context)
                updateAppWidget(context, mgr, appWidgetId)
            } else {
                val mgr = AppWidgetManager.getInstance(context)
                val component = ComponentName(context, ToggleWidgetProvider::class.java)
                val ids = mgr.getAppWidgetIds(component)
                for (id in ids) {
                    toggleState(context, id)
                    updateAppWidget(context, mgr, id)
                }
            }
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_toggle)
        val prefs = prefs(context)
        val showText = prefs.getBoolean(stateKey(appWidgetId), false)

        views.setViewVisibility(R.id.btnToggle, if (showText) View.GONE else View.VISIBLE)
        views.setViewVisibility(R.id.txtHello, if (showText) View.VISIBLE else View.GONE)

        val toggleIntent = Intent(context, ToggleWidgetProvider::class.java).apply {
            action = ACTION_TOGGLE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pi = PendingIntent.getBroadcast(context, appWidgetId, toggleIntent, flags)

        views.setOnClickPendingIntent(R.id.btnToggle, pi)
        views.setOnClickPendingIntent(R.id.txtHello, pi)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun ensureDefaultState(context: Context, appWidgetId: Int) {
        val p = prefs(context)
        if (!p.contains(stateKey(appWidgetId))) {
            p.edit().putBoolean(stateKey(appWidgetId), false).apply()
        }
    }

    private fun toggleState(context: Context, appWidgetId: Int) {
        val p = prefs(context)
        val key = stateKey(appWidgetId)
        val current = p.getBoolean(key, false)
        p.edit().putBoolean(key, !current).apply()
    }

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun stateKey(appWidgetId: Int) = "state_" + appWidgetId

    companion object {
        const val ACTION_TOGGLE = "com.egorf.togglewidget.ACTION_TOGGLE"
        private const val PREFS_NAME = "toggle_widget_prefs"
    }
}
