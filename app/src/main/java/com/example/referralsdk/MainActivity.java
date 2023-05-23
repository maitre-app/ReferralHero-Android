package com.example.referralsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdk.rh.RH;
import com.sdk.rh.networking.ApiResponse;


public class MainActivity extends AppCompatActivity implements RH.RHReferralCallBackListener, View.OnClickListener {


    Button btnAdd,btnTrack,btnOrgTrack,btnPending,btnConfirm,btnGetCampaign,btnGetReferral,btnCapture;
    TextView txtReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnTrack = findViewById(R.id.btnTrack);
        btnOrgTrack = findViewById(R.id.btnOrgTrack);
        btnPending = findViewById(R.id.btnPending);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnGetCampaign = findViewById(R.id.btnGetCampaign);
        btnGetReferral = findViewById(R.id.btnGetReferral);
        btnCapture = findViewById(R.id.btnCapture);

        btnAdd.setOnClickListener(this);
        btnTrack.setOnClickListener(this);
        btnOrgTrack.setOnClickListener(this);
        btnPending.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnGetCampaign.setOnClickListener(this);
        btnGetReferral.setOnClickListener(this);
        btnCapture.setOnClickListener(this);

    }

    @Override
    public void onSuccessCallback(ApiResponse response) {
        Log.e("onSuccessCallback", response.getData().getCode());
    }

    @Override
    public void onFailureCallback(ApiResponse response) {
        Log.e("onFailureCallback", response.getStatus());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnAdd:
                RH.getInstance().
                        setEmail("Android123@test.com").
                        setCustomDomain("https://wongazoma.aistechnolabs.info/action").
                        setUserName("Rajesh").setPhoneNumber("+918200108568").
                        setReferrerCode("").
                        submit(this);
                break;
        }
    }
}