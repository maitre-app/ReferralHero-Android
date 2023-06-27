package com.sdk.referral.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * Class for RH utility methods
 */
object RHUtil {
    /**
     * Read RH accessToken from Android mainfests meta-data tag
     */
    fun readRhKey(context: Context): String {
        var RHKey: String? = null
        val metaDataKey = "com.sdk.referral.RhKey"
        // manifest overrides string resources
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            if (ai.metaData != null) {
                RHKey = ai.metaData.getString(metaDataKey)
                if (RHKey == null) {
                    RHKey = ai.metaData.getString("com.sdk.referral.token")
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
        val metaDataKey = "com.sdk.referral.uuid"
        // manifest overrides string resources
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            if (ai.metaData != null) {
                RHKey = ai.metaData.getString(metaDataKey)
                if (RHKey == null) {
                    RHKey = ai.metaData.getString("com.sdk.referral.uuid")
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


}