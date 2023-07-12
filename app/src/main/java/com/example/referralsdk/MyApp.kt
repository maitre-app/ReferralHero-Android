package com.example.referralsdk

import android.app.Application
import com.sdk.referral.RH

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RH.initRHSDK(this, "aff8792c7bccffd9165639e8be281bc4c51e3a11", "MF67c1950f09")
    }
}