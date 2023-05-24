package com.example.referralsdk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.rh.RH;
import com.sdk.rh.networking.ApiResponse;
import com.sdk.rh.networking.ReferralParams;


public class MainActivity extends AppCompatActivity implements RH.RHReferralCallBackListener, View.OnClickListener {


    Button btnAdd, btnGet, btnDelete, btnUpdate, btnTrack, btnOrgTrack, btnPending, btnConfirm, btnGetCampaign, btnGetReferral, btnCapture;
    TextView txtReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnGet = findViewById(R.id.btnGet);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnTrack = findViewById(R.id.btnTrack);
        btnOrgTrack = findViewById(R.id.btnOrgTrack);
        btnPending = findViewById(R.id.btnPending);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnGetCampaign = findViewById(R.id.btnGetCampaign);
        btnGetReferral = findViewById(R.id.btnGetReferral);
        btnCapture = findViewById(R.id.btnCapture);
        txtReponse = findViewById(R.id.txtReponse);

        btnAdd.setOnClickListener(this);
        btnGet.setOnClickListener(this);
        btnTrack.setOnClickListener(this);
        btnOrgTrack.setOnClickListener(this);
        btnPending.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnGetCampaign.setOnClickListener(this);
        btnGetReferral.setOnClickListener(this);
        btnCapture.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

    }

    @Override
    public void onSuccessCallback(ApiResponse response) {
        txtReponse.setText("Response : " + response.getStatus());
    }

    @Override
    public void onFailureCallback(ApiResponse response) {
        txtReponse.setText("Response : " + response.getMessage());
    }

    @Override
    public void onClick(View v) {
        ReferralParams referralParams = new ReferralParams();
        switch (v.getId()) {
            case R.id.btnAdd:
                referralParams.setEmail("AndiDev234as@gmail.com");
                referralParams.setDomain("https://wongazoma.aistechnolabs.info/action");
                referralParams.setName("AndiDev");
                referralParams.setReferrer("");
                referralParams.setUuid("MF4345c63888");
                RH.getInstance().formSubmit(this, referralParams);
                RH.getInstance().getPrefHelper_().getRHReferralLink();
                break;

            case R.id.btnGet:
                RH.getInstance().getSubscriberByID(this);
                break;
            case R.id.btnDelete:
                RH.getInstance().deleteSubscriberByID(this);
                break;
            case R.id.btnUpdate:
                RH.getInstance().updateSubscriberByID(this, referralParams);
                break;

        }
    }
}