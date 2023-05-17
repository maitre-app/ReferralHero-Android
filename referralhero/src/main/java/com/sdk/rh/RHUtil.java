package com.sdk.rh;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;


/**
 * Class for RH utility methods
 */

public class RHUtil {



    /**
     *Read RH accessToken from Android mainfests meta-data tag
     */

    public static String readRhKey(Context context) {
        String RHKey = null;

        String metaDataKey = "com.rh.sdk.RhKey";
        // manifest overrides string resources
        try {
            final ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                RHKey = ai.metaData.getString(metaDataKey);
                if (RHKey == null) {
                    RHKey = ai.metaData.getString("com.rh.sdk.RhKey");
                }
            }
        } catch (final PackageManager.NameNotFoundException ignore) { }
        if (RHKey != null) return RHKey;

        // check string resources as the last resort
        Resources resources = context.getResources();
        RHKey = resources.getString(resources.getIdentifier(metaDataKey, "string", context.getPackageName()));

        return RHKey;
    }

    /**
     *Read RH Campaign ID  from Android mainfests meta-data tag
     */

    public static String readRhCampaignID(Context context) {
        String RHKey = null;

        String metaDataKey = "com.rh.sdk.uuid";
        // manifest overrides string resources
        try {
            final ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                RHKey = ai.metaData.getString(metaDataKey);
                if (RHKey == null) {
                    RHKey = ai.metaData.getString("com.rh.sdk.uuid");
                }
            }
        } catch (final PackageManager.NameNotFoundException ignore) { }
        if (RHKey != null) return RHKey;

        // check string resources as the last resort
        Resources resources = context.getResources();
        RHKey = resources.getString(resources.getIdentifier(metaDataKey, "string", context.getPackageName()));

        return RHKey;
    }

}
