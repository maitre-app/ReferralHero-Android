package com.example.referralsdk;

import android.app.Application;

import com.sdk.referral.RH;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RH.getAutoInstance(this);
    }
}
