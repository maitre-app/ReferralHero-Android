package com.sdk.rh;

import android.content.Context;
import android.provider.Settings.Secure;

public class AndroidIdUtil {

    /**
     * <p>this method to return the ANDROID_ID value is unique to each user </p>
     *
     * @param context      pass Context.
     */
    public static String getAndroidId(final Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }
}
