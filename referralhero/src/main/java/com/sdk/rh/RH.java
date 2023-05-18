package com.sdk.rh;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.sdk.rh.networking.ApiConstants;
import com.sdk.rh.networking.ApiResponse;
import com.sdk.rh.networking.ReferralNetworkClient;
import com.sdk.rh.networking.ServerCallback;
import com.sdk.rh.networking.Subscriber;

import java.util.HashMap;

import okhttp3.Call;


/**
 * Created by Jaspalsinh Gohil(Jayden) on 02-05-2023.
 */
public class RH {


    private static Uri SHORT_LINK;
    /**
     * <p>A {@link RH} object that is instantiated on init and holds the singleton instance of
     * the class during application runtime.</p>
     */
    private static RH RHReferral_;
    final PrefHelper prefHelper_;
    private final DeviceInfo deviceInfo_;
    Context context_;
    ReferralNetworkClient referralNetworkClient_;
    private RHReferralCallBackListener registerSubscriberCallback_;
    private RHReferralCallBackListener removeSubscriberCallback_;
    private RHReferralCallBackListener trackReferralCallback_;
    private RHReferralCallBackListener pendingReferralCallback_;
    private RHReferralCallBackListener orgaincTrackReferralCallback_;
    private String email = "";
    private String customDomain = "";
    private String name = "";
    private String phoneNumber = "";
    private String referrer = "";

    public RH(@NonNull Context context) {
        this.context_ = context;
        this.deviceInfo_ = new DeviceInfo(context);
        this.prefHelper_ = new PrefHelper(context);
        this.referralNetworkClient_ = new ReferralNetworkClient();
    }


    /**
     * <p>Singleton method to return the pre-initialised object of the type {@link RH}.
     * Make sure your app is instantiating {@link RH} before calling this method
     * or you have created an instance of RH already by calling getInstance(Context ctx).</p>
     *
     * @return An initialised singleton {@link RH} object
     */
    synchronized public static RH getInstance() {
        if (RHReferral_ == null) {
            PrefHelper.Debug("RH instance is not created yet. Make sure you call getAutoInstance(Context).");
        }
        return RHReferral_;
    }

    synchronized private static RH initRHSDK(@NonNull Context context, String RHaccessToken, String RHuuid) {
        if (RHReferral_ != null) {
            PrefHelper.Debug("Warning, attempted to reinitialize RH SDK singleton!");
            return RHReferral_;
        }
        RHReferral_ = new RH(context.getApplicationContext());
        if (TextUtils.isEmpty(RHaccessToken)) {
            PrefHelper.Debug("Warning: Please enter your access_token in your project's Manifest file!");
            RHReferral_.prefHelper_.setRHAccessTokenKey(PrefHelper.NO_STRING_VALUE);
        } else {
            RHReferral_.prefHelper_.setRHAccessTokenKey(RHaccessToken);
        }

        if (TextUtils.isEmpty(RHuuid)) {
            PrefHelper.Debug("Warning: Please enter your Campaign  uuid in your project's Manifest file!");
            RHReferral_.prefHelper_.setRHCampaignID(PrefHelper.NO_STRING_VALUE);
        } else {
            RHReferral_.prefHelper_.setRHCampaignID(RHuuid);
        }
        return RHReferral_;
    }


    /**
     * <p>Singleton method to return the pre-initialised, or newly initialise and return, a singleton
     * object of the type {@link RH}.</p>
     * <p>Use this whenever you need to call a method directly on the {@link RH} object.</p>
     *
     * @param context A {@link Context} from which this call was made.
     * @return An initialised {@link RH} object, either fetched from a pre-initialised
     * instance within the singleton class, or a newly instantiated object where
     * one was not already requested during the current app lifecycle.
     */
    synchronized public static RH getAutoInstance(@NonNull Context context) {
        PrefHelper.Debug("Warning, attempted to getAutoInstance RH SDK singleton!");
        if (RHReferral_ == null) {
            RHReferral_ = initRHSDK(context, RHUtil.readRhKey(context), RHUtil.readRhCampaignID(context));
            //   getPreinstallSystemData(branchReferral_, context);
        }
        return RHReferral_;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo_;
    }

    /**
     * Call Install Referrer here
     **/

    public void registerAppInit() {
        Log.e("Referrer", "registerAppInit");


    }

    public RH setEmail(String email) {
        this.email = email;
        return this;
    }

    public RH setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public RH setCustomDomain(String domain) {
        this.customDomain = domain.trim();
        return this;
    }

    public RH setUserName(String name) {
        this.name = name;
        return this;
    }

    public RH setReferrerCode(String referrerCode) {
        this.referrer = referrerCode;
        return this;
    }


    /**
     * User can add this method to their signup form or any other place from where they
     * want to add customers to the referral program
     *
     * @param callback -- callback call success and failure method
     **/
    public void submit(RHReferralCallBackListener callback) {
        registerSubscriberCallback_ = callback;
        HashMap<String, String> queryParams = new HashMap<>();
        if (prefHelper_.getRhAccessTokenKey().equalsIgnoreCase(PrefHelper.NO_STRING_VALUE))
            queryParams.put(ApiConstants.RequestParam.RH_API_TOKEN, RHUtil.readRhKey(context_));
        else
            queryParams.put(ApiConstants.RequestParam.RH_API_TOKEN, prefHelper_.getRhAccessTokenKey());

        queryParams.put(ApiConstants.RequestParam.RH_UUID, prefHelper_.getRhCampaignID());
        queryParams.put(ApiConstants.RequestParam.RH_EMAIL, this.email);
        queryParams.put(ApiConstants.RequestParam.RH_PHONE_NUMBER, this.phoneNumber);
        queryParams.put(ApiConstants.RequestParam.RH_NAME, this.name);
        queryParams.put(ApiConstants.RequestParam.RH_DOMAIN, this.customDomain);
        queryParams.put(ApiConstants.RequestParam.RH_REFERRER, this.referrer);

        Log.e("Param", queryParams.toString());
        new Thread(() -> referralNetworkClient_.callApiPost(prefHelper_.getRhCampaignID() + "/subscribers/", queryParams, Subscriber.class, new ServerCallback() {
            @Override
            public void onFailure(Call call, Exception exception) {
                PrefHelper.Debug(exception.toString());
            }

            @Override
            public void onResponse(Call call, ApiResponse response) {
                if (response.isSuccess()) {
                    if (response.getData() != null) {
                        prefHelper_.setRHReferralLink(response.getData().getReferrallink());
                        prefHelper_.setRHSubscriberID(response.getData().getId());
                    }
                    registerSubscriberCallback_.onSuccessCallback(response);
                } else {
                    prefHelper_.setRHReferralLink(PrefHelper.NO_STRING_VALUE);
                    registerSubscriberCallback_.onFailureCallback(response);
                }
            }
        })).start();
    }


    /***
     * This method will only be used when a multistep event is selected in the goal section for
     * the campaign.
     * The use case for this method will be like,
     * Referral is added with a pending status at the time of signup. When they complete the
     * conversion event, the status is changed from pending to unconfirmed/confirmed. The
     * mwr code (if referral is not present in the campaign with pending status) and unique
     * identifier will be required in this method. Customers can send external data with this
     * method like name, extra field, etc.
     * @param callback -- callback call success and failure method
     * **/
    public void trackReferral(RHReferralCallBackListener callback) {
        trackReferralCallback_ = callback;
        HashMap<String, String> params = new HashMap<>();
        new Thread(() -> referralNetworkClient_.callApiPost(prefHelper_.getRhCampaignID() + "/subscribers/", params, Subscriber.class, new ServerCallback() {
            @Override
            public void onFailure(Call call, Exception exception) {
                PrefHelper.Debug(exception.toString());
            }

            @Override
            public void onResponse(Call call, ApiResponse response) {
                if (response.isSuccess()) {

                } else {

                }
            }
        })).start();
    }

    /***
     * This method will only be used when a multistep event is selected in the goal section for
     * the campaign.
     * If referral code exists then It will create the user as pending referral in the campaign
     * else nothing. Customer can send other data with this method.
     * @param callback -- callback call success and failure method
     * **/
    public void pendingReferral(RHReferralCallBackListener callback) {
        pendingReferralCallback_ = callback;
        HashMap<String, String> params = new HashMap<>();
        new Thread(() -> referralNetworkClient_.callApiPost(prefHelper_.getRhCampaignID() + "/subscribers/", params, Subscriber.class, new ServerCallback() {
            @Override
            public void onFailure(Call call, Exception exception) {
                PrefHelper.Debug(exception.toString());
            }

            @Override
            public void onResponse(Call call, ApiResponse response) {
                if (response.isSuccess()) {

                } else {

                }
            }
        })).start();
    }

    /***
     * This method will only be used when a multistep event is selected in the goal section for
     * the campaign.
     * We can call this method anywhere. If the referral code exists then we will save this as
     * unconfirmed/confirmed else as an organic subscriber.
     * @param callback -- callback call success and failure method
     * **/
    public void orgaincTrackReferral(RHReferralCallBackListener callback) {
        orgaincTrackReferralCallback_ = callback;
        HashMap<String, String> params = new HashMap<>();
        new Thread(() -> referralNetworkClient_.callApiPost(prefHelper_.getRhCampaignID() + "/subscribers/", params, Subscriber.class, new ServerCallback() {
            @Override
            public void onFailure(Call call, Exception exception) {
                PrefHelper.Debug(exception.toString());
            }

            @Override
            public void onResponse(Call call, ApiResponse response) {
                if (response.isSuccess()) {

                } else {

                }
            }
        })).start();
    }


    /***
     * This method will only be used when user want Delete a subscriber.
     * it is only Delete a single subscriber.
     * @param callback  -- callback call success and failure method
     * **/

    public void removeReferralSubscriber(RHReferralCallBackListener callback) {
        removeSubscriberCallback_ = callback;
        HashMap<String, String> params = new HashMap<>();
        new Thread(() -> referralNetworkClient_.callApiPost(prefHelper_.getRhCampaignID() + "/subscribers/", params, Subscriber.class, new ServerCallback() {
            @Override
            public void onFailure(Call call, Exception exception) {
                PrefHelper.Debug(exception.toString());
            }

            @Override
            public void onResponse(Call call, ApiResponse response) {
                if (response.isSuccess()) {

                } else {

                }
            }
        })).start();
    }

    public interface RHReferralCallBackListener {
        void onSuccessCallback(ApiResponse response);

        void onFailureCallback(ApiResponse response);
    }


}
