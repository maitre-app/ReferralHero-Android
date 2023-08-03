package com.example.referralsdk

import android.app.Application
import com.sdk.referral.RH.Companion.initRHSDK

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initRHSDK(this, "d7a827ce7d2b6fbcdbfb35d769272dabfd97f753", "MFdc38730f91")
    }
}