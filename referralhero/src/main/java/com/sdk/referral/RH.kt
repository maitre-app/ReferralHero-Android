package com.sdk.referral

import android.content.Context
import android.text.TextUtils
import com.sdk.referral.logger.Logger
import com.sdk.referral.model.*
import com.sdk.referral.networking.ReferralNetworkClient
import com.sdk.referral.utils.DeviceInfo
import com.sdk.referral.utils.PrefHelper
import com.sdk.referral.utils.RHUtil
import com.sdk.referral.utils.StoreReferrerGooglePlayStore.IGoogleInstallReferrerEvents
import com.sdk.referral.utils.StoreReferrerGooglePlayStore.fetch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            if (!prefHelper.appStoreReferrer.toString().equals("NO_STRING_VALUE", true))
                referralParams.referrer = prefHelper.appStoreReferrer
        }

        if (referralParams.ip_address.isNullOrEmpty())
            referralParams.ip_address = deviceInfo.getIpAddress()
        try {
            mainCoroutineScope.launch {
                try {
                    val response = referralNetworkClient.serverRequestCallBackAsync(
                        context_, "${prefHelper.rhCampaignID}/subscribers/", referralParams
                    )
                    if (response.status == "ok") {
                        prefHelper.rHSubscriberID = response.data?.id
                        prefHelper.rHReferralLink = response.data?.universal_link
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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
                    if (response.status == "ok") {
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

    private fun getPublicIP(): String? {
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
        fun onSuccessCallback(response: ApiResponse<SubscriberData>?)
        fun onFailureCallback(response: ApiResponse<SubscriberData>?)
    }

    interface RHMyReferralCallBackListener {
        fun onMyReferralSuccessCallback(response: ApiResponse<ListSubscriberData>?)
        fun onMyReferralFailureCallback(response: ApiResponse<ListSubscriberData>?)
    }

    interface RHLeaderBoardReferralCallBackListener {
        fun onLeaderBoardReferralSuccessCallback(response: ApiResponse<RankingDataContent>?)
        fun onLeaderBoardReferralFailureCallback(response: ApiResponse<RankingDataContent>?)
    }

    interface RHRewardCallBackListener {
        fun onRewardSuccessCallback(response: ApiResponse<ListSubscriberData>?)
        fun onRewardFailureCallback(response: ApiResponse<ListSubscriberData>?)
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