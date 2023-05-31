package com.sdk.rh

import android.content.Context
import android.content.pm.PackageManager
import java.util.*

/**
 * Class for RH utility methods
 */
object RHUtil {
    /**
     * Read RH accessToken from Android mainfests meta-data tag
     */
    fun readRhKey(context: Context): String {
        var RHKey: String? = null
        val metaDataKey = "com.rh.sdk.ApiKey"
        // manifest overrides string resources
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            if (ai.metaData != null) {
                RHKey = ai.metaData.getString(metaDataKey)
                if (RHKey == null) {
                    RHKey = ai.metaData.getString("com.rh.sdk.ApiKey")
                }
            }
        } catch (ignore: PackageManager.NameNotFoundException) {
        }
        if (RHKey != null) return RHKey

        // check string resources as the last resort
        val resources = context.resources
        RHKey =
            resources.getString(resources.getIdentifier(metaDataKey, "string", context.packageName))
        return RHKey
    }

    /**
     * Read RH Campaign ID  from Android mainfests meta-data tag
     */
    fun readRhCampaignID(context: Context): String {
        var RHKey: String? = null
        val metaDataKey = "com.rh.sdk.uuid"
        // manifest overrides string resources
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            if (ai.metaData != null) {
                RHKey = ai.metaData.getString(metaDataKey)
                if (RHKey == null) {
                    RHKey = ai.metaData.getString("com.rh.sdk.uuid")
                }
            }
        } catch (ignore: PackageManager.NameNotFoundException) {
        }
        if (RHKey != null) return RHKey

        // check string resources as the last resort
        val resources = context.resources
        RHKey =
            resources.getString(resources.getIdentifier(metaDataKey, "string", context.packageName))
        return RHKey
    }

    fun formatString(format: String?, vararg args: Any?): String? {
        return java.lang.String.format(Locale.US, format, args)
    }

}