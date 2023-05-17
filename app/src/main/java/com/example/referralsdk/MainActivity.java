package com.example.referralsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.sdk.rh.RH;
import com.sdk.rh.RHError;
import org.json.JSONObject;
import com.example.referralsdk.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RH.getAutoInstance(this);
        RH rh = new RH(this);
        rh.registerSubscriber("", new RH.RHReferralRegisterSubscriberListener() {
            @Override
            public void onFinished(String response, RHError error) {

            }
        });


    }
}