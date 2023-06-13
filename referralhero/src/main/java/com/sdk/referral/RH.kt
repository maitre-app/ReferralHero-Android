package com.sdk.referral

import android.content.Context
import android.text.TextUtils
import com.sdk.referral.Logger.Logger
import com.sdk.referral.model.*
import com.sdk.referral.networking.ApiConstants
import com.sdk.referral.networking.ReferralNetworkClient
import com.sdk.referral.utils.DeviceInfo
import com.sdk.referral.utils.PrefHelper
import com.sdk.referral.utils.RHUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Jaspalsinh Gohil(Jayden) on 02-05-2023.
 */
class RH(var context_: Context) {
    val prefHelper: PrefHelper
    val deviceInfo: DeviceInfo
    private var referralNetworkClient: ReferralNetworkClient
    private var registerSubscriberCallback: RHReferralCallBackListener? = null
    private var removeSubscriberCallback: RHReferralCallBackListener? = null
    private var trackReferralCallback: RHReferralCallBackListener? = null
    private var myReferralCallback: RHMyReferralCallBackListener? = null
    private var leaderBoardReferralCallback: RHLeaderBoardReferralCallBackListener? = null
    private var rewardCallback: RHRewardCallBackListener? = null

    var logger: Logger? = null

    init {
        deviceInfo = DeviceInfo(context_)
        prefHelper = PrefHelper(context_)
        logger = Logger()
        referralNetworkClient = ReferralNetworkClient()
    }

    /**
     * User can add this method to their signup form or any other place from where they
     * want to add customers to the referral program
     *
     * @param callback -- callback call success and failure method
     */
    fun formSubmit(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        registerSubscriberCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestCallBackAsync(
                    context_, "${prefHelper.rhCampaignID}/subscribers/", referralParams
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.ADD.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }

    /***
     * This method will only be used when user want get a subscriber detail.
     * it is only get a single subscriber detail.
     * @param callback  -- callback call success and failure method
     */
    fun getSubscriber(callback: RHReferralCallBackListener?) {
        registerSubscriberCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestGetAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper.rHSubscriberID}"
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.GET.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }

    /***
     * This method will only be used when user want Delete a subscriber.
     * it is only Delete a single subscriber.
     * @param callback  -- callback call success and failure method
     */
    fun deleteSubscriber(callback: RHReferralCallBackListener?) {
        removeSubscriberCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestDeleteAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper.rHSubscriberID}"
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.DELETE.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }

    }

    /***
     * This method will only be used when user want Update a subscriber.
     * it is only Update a single subscriber.
     * @param callback  -- callback call success and failure method
     */
    fun updateSubscriber(
        callback: RHReferralCallBackListener?, referralParams: ReferralParams
    ) {
        removeSubscriberCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestPatchAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper.rHSubscriberID}",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.UPDATE.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
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
        trackReferralCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestCallBackAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/track_referral_conversion_event",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.TRACK.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }

    /**
     * It is used to send the Share event to the RH. It means If user call this method
     * with any params like facebook, messenger, etc. We capture this as the share in our system.
     * **/
    fun captureShare(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        trackReferralCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestCallBackAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper.rHSubscriberID}/click_capture",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.CAPTURE.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }


    /**
     * To add a pending referral, simply call ReferralHero's
     * RH.pendingReferral(callback,param) function and send the user information such as email address and name.
     * */
    fun pendingReferral(callback: RHReferralCallBackListener?, referralParams: ReferralParams) {
        trackReferralCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestCallBackAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/pending_referral",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.TRACK.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }


    /**
     *  If you would like to track referrals or add organic subscribers on the conversion
     *  page to your referral campaign, you can use this function
     * */
    fun organicTrackReferral(
        callback: RHReferralCallBackListener?,
        referralParams: ReferralParams
    ) {
        trackReferralCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestCallBackAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/organic_track_referral",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.TRACK.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }

    fun getReferrer(
        callback: RHReferralCallBackListener?,
        referralParams: ReferralParams
    ) {
        trackReferralCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestCallBackAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/referrer",
                    referralParams
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.TRACK.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }

    /**
     * Confirm a referral. Useful when your campaign has enabled the "Manual confirmation"
     * option and you want to confirm referrals when a specific event occur (e.g: upgrade to a paid plan,
     * end of trial, etc)
     * */
    fun confirmReferral(callback: RHReferralCallBackListener?) {
        trackReferralCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestCallBackAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper.rHSubscriberID}/confirm",
                    ReferralParams()
                )
                withContext(Dispatchers.Main) {
                    handleApiResponse(response, ApiConstants.OperationType.TRACK.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }

    /**
    - Updated the function signature to include the response type @param ApiResponse<SubscriberData> and an @param ordinal parameter.
    - Added conditional checks based on the response status and `ordinal` value.
    - If the response status is "ok" and the `ordinal` is `ApiConstants.OperationType.DELETE`, clear preferences related to branch key change.
    - If the response status is "ok" and the `ordinal` is `ApiConstants.OperationType.ADD`, update preferences with the subscriber data from the response.
    - Invoked the appropriate callbacks from the `registerSubscriberCallback_` based on the response status and `ordinal`.
     * **/
    private fun handleApiResponse(response: ApiResponse<SubscriberData>, ordinal: Int) {
        if (response.status == "ok") {
            if (ordinal == ApiConstants.OperationType.DELETE.ordinal) prefHelper.clearPrefOnBranchKeyChange()

            if (ordinal == ApiConstants.OperationType.ADD.ordinal) {
                response.data?.let {
                    prefHelper.rHReferralLink = it.referral_link
                    prefHelper.appStoreReferrer = it.universal_link
                    prefHelper.rHSubscriberID = it.id
                }
            }
            registerSubscriberCallback?.onSuccessCallback(response)
        } else {
            prefHelper.rHReferralLink = PrefHelper.NO_STRING_VALUE
            registerSubscriberCallback?.onFailureCallback(response)
        }
    }

    private fun handlerewardApiResponse(response: ApiResponse<ListSubscriberData>, ordinal: Int) {
        if (response.status == "ok") {
            rewardCallback?.onRewardSuccessCallback(response)
        } else {
            prefHelper.rHReferralLink = PrefHelper.NO_STRING_VALUE
            rewardCallback?.onRewardFailureCallback(response)
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestGetMyReferralAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper.rHSubscriberID}/referrals_data"
                )
                withContext(Dispatchers.Main) {
                    handleMyReferralApiResponse(response)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestGetLeaderboardAsync<Any>(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/leaderboard"
                )
                withContext(Dispatchers.Main) {
                    handleLeaderBoardReferralApiResponse(response)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }

    fun getRewards(callback: RHRewardCallBackListener?) {
        rewardCallback = callback
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = referralNetworkClient.serverRequestRewardAsync(
                    context_,
                    "${RHUtil.readRhCampaignID(context_)}/subscribers/${prefHelper.rHSubscriberID}/rewards"
                )
                withContext(Dispatchers.Main) {
                    handlerewardApiResponse(response, ApiConstants.OperationType.GET.ordinal)
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    logger?.error(exception.toString())
                }
            }
        }
    }


    /**
    - Updated the function signature to include the response type `ApiResponse<ListSubscriberData>`.
    - Added conditional checks based on the response status.
     * **/

    private fun handleMyReferralApiResponse(response: ApiResponse<ListSubscriberData>) {
        if (response.status == "ok") {
            myReferralCallback?.onMyReferralSuccessCallback(response)
        } else {
            myReferralCallback?.onMyReferralFailureCallback(response)
        }
    }

    /**
    - Updated the function signature to include the response type `ApiResponse<RankingDataContent>`.
    - Added conditional checks based on the response status.
     * **/
    private fun handleLeaderBoardReferralApiResponse(response: ApiResponse<RankingDataContent>) {
        if (response.status == "ok") {
            leaderBoardReferralCallback?.onLeaderBoardReferralSuccessCallback(response)
        } else {
            leaderBoardReferralCallback?.onLeaderBoardReferralFailureCallback(response)
        }
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
                }
                return RHReferral_
            }

        @Synchronized
        private fun initRHSDK(context: Context, RHaccessToken: String?, RHuuid: String?): RH? {
            if (RHReferral_ != null) {
                Logger().debug("Warning, attempted to reinitialize RH SDK singleton!")
                return RHReferral_
            }
            RHReferral_ = RH(context.applicationContext)
            if (TextUtils.isEmpty(RHaccessToken)) {
                Logger().debug("Warning: Please enter your access_token in your project's Manifest file!")
                RHReferral_!!.prefHelper.setRHAccessTokenKey(PrefHelper.NO_STRING_VALUE)
            } else {
                RHReferral_!!.prefHelper.setRHAccessTokenKey(RHaccessToken)
            }
            if (TextUtils.isEmpty(RHuuid)) {
                Logger().debug("Warning: Please enter your Campaign  uuid in your project's Manifest file!")
                RHReferral_!!.prefHelper.setRHCampaignID(PrefHelper.NO_STRING_VALUE)
            } else {
                RHReferral_!!.prefHelper.setRHCampaignID(RHuuid)
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
            //this.RHReferral_?.context_ = context
            Logger().debug("Warning, attempted to getAutoInstance RH SDK singleton!")
            if (RHReferral_ == null) {
                RHReferral_ =
                    initRHSDK(context, RHUtil.readRhKey(context), RHUtil.readRhCampaignID(context))
            }
            return RHReferral_
        }
    }
}