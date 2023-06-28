package com.example.referralsdk

import android.app.Application
import com.sdk.referral.RH

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RH.initRHSDK(this, "00f9e163fd6d54b18631fd255a91ce06b959bd5c", "MF4345c63888")
    }
}