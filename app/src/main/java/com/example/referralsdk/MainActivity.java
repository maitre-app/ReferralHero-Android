package com.example.referralsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.sdk.rh.RH;

import com.sdk.rh.networking.ApiResponse;

public class MainActivity extends AppCompatActivity implements RH.RHReferralRegisterSubscriberListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RH.getAutoInstance(this);


        // To add a subscriber manually  simply call method below function and
        // send the user information such as email address and name.
        RH.getInstance().
                setEmail("Rajesh@test.com").
                setCustomDomain("https://wongazoma.aistechnolabs.info/action").
                setUserName("Rajesh").setPhoneNumber("+918200108568").
                setReferrerCode("").
                submit(this);


    }

    @Override
    public void onSuccessCallback(ApiResponse response) {
        Log.e("onSuccessCallback", response.getData().getCode());
    }

    @Override
    public void onFailureCallback(ApiResponse response) {
        Log.e("onFailureCallback", response.getStatus());
    }
}