package com.example.referralsdk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sdk.rh.RH;
import com.sdk.rh.networking.ApiResponse;
import com.sdk.rh.networking.ListSubscriberData;
import com.sdk.rh.networking.RankingDataContent;
import com.sdk.rh.networking.ReferralParams;


public class MainActivity extends AppCompatActivity implements RH.RHReferralCallBackListener, View.OnClickListener, RH.RHMyReferralCallBackListener, RH.RHLeaderBoardReferralCallBackListener {


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
                referralParams.setEmail("Jayden@gmail.com");
                referralParams.setDomain("https://wongazoma.aistechnolabs.info/action");
                referralParams.setName("AndiDev");
                referralParams.setReferrer("");
                referralParams.setUuid("MF4345c63888");
                RH.getInstance().formSubmit(this, referralParams);
                RH.getInstance().getPrefHelper().getRHReferralLink();
                break;

            case R.id.btnGet:
                RH.getInstance().getSubscriberByID(this);
                break;
            case R.id.btnDelete:
                RH.getInstance().deleteSubscriberByID(this);
                break;
            case R.id.btnUpdate:
                referralParams.setName("AndiDevOps");
                RH.getInstance().updateSubscriberByID(this, referralParams);
                break;
            case R.id.btnTrack:
                referralParams.setEmail("Jayden@gmail.com");
                referralParams.setName("AndiDev");
                RH.getInstance().trackReferral(this, referralParams);
                break;
            case R.id.btnCapture:
                referralParams.setSocial("Whatsapp");
                RH.getInstance().captureShare(this, referralParams);
                break;

            case R.id.btnGetReferral:
                RH.getInstance().getMyReferrals(this);
                break;
            case R.id.btnGetCampaign:
                RH.getInstance().getLeaderboard(this);
                break;

        }
    }

    @Override
    public void onMyReferralSuccessCallback(@Nullable ApiResponse<ListSubscriberData> response) {
        Log.e("onMyReferralSuccess", response.getData().getResponse());
    }

    @Override
    public void onMyReferralFailureCallback(@Nullable ApiResponse<ListSubscriberData> response) {
        Log.e("onMyReferralSuccess", response.getMessage());
    }

    @Override
    public void onLeaderBoardReferralSuccessCallback(@Nullable ApiResponse<RankingDataContent> response) {
        Log.e("onLeaderBoardSuccess", String.valueOf(response.getData().getCount()));
    }

    @Override
    public void onLeaderBoardReferralFailureCallback(@Nullable ApiResponse<RankingDataContent> response) {
        Log.e("onLeaderBoardSuccess", response.getMessage());
    }
}