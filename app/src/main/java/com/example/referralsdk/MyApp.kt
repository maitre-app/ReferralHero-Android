package com.example.referralsdk

import android.app.Application
import com.sdk.referral.RH.Companion.getAutoInstance

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        getAutoInstance(this)
    }
}