package com.example.referralsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.sdk.referral.DeviceInfo;
import com.sdk.referral.RH;
import com.sdk.referral.RhReferrerReceiver;
import com.sdk.referral.networking.ApiResponse;
import com.sdk.referral.networking.ListSubscriberData;
import com.sdk.referral.networking.RankingDataContent;
import com.sdk.referral.networking.ReferralParams;
import com.sdk.referral.networking.SubscriberData;


public class MainActivity extends AppCompatActivity implements RH.RHReferralCallBackListener, View.OnClickListener, RH.RHMyReferralCallBackListener, RH.RHLeaderboardReferralCallBackListener {


    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Referre Code", RH.getInstance().getPrefHelper().getAppStoreReferrer());
        }
    };
    TextView txtReponse;
    Button btnAdd, btnGet, btnShareLink, btnDelete, btnUpdate, btnTrack, btnOrgTrack, btnPending, btnConfirm, btnGetCampaign, btnGetReferral, btnCapture;
    DeviceInfo deviceInfo;
    RH rh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceInfo = new DeviceInfo(this);
        rh = RH.getInstance();

        btnAdd = findViewById(R.id.btnAdd);
        btnGet = findViewById(R.id.btnGet);
        btnDelete = findViewById(R.id.btnDelete);
        btnShareLink = findViewById(R.id.btnVisitor);
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
        btnShareLink.setOnClickListener(this);

    }

    @Override
    public void onSuccessCallback(ApiResponse response) {
        txtReponse.setText("Response : " + new Gson().toJson(response));
        Log.e("onSuccessCallback", new Gson().toJson(response));
    }

    @Override
    public void onFailureCallback(ApiResponse response) {
        txtReponse.setText("Response : " + new Gson().toJson(response));
        Log.e("onFailureCallback", new Gson().toJson(response));
    }

    @Override
    public void onClick(View v) {
        ReferralParams referralParams = new ReferralParams();
        switch (v.getId()) {
            case R.id.btnAdd:
                referralParams.setEmail("Jayden2413@gmail.com");
                referralParams.setDomain("https://wongazoma.aistechnolabs.info/action");
                referralParams.setName("AndiDe4v");
                referralParams.setReferrer("");
                referralParams.setUuid("MF4345c63888");
                referralParams.setDevice("Android");
                referralParams.setOsType(deviceInfo.getOperatingSystem());
                rh.formSubmit(new RH.RHReferralCallBackListener() {
                    @Override
                    public void onSuccessCallback(@Nullable ApiResponse<SubscriberData> response) {

                    }

                    @Override
                    public void onFailureCallback(@Nullable ApiResponse<SubscriberData> response) {

                    }
                }, referralParams);
                rh.getPrefHelper().getRHReferralLink();
                break;

            case R.id.btnGet:
                rh.getSubscriber(this);
                break;
            case R.id.btnDelete:
                rh.deleteSubscriber(this);
                break;
            case R.id.btnUpdate:
                referralParams.setName("AndiDevOps");
                rh.updateSubscriber(this, referralParams);
                break;
            case R.id.btnTrack:
                referralParams.setEmail("Jayden@gmail.com");
                referralParams.setName("AndiDev");
                rh.trackReferral(this, referralParams);
                break;
            case R.id.btnCapture:
                referralParams.setSocial("Whatsapp");
                rh.captureShare(this, referralParams);
                break;

            case R.id.btnGetReferral:
                rh.getMyReferrals(this);
                break;
            case R.id.btnGetCampaign:
                rh.getLeaderboard(new RH.RHLeaderboardReferralCallBackListener() {
                    @Override
                    public void onLeaderboardReferralSuccessCallback(@Nullable ApiResponse<RankingDataContent> response) {

                    }

                    @Override
                    public void onLeaderboardReferralFailureCallback(@Nullable ApiResponse<RankingDataContent> response) {

                    }
                });
                break;
            case R.id.btnPending:
                referralParams.setEmail("Jayden@gmail.com");
                referralParams.setName("AndiDev");
                rh.pendingReferral(this, referralParams);
                break;
            case R.id.btnOrgTrack:
                referralParams.setEmail("Jayden@gmail.com");
                referralParams.setName("AndiDev");
                rh.organicTrackReferral(this, referralParams);
                break;
            case R.id.btnConfirm:
                rh.confirmReferral(this);
                break;
            case R.id.btnVisitor:

                break;

        }
    }

    @Override
    public void onMyReferralSuccessCallback(@Nullable ApiResponse<ListSubscriberData> response) {
        Log.e("onMyReferralSuccess", new Gson().toJson(response));
    }

    @Override
    public void onMyReferralFailureCallback(@Nullable ApiResponse<ListSubscriberData> response) {
        Log.e("onMyReferralSuccess", new Gson().toJson(response));
    }

    @Override
    public void onLeaderboardReferralSuccessCallback(@Nullable ApiResponse<RankingDataContent> response) {
        Log.e("onLeaderBoardSuccess", new Gson().toJson(response));
    }

    @Override
    public void onLeaderboardReferralFailureCallback(@Nullable ApiResponse<RankingDataContent> response) {
        Log.e("onLeaderBoardSuccess", new Gson().toJson(response));
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateReceiver, new IntentFilter(new RhReferrerReceiver().getACTION_UPDATE_DATA()));
        super.onResume();
    }
}