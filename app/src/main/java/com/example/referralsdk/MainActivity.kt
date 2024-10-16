package com.example.referralsdk

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.sdk.referral.RH
import com.sdk.referral.RH.Companion.instance
import com.sdk.referral.RH.RHReferralCallBackListener
import com.sdk.referral.model.ReferralParams
import com.sdk.referral.utils.DeviceInfo


class MainActivity : AppCompatActivity(), RHReferralCallBackListener, View.OnClickListener,
    RH.RHMyReferralCallBackListener, RH.RHLeaderBoardReferralCallBackListener,
    RH.RHRewardCallBackListener {

    lateinit var btnGet: Button
    lateinit var btnAdd: Button
    lateinit var btnDelete: Button
    lateinit var btnUpdate: Button
    lateinit var btnTrack: Button
    lateinit var btnOrgTrack: Button
    lateinit var btnPending: Button
    lateinit var btnConfirm: Button
    lateinit var btnGetCampaign: Button
    lateinit var btnGetReferral: Button
    lateinit var btnCapture: Button
    lateinit var btnReward: Button
    lateinit var btnReffer: Button
    lateinit var txtReponse: TextView
    private val rh = instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAdd = findViewById(R.id.btnAdd)
        btnGet = findViewById(R.id.btnGet)
        btnDelete = findViewById(R.id.btnDelete)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnTrack = findViewById(R.id.btnTrack)
        btnOrgTrack = findViewById(R.id.btnOrgTrack)
        btnPending = findViewById(R.id.btnPending)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnGetCampaign = findViewById(R.id.btnGetCampaign)
        btnGetReferral = findViewById(R.id.btnGetReferral)
        btnCapture = findViewById(R.id.btnCapture)
        btnReward = findViewById(R.id.btnReward)
        btnReffer = findViewById(R.id.btnReferrer)
        txtReponse = findViewById(R.id.txtReponse)

        btnAdd.setOnClickListener(this)
        btnGet.setOnClickListener(this)
        btnTrack.setOnClickListener(this)
        btnOrgTrack.setOnClickListener(this)
        btnPending.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
        btnGetCampaign.setOnClickListener(this)
        btnGetReferral.setOnClickListener(this)
        btnCapture.setOnClickListener(this)
        btnReward.setOnClickListener(this)
        btnReffer.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnUpdate.setOnClickListener(this)
        Log.e("Response", rh?.deviceInfo?.getIpAddress().toString())
    }

    override fun onFailureCallback(response: String) {
        Log.e("Response", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }

    override fun onSuccessCallback(response: String) {
        Log.e("onSuccessCallback", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }

    override fun onClick(v: View) {
        val referralParams = ReferralParams()


        when (v.id) {
            R.id.btnAdd -> {
                referralParams.email = "Jaspalsinh@aistechnolabs.org"
                referralParams.domain = "https://wongazoma.aistechnolabs.info/action"
                referralParams.name = "Jaspalsinh Gohil"
                referralParams.referrer = ""
                referralParams.uuid = "MF67c1950f09"
                referralParams.ip_address = rh?.deviceInfo?.getIpAddress()
                referralParams.device = rh?.deviceInfo?.getDeviceModel()
                referralParams.os_type = rh?.deviceInfo?.getOperatingSystem()
                referralParams.screen_size = rh?.deviceInfo?.getDeviceScreenSize()
                referralParams.status = "custom_event_pending"

                Log.e("params", Gson().toJson(referralParams))
                rh?.formSubmit(this, referralParams)
            }
            R.id.btnGet -> instance?.getSubscriber(this)
            R.id.btnDelete -> instance?.deleteSubscriber(this)
            R.id.btnUpdate -> {
                referralParams.name = "AndiDevOps"
                rh?.updateSubscriber(this, referralParams)
            }
            R.id.btnTrack -> {
                referralParams.email = "Jayden@gmail.com"
                referralParams.name = "AndiDev"
                rh?.trackReferral(this, referralParams)
            }
            R.id.btnCapture -> {
                referralParams.social = "Whatsapp"
                rh?.captureShare(this, referralParams)
            }
            R.id.btnGetReferral -> instance?.getMyReferrals(this)
            R.id.btnGetCampaign -> instance?.getLeaderboard(this)
            R.id.btnConfirm -> instance?.confirmReferral(this)
            R.id.btnPending -> {
                referralParams.email = "Jayden@gmail.com"
                referralParams.name = "AndiDev"
                referralParams.ip_address = DeviceInfo(this).getIpAddress()
                referralParams.screen_size = DeviceInfo(this).getDeviceScreenSize()
                referralParams.device = DeviceInfo(this).getOperatingSystem()
                referralParams.referrer = ""
                rh?.pendingReferral(this, referralParams)
            }
            R.id.btnOrgTrack -> {
                referralParams.email = "Jayden@gmail.com"
                rh?.organicTrackReferral(this, referralParams)
            }
            R.id.btnReward -> {
                rh?.getRewards(this)
            }
            R.id.btnReferrer -> {
                rh?.getReferrer(this)

            }
        }

    }

    override fun onMyReferralSuccessCallback(response: String) {
        Log.e("onMyReferralSuccess", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }

    override fun onMyReferralFailureCallback(response: String) {
        Log.e("onMyReferralSuccess", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }

    override fun onLeaderBoardReferralSuccessCallback(response: String) {
        Log.e("onLeaderBoardSuccess", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }

    override fun onLeaderBoardReferralFailureCallback(response: String) {
        Log.e("onLeaderBoardSuccess", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }

    override fun onRewardSuccessCallback(response: String) {
        Log.e("onRewardSuccess", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }

    override fun onRewardFailureCallback(response: String) {
        Log.e("onRewardSuccess", Gson().toJson(response))
        txtReponse.text = "Response : " + Gson().toJson(response)
    }


}

