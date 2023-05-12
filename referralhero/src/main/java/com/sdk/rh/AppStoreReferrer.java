package com.sdk.rh;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

public abstract class AppStoreReferrer {

    /* Link identifier on installing app from play store. */
    private static final String installID_ = "static";

    protected static void processReferrerInfo(Context context, String rawReferrerString, long referrerClickTS, long installClickTS, String store) {

        if (rawReferrerString != null) {
            Log.e("RH Referral", rawReferrerString + "null");
            try {
                rawReferrerString = URLDecoder.decode(rawReferrerString, "UTF-8");
                HashMap<String, String> referrerMap = new HashMap<>();
                String[] referralParams = rawReferrerString.split("&");
                //Always set the raw referrer string:
                for (String referrerParam : referralParams) {
                    if (!TextUtils.isEmpty(referrerParam)) {
                        String splitter = "=";
                        if (!referrerParam.contains("=") && referrerParam.contains("-")) {
                            splitter = "-";
                        }
                        String[] keyValue = referrerParam.split(splitter);
                        if (keyValue.length > 1) { // To make sure that there is one key value pair in referrer
                            try {
                                referrerMap.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                // PrefHelper.Debug("Illegal characters in url encoded string");
            }
        }
    }

    // historically been a public API but not advertised
    public static String getInstallationID() {
        return installID_;
    }
}
