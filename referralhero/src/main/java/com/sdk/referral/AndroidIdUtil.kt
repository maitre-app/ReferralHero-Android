package com.sdk.referral

import android.content.Context
import android.provider.Settings.Secure

object AndroidIdUtil {
    /**
     *
     * this method to return the ANDROID_ID value is unique to each user
     *
     * @param context      pass Context.
     */
    fun getAndroidId(context: Context): String {
        return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
    }
}