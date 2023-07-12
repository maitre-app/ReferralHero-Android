package com.sdk.referral

import android.content.Context
import android.text.TextUtils
import com.sdk.referral.logger.Logger
import com.sdk.referral.model.ReferralParams
import com.sdk.referral.networking.ReferralNetworkClient
import com.sdk.referral.utils.DeviceInfo
import com.sdk.referral.utils.PrefHelper
import com.sdk.referral.utils.RHUtil
import com.sdk.referral.utils.StoreReferrerGooglePlayStore.IGoogleInstallReferrerEvents
import com.sdk.referral.utils.StoreReferrerGooglePlayStore.fetch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


/**
 * Created by Jaspalsinh Gohil(Jayden) on 02-05-2023.
 */
class RH(var context_: Context) {
    val prefHelper: PrefHelper
    val deviceInfo: DeviceInfo
    private var referralNetworkClient: ReferralNetworkClient
    private var registerSubscriberCallback: RHReferralCallBackListener? = null
    private var myReferralCallback: RHMyReferralCallBackListener? = null
    private var leaderBoardReferralCallback: RHLeaderBoardReferralCallBackListener? = null
    private var rewardCallback: RHRewardCallBackListener? = null

    var logger: Logger? = null

    init {
        deviceInfo = DeviceInfo(context_)
        prefHelper = PrefHelper(context_)
        logger = Logger()
        referralNetworkClient = ReferralNetworkClient()
        fetchInstallReferrer(context_)

    }

    /**
     * User can add this method to their signup form or any other place from where they
     * want to add customers to the referral program
     *
     * @param callback -- callback call success and failure method
     */
    fun formSubmit(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)

        if (prefHelper.appStoreReferrer?.trim()?.isNotEmpty() == true) {
            if (!prefHelper.appStoreReferrer.toString().trim().equals("NO_STRING_VALUE", true))
                referralParams.referrer = prefHelper.appStoreReferrer
        }

        if (referralParams.ip_address.isNullOrEmpty()) {
            if (!prefHelper.getString("RHSDKIP").toString().equals("NO_STRING_VALUE", true))
                referralParams.ip_address = prefHelper.getString("RHSDKIP")
            else
                referralParams.ip_address = deviceInfo.getIpAddress()
        }

        //need to pass "Android" in device parameter for android devices and "IPhone" for IOS Devices it required value should be only this like /mobile|android|iphone|ipad|ipod|blackberry|iemobile|opera mini/
        referralParams.device = "android"
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestCallBackAsync(
                        context_, "${prefHelper.rhCampaignID}/subscribers/", referralParams
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        val dataObject = jsonObject.optJSONObject("data")
                        val id = dataObject?.optString("id", "")
                        val universalLink = dataObject?.optString("universal_link", "")
                        prefHelper.rHSubscriberID = id
                        prefHelper.rHReferralLink = universalLink
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        prefHelper.rHReferralLink = PrefHelper.NO_STRING_VALUE
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }


    }

    /***
     * This method will only be used when user want get a subscriber detail.
     * it is only get a single subscriber detail.
     * @param callback  -- callback call success and failure method
     */
    fun getSubscriber(callback: RHReferralCallBackListener?) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestGetAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/${prefHelper.rHSubscriberID}"
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }

            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }
    }

    /***
     * This method will only be used when user want Delete a subscriber.
     * it is only Delete a single subscriber.
     * @param callback  -- callback call success and failure method
     */
    fun deleteSubscriber(callback: RHReferralCallBackListener?) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestDeleteAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/${prefHelper.rHSubscriberID}"
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        prefHelper.clearPrefOnBranchKeyChange()
                        prefHelper.rHSubscriberID = ""
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }

    }

    fun clearSDKData() {
        prefHelper.clearPrefOnBranchKeyChange()
    }

    /***
     * This method will only be used when user want Update a subscriber.
     * it is only Update a single subscriber.
     * @param callback  -- callback call success and failure method
     */
    fun updateSubscriber(
        callback: RHReferralCallBackListener?, referralParams: ReferralParams
    ) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestPatchAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/${prefHelper.rHSubscriberID}",
                        referralParams
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
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
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestCallBackAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/track_referral_conversion_event",
                        referralParams
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }

    }

    /**
     * It is used to send the Share event to the RH. It means If user call this method
     * with any params like facebook, messenger, etc. We capture this as the share in our system.
     * **/
    fun captureShare(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestCallBackAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/${prefHelper.rHSubscriberID}/click_capture",
                        referralParams
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }

    }


    /**
     * To add a pending referral, simply call ReferralHero's
     * RH.pendingReferral(callback,param) function and send the user information such as email address and name.
     * */
    fun pendingReferral(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestCallBackAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/pending_referral",
                        referralParams
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }

    }


    /**
     *  If you would like to track referrals or add organic subscribers on the conversion
     *  page to your referral campaign, you can use this function
     * */
    fun organicTrackReferral(
        callback: RHReferralCallBackListener?, referralParams: ReferralParams
    ) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestCallBackAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/organic_track_referral",
                        referralParams
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }
    }

    fun getReferrer(
        callback: RHReferralCallBackListener?
    ) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestGetReferrerAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/referrer",
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }

    }

    /**
     * Confirm a referral. Useful when your campaign has enabled the "Manual confirmation"
     * option and you want to confirm referrals when a specific event occur (e.g: upgrade to a paid plan,
     * end of trial, etc)
     * */
    fun confirmReferral(callback: RHReferralCallBackListener?) {
        registerSubscriberCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestCallBackAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/${prefHelper.rHSubscriberID}/confirm",
                        ReferralParams()
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        registerSubscriberCallback?.onSuccessCallback(response)
                    } else {
                        registerSubscriberCallback?.onFailureCallback(response)
                    }

                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }

    }


    /*
    - Created a new function `getMyReferrals` to fetch referral data specific to the current subscriber.
    - Updated the function signature to include the `callback` parameter of type `RHMyReferralCallBackListener`.
    - Utilized coroutine scope and `Dispatchers.IO` for performing the network request asynchronously.
    - Invoked `serverRequestGetMyReferralAsync` from the `referralNetworkClient_` to retrieve the referral data.
    - Utilized `withContext` and `Dispatchers.Main` to handle the response on the main thread.
    - Implemented `handleMyReferralApiResponse` to process the API response and invoke the appropriate callbacks.
    - Added error handling using `try-catch` block to catch any exceptions during the network request.
   * */
    fun getMyReferrals(callback: RHMyReferralCallBackListener?) {
        myReferralCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestGetMyReferralAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/${prefHelper.rHSubscriberID}/referrals_data"
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        myReferralCallback?.onMyReferralSuccessCallback(response)
                    } else {
                        myReferralCallback?.onMyReferralFailureCallback(response)
                    }
                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }

    }

    /*
    - Created a new function `getLeaderboard` to fetch leaderboard data.
    - Updated the function signature to include the `callback` parameter of type `RHLeaderBoardReferralCallBackListener`.
    - Utilized coroutine scope and `Dispatchers.IO` for performing the network request asynchronously.
    - Invoked `serverRequestGetLeaderboardAsync` from the `referralNetworkClient_` to retrieve the leaderboard data.
    - Utilized `withContext` and `Dispatchers.Main` to handle the response on the main thread.
    - Implemented `handleLeaderBoardReferralApiResponse` to process the API response and invoke the appropriate callbacks.
    - Added error handling using `try-catch` block to catch any exceptions during the network request.
    * */
    fun getLeaderboard(callback: RHLeaderBoardReferralCallBackListener?) {
        leaderBoardReferralCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)

        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestGetLeaderboardAsync(
                        context_, "${prefHelper.rhCampaignID}/leaderboard"
                    )
                    // Handle the response
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        leaderBoardReferralCallback?.onLeaderBoardReferralSuccessCallback(response)
                    } else {
                        leaderBoardReferralCallback?.onLeaderBoardReferralFailureCallback(response)
                    }
                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }
    }

    fun getRewards(callback: RHRewardCallBackListener?) {
        rewardCallback = callback
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestRewardAsync(
                        context_,
                        "${prefHelper.rhCampaignID}/subscribers/${prefHelper.rHSubscriberID}/rewards"
                    )
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.optString("status")
                    if (status.equals("ok", true)) {
                        rewardCallback?.onRewardSuccessCallback(response)
                    } else {
                        prefHelper.rHReferralLink = PrefHelper.NO_STRING_VALUE
                        rewardCallback?.onRewardFailureCallback(response)
                    }
                } catch (exception: Exception) {
                    logger?.error(exception.toString())
                }
            }
        } catch (exception: Exception) {
            logger?.error(exception.toString())
        }
    }

    // Method to fetch install referrer
    private fun fetchInstallReferrer(context: Context?) {
        fetch(context, object : IGoogleInstallReferrerEvents {
            override fun onGoogleInstallReferrerEventsFinished() {
                Logger().warn("Referrer Data:  ${RHReferral_?.prefHelper?.appStoreReferrer}")
            }
        })
    }

    private fun getPublicIP(): String {
        var ipAddress = ""
        val mainCoroutineScope = CoroutineScope(Dispatchers.Main)
        try {
            mainCoroutineScope.launch {
                ipAddress = ReferralNetworkClient().getIpAddressAsync(context_)
                RHReferral_?.prefHelper?.setString("RHSDKIP", ipAddress)
                Logger().warn("Ip Address:  ${RHReferral_?.prefHelper?.getString("RHSDKIP")}")
            }
        } catch (exception: Exception) {
            Logger().error(exception.toString())
        }
        return ipAddress

    }


    interface RHReferralCallBackListener {
        fun onSuccessCallback(response: String)
        fun onFailureCallback(response: String)
    }

    interface RHMyReferralCallBackListener {
        fun onMyReferralSuccessCallback(response: String)
        fun onMyReferralFailureCallback(response: String)
    }

    interface RHLeaderBoardReferralCallBackListener {
        fun onLeaderBoardReferralSuccessCallback(response: String)
        fun onLeaderBoardReferralFailureCallback(response: String)
    }

    interface RHRewardCallBackListener {
        fun onRewardSuccessCallback(response: String)
        fun onRewardFailureCallback(response: String)
    }


    companion object {


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
                } else {
                    RHReferral_?.getPublicIP()
                    RHReferral_?.prefHelper?.rhAccessTokenKey = RHReferral_?.context_?.let {
                        RHUtil.readRhKey(
                            it
                        )
                    }
                    RHReferral_?.prefHelper?.rhCampaignID = RHReferral_?.context_?.let {
                        RHUtil.readRhCampaignID(
                            it
                        )
                    }
                }

                return RHReferral_
            }

        @JvmStatic
        @Synchronized
        fun initRHSDK(context: Context, ApiToken: String?, RHuuid: String?): RH? {
            if (RHReferral_ != null) {
                RHReferral_?.getPublicIP()
                Logger().warn("Warning, attempted to reinitialize RH SDK singleton!")
                return RHReferral_
            }
            RHReferral_ = RH(context.applicationContext)
            if (TextUtils.isEmpty(ApiToken)) {
                Logger().warn("Warning: Please enter your access_token in your project's Manifest file!")
                RHReferral_?.prefHelper?.setRHAccessTokenKey(RHUtil.readRhKey(context))
            } else {
                RHReferral_?.prefHelper?.setRHAccessTokenKey(ApiToken)
            }
            if (TextUtils.isEmpty(RHuuid)) {
                Logger().warn("Warning: Please enter your Campaign  uuid in your project's Manifest file!")
                RHReferral_?.prefHelper?.setRHCampaignID(RHUtil.readRhCampaignID(context))
            } else {
                RHReferral_?.prefHelper?.setRHCampaignID(RHuuid)
            }
            RHReferral_?.getPublicIP()
            RHReferral_?.fetchInstallReferrer(context)
            return RHReferral_
        }


    }
}