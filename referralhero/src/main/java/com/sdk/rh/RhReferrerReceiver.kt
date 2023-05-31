package com.sdk.rh

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class RhReferrerReceiver : BroadcastReceiver() {


    val ACTION_UPDATE_DATA = "ACTION_UPDATE_DATA"
    private val ACTION_INSTALL_REFERRER = "com.android.vending.INSTALL_REFERRER"
    private val KEY_REFERRER = "referrer"


    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) {
            PrefHelper.Debug("Intent is null")
            return
        }
        if (ACTION_INSTALL_REFERRER != intent.action) {
            PrefHelper.Debug(
                "Wrong action! Expected: " + ACTION_INSTALL_REFERRER + " but was: " + intent.action
            )
            return
        }
        val extras = intent.extras
        if (intent.extras == null) {
            PrefHelper.Debug("ReferrerReceiver" + "No data in intent")
            return
        } else {
            PrefHelper(context).appStoreReferrer = extras?.getString(KEY_REFERRER)
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(ACTION_UPDATE_DATA))
        }

    }
}