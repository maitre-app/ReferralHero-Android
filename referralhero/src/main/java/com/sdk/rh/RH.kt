package com.sdk.rh

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.sdk.rh.networking.*
import com.sdk.rh.networking.ApiConstants.RequestParam
import okhttp3.Call

/**
 * Created by Jaspalsinh Gohil(Jayden) on 02-05-2023.
 */
class RH(var context_: Context) {
    val prefHelper_: PrefHelper
    val deviceInfo: DeviceInfo
    var referralNetworkClient_: ReferralNetworkClient
    private var registerSubscriberCallback_: RHReferralCallBackListener? = null
    private var removeSubscriberCallback_: RHReferralCallBackListener? = null
    private var trackReferralCallback_: RHReferralCallBackListener? = null
    private var pendingReferralCallback_: RHReferralCallBackListener? = null
    private var orgaincTrackReferralCallback_: RHReferralCallBackListener? = null
    private var email = ""
    private var customDomain = ""
    private var name = ""
    private var phoneNumber = ""
    private var referrer = ""

    init {
        deviceInfo = DeviceInfo(context_)
        prefHelper_ = PrefHelper(context_)
        referralNetworkClient_ = ReferralNetworkClient()
    }

    /**
     * Call Install Referrer here
     */
    fun registerAppInit() {
        Log.e("Referrer", "registerAppInit")
    }

    fun setEmail(email: String): RH {
        this.email = email
        return this
    }

    fun setPhoneNumber(phoneNumber: String): RH {
        this.phoneNumber = phoneNumber
        return this
    }

    fun setCustomDomain(domain: String): RH {
        customDomain = domain.trim { it <= ' ' }
        return this
    }

    fun setUserName(name: String): RH {
        this.name = name
        return this
    }

    fun setReferrerCode(referrerCode: String): RH {
        referrer = referrerCode
        return this
    }

    /**
     * User can add this method to their signup form or any other place from where they
     * want to add customers to the referral program
     *
     * @param callback -- callback call success and failure method
     */
    fun submit(callback: RHReferralCallBackListener?) {
        registerSubscriberCallback_ = callback
        val queryParams = HashMap<String, String?>()
        if (prefHelper_.rhAccessTokenKey.equals(
                PrefHelper.NO_STRING_VALUE,
                ignoreCase = true
            )
        ) queryParams[RequestParam.RH_API_TOKEN] =
            RHUtil.readRhKey(context_) else queryParams[RequestParam.RH_API_TOKEN] =
            prefHelper_.rhAccessTokenKey
        queryParams[RequestParam.RH_UUID] = prefHelper_.rhCampaignID
        queryParams[RequestParam.RH_EMAIL] = email
        queryParams[RequestParam.RH_PHONE_NUMBER] = phoneNumber
        queryParams[RequestParam.RH_NAME] = name
        queryParams[RequestParam.RH_DOMAIN] = customDomain
        queryParams[RequestParam.RH_REFERRER] = referrer
        Log.e("Param", queryParams.toString())
        Thread {
            referralNetworkClient_.callApiPost<Any>(
                prefHelper_.rhCampaignID + "/subscribers/",
                queryParams,
                Subscriber::class.java,
                object : ServerCallback {
                    override fun onFailure(call: Call?, exception: Exception) {
                        PrefHelper.Debug(exception.toString())
                    }

                    override fun onResponse(call: Call?, response: ApiResponse) {
                        if (response.isSuccess) {
                            if (response.data != null) {
                                prefHelper_.rHReferralLink = response.data?.referrallink
                                prefHelper_.rHSubscriberID = response.data?.id
                            }
                            registerSubscriberCallback_!!.onSuccessCallback(response)
                        } else {
                            prefHelper_.rHReferralLink = PrefHelper.NO_STRING_VALUE
                            registerSubscriberCallback_!!.onFailureCallback(response)
                        }
                    }
                })
        }.start()
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
     */
    fun trackReferral(callback: RHReferralCallBackListener?) {
        trackReferralCallback_ = callback
        val params = HashMap<String, String?>()
        Thread {
            referralNetworkClient_.callApiPost<Any>(
                prefHelper_.rhCampaignID + "/subscribers/",
                params,
                Subscriber::class.java,
                object : ServerCallback {
                    override fun onFailure(call: Call?, exception: Exception) {
                        PrefHelper.Debug(exception.toString())
                    }

                    override fun onResponse(call: Call?, response: ApiResponse) {
                        if (response.isSuccess) {
                        } else {
                        }
                    }
                })
        }.start()
    }

    /***
     * This method will only be used when a multistep event is selected in the goal section for
     * the campaign.
     * If referral code exists then It will create the user as pending referral in the campaign
     * else nothing. Customer can send other data with this method.
     * @param callback -- callback call success and failure method
     */
    fun pendingReferral(callback: RHReferralCallBackListener?) {
        pendingReferralCallback_ = callback
        val params = HashMap<String, String?>()
        Thread {
            referralNetworkClient_.callApiPost<Any>(
                prefHelper_.rhCampaignID + "/subscribers/",
                params,
                Subscriber::class.java,
                object : ServerCallback {
                    override fun onFailure(call: Call?, exception: Exception) {
                        PrefHelper.Debug(exception.toString())
                    }

                    override fun onResponse(call: Call?, response: ApiResponse) {
                        if (response.isSuccess) {
                        } else {
                        }
                    }
                })
        }.start()
    }

    /***
     * This method will only be used when a multistep event is selected in the goal section for
     * the campaign.
     * We can call this method anywhere. If the referral code exists then we will save this as
     * unconfirmed/confirmed else as an organic subscriber.
     * @param callback -- callback call success and failure method
     */
    fun orgaincTrackReferral(callback: RHReferralCallBackListener?) {
        orgaincTrackReferralCallback_ = callback
        val params = HashMap<String, String?>()
        Thread {
            referralNetworkClient_.callApiPost<Any>(
                prefHelper_.rhCampaignID + "/subscribers/",
                params,
                Subscriber::class.java,
                object : ServerCallback {
                    override fun onFailure(call: Call?, exception: Exception) {
                        PrefHelper.Debug(exception.toString())
                    }

                    override fun onResponse(call: Call?, response: ApiResponse) {
                        if (response.isSuccess) {
                        } else {
                        }
                    }
                })
        }.start()
    }

    /***
     * This method will only be used when user want Delete a subscriber.
     * it is only Delete a single subscriber.
     * @param callback  -- callback call success and failure method
     */
    fun removeReferralSubscriber(callback: RHReferralCallBackListener?) {
        removeSubscriberCallback_ = callback
        val params = HashMap<String, String?>()
        Thread {
            referralNetworkClient_.callApiPost<Any>(
                prefHelper_.rhCampaignID + "/subscribers/",
                params,
                Subscriber::class.java,
                object : ServerCallback {
                    override fun onFailure(call: Call?, exception: Exception) {
                        PrefHelper.Debug(exception.toString())
                    }

                    override fun onResponse(call: Call?, response: ApiResponse) {
                        if (response.isSuccess) {
                        } else {
                        }
                    }
                })
        }.start()
    }

    interface RHReferralCallBackListener {
        fun onSuccessCallback(response: ApiResponse?)
        fun onFailureCallback(response: ApiResponse?)
    }

    companion object {
        private val SHORT_LINK: Uri? = null

        /**
         *
         * A [RH] object that is instantiated on init and holds the singleton instance of
         * the class during application runtime.
         */
        private var RHReferral_: RH? = null

        /**
         *
         * Singleton method to return the pre-initialised object of the type [RH].
         * Make sure your app is instantiating [RH] before calling this method
         * or you have created an instance of RH already by calling getInstance(Context ctx).
         *
         * @return An initialised singleton [RH] object
         */
        @JvmStatic
        @get:Synchronized
        val instance: RH?
            get() {
                if (RHReferral_ == null) {
                    PrefHelper.Debug("RH instance is not created yet. Make sure you call getAutoInstance(Context).")
                }
                return RHReferral_
            }

        @Synchronized
        private fun initRHSDK(context: Context, RHaccessToken: String?, RHuuid: String?): RH? {
            if (RHReferral_ != null) {
                PrefHelper.Debug("Warning, attempted to reinitialize RH SDK singleton!")
                return RHReferral_
            }
            RHReferral_ = RH(context.applicationContext)
            if (TextUtils.isEmpty(RHaccessToken)) {
                PrefHelper.Debug("Warning: Please enter your access_token in your project's Manifest file!")
                RHReferral_!!.prefHelper_.setRHAccessTokenKey(PrefHelper.NO_STRING_VALUE)
            } else {
                RHReferral_!!.prefHelper_.setRHAccessTokenKey(RHaccessToken)
            }
            if (TextUtils.isEmpty(RHuuid)) {
                PrefHelper.Debug("Warning: Please enter your Campaign  uuid in your project's Manifest file!")
                RHReferral_!!.prefHelper_.setRHCampaignID(PrefHelper.NO_STRING_VALUE)
            } else {
                RHReferral_!!.prefHelper_.setRHCampaignID(RHuuid)
            }
            return RHReferral_
        }

        /**
         *
         * Singleton method to return the pre-initialised, or newly initialise and return, a singleton
         * object of the type [RH].
         *
         * Use this whenever you need to call a method directly on the [RH] object.
         *
         * @param context A [Context] from which this call was made.
         * @return An initialised [RH] object, either fetched from a pre-initialised
         * instance within the singleton class, or a newly instantiated object where
         * one was not already requested during the current app lifecycle.
         */
        @JvmStatic
        @Synchronized
        fun getAutoInstance(context: Context): RH? {
            PrefHelper.Debug("Warning, attempted to getAutoInstance RH SDK singleton!")
            if (RHReferral_ == null) {
                RHReferral_ =
                    initRHSDK(context, RHUtil.readRhKey(context), RHUtil.readRhCampaignID(context))
                //   getPreinstallSystemData(branchReferral_, context);
            }
            return RHReferral_
        }
    }
}