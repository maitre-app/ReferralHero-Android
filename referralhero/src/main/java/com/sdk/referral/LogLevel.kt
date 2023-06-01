package com.sdk.referral

import android.util.Log

enum class LogLevel(val androidLogLevel: Int) {
    VERBOSE(Log.VERBOSE), DEBUG(Log.DEBUG), INFO(Log.INFO), WARN(Log.WARN), ERROR(Log.ERROR), ASSERT(
        Log.ASSERT
    ),
    SUPRESS(8);

}