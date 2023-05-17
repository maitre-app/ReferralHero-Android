package com.sdk.rh;


import android.content.Context;

/**
 * <p>
 * Class is  responsible for capturing device info and updating
 * device info to ReferralHero requests
 * </p>
 */
public class DeviceInfo {

    private final Context context_;


    /**
     * Get the singleton instance for this class
     *
     * @return {@link DeviceInfo} instance if already initialised or null
     */
    static DeviceInfo getInstance() {
        RH b = RH.getInstance();
        if (b == null) return null;
        return b.getDeviceInfo();
    }

    DeviceInfo(Context context_) {
        this.context_ = context_;
    }
}
