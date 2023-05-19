package com.sdk.rh

import android.content.Context

/**
 *
 *
 * Class is  responsible for capturing device info and updating
 * device info to ReferralHero requests
 *
 */
class DeviceInfo internal constructor(private val context_: Context) {
    companion object {
        /**
         * Get the singleton instance for this class
         *
         * @return [DeviceInfo] instance if already initialised or null
         */
        val instance: DeviceInfo?
            get() {
                val b: RH = RH.instance ?: return null
                return b.deviceInfo
            }
    }
}