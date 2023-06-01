package com.sdk.referral

import android.content.Context
import android.text.TextUtils
import android.util.Log
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

object AppStoreReferrer {
    // historically been a public API but not advertised
    /* Link identifier on installing app from play store. */
    const val installationID = "static"
    internal fun processReferrerInfo(
        context: Context?,
        rawReferrerString: String?,
        referrerClickTS: Long,
        installClickTS: Long,
        store: String?
    ) {
        var rawReferrerString = rawReferrerString
        if (rawReferrerString != null) {
            Log.e("RH Referral", rawReferrerString + "null")
            try {
                rawReferrerString = URLDecoder.decode(rawReferrerString, "UTF-8")
                val referrerMap = HashMap<String, String>()
                val referralParams =
                    rawReferrerString.split("&".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                //Always set the raw referrer string:
                for (referrerParam in referralParams) {
                    if (!TextUtils.isEmpty(referrerParam)) {
                        var splitter = "="
                        if (!referrerParam.contains("=") && referrerParam.contains("-")) {
                            splitter = "-"
                        }
                        val keyValue =
                            referrerParam.split(splitter.toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        if (keyValue.size > 1) { // To make sure that there is one key value pair in referrer
                            try {
                                referrerMap[URLDecoder.decode(keyValue[0], "UTF-8")] =
                                    URLDecoder.decode(
                                        keyValue[1], "UTF-8"
                                    )
                            } catch (e: UnsupportedEncodingException) {
                                throw RuntimeException(e)
                            }
                        }
                    }
                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                // PrefHelper.Debug("Illegal characters in url encoded string");
            }
        }
    }
}