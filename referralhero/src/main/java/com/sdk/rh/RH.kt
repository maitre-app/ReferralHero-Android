package com.sdk.rh

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.sdk.rh.networking.ApiConstants
import com.sdk.rh.networking.ApiResponse
import com.sdk.rh.networking.ReferralNetworkClient
import com.sdk.rh.networking.ReferralParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


    init {
        deviceInfo = DeviceInfo(context_)
        prefHelper_ = PrefHelper(context_)
        referralNetworkClient_ = ReferralNetworkClient()
    }

    /**
     * User can add this method to their signup form or any other place from where they
     * want to add customers to the referral program
     *
     * @param callback -- callback call success and failure method
     */
    fun formSubmit(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        registerSubscriberCallback_ = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient_.serverRequestCallBackAsync<Any>(
                    context_, "${prefHelper_.rhCampaignID}/subscribers/", referralParams
                )
                withContext(Dispatchers.Main) {
                    handleNewResponse(response)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    PrefHelper.Debug(exception.toString())
                }
            }
        }
    }

    fun getSubscriberByID(callback: RHReferralCallBackListener?) {
        registerSubscriberCallback_ = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient_.serverRequestGetAsync<Any>(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper_.rHSubscriberID}"
                )
                withContext(Dispatchers.Main) {
                    handleResponse(response, ApiConstants.OperationType.GET.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    PrefHelper.Debug(exception.toString())
                }
            }
        }
    }

    /***
     * This method will only be used when user want Delete a subscriber.
     * it is only Delete a single subscriber.
     * @param callback  -- callback call success and failure method
     */
    fun deleteSubscriberByID(callback: RHReferralCallBackListener?) {
        removeSubscriberCallback_ = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient_.serverRequestDeleteAsync<Any>(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper_.rHSubscriberID}"
                )
                withContext(Dispatchers.Main) {
                    handleResponse(response, ApiConstants.OperationType.DELETE.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    PrefHelper.Debug(exception.toString())
                }
            }
        }

    }

    /***
     * This method will only be used when user want Update a subscriber.
     * it is only Update a single subscriber.
     * @param callback  -- callback call success and failure method
     */
    fun updateSubscriberByID(
        callback: RHReferralCallBackListener?, referralParams: ReferralParams
    ) {
        removeSubscriberCallback_ = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient_.serverRequestPatchAsync<Any>(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper_.rHSubscriberID}",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleResponse(response, ApiConstants.OperationType.UPDATE.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    PrefHelper.Debug(exception.toString())
                    //callback?.onFailureCallback(exception)
                }
            }
        }

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
    fun trackReferral(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        trackReferralCallback_ = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient_.serverRequestCallBackAsync<Any>(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/track_referral_conversion_event",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleResponse(response, ApiConstants.OperationType.TRACK.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    PrefHelper.Debug(exception.toString())
                }
            }
        }
    }

    /**
     * It is used to send the Share event to the RH. It means If user call this method
     * with any params like facebook, messenger, etc. We capture this as the share in our system.
     * **/
    fun clickCapture(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        trackReferralCallback_ = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient_.serverRequestCallBackAsync<Any>(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper_.rHSubscriberID}/click_capture",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleResponse(response, ApiConstants.OperationType.CAPTURE.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    PrefHelper.Debug(exception.toString())
                }
            }
        }
    }

    fun handleNewResponse(response: ApiResponse) {
        if (response.status == "ok") {
            response.data?.let {
                prefHelper_.rHReferralLink = it.referral_link
                prefHelper_.rHSubscriberID = it.id
            }
            registerSubscriberCallback_?.onSuccessCallback(response)
        } else {
            prefHelper_.rHReferralLink = PrefHelper.NO_STRING_VALUE
            registerSubscriberCallback_?.onFailureCallback(response)
        }
    }

    fun handleResponse(response: ApiResponse, ordinal: Int) {
        if (response.status == "ok") {
            if (ordinal == ApiConstants.OperationType.DELETE.ordinal) prefHelper_.clearPrefOnBranchKeyChange()

            response.data?.let {
                prefHelper_.rHReferralLink = it.referral_link
                prefHelper_.rHSubscriberID = it.id
            }
            registerSubscriberCallback_?.onSuccessCallback(response)
        } else {
            prefHelper_.rHReferralLink = PrefHelper.NO_STRING_VALUE
            registerSubscriberCallback_?.onFailureCallback(response)
        }
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
            }
            return RHReferral_
        }
    }
}