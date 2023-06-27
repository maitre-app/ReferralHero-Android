package com.sdk.referral.utils

import android.content.Context
import android.os.RemoteException
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import java.util.*

object StoreReferrerGooglePlayStore {
    private var callback_: IGoogleInstallReferrerEvents? = null
    var hasBeenUsed = false
    var erroredOut = false
    var clickTimestamp = Long.MIN_VALUE
    var installBeginTimestamp = Long.MIN_VALUE
    var rawReferrer: String? = null

    fun fetch(context: Context?, iGoogleInstallReferrerEvents: IGoogleInstallReferrerEvents?) {
        callback_ = iGoogleInstallReferrerEvents
        hasBeenUsed = true
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                PrefHelper.Debug("Google Play onInstallReferrerSetupFinished, responseCode = $responseCode")
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> try {
                        val response = referrerClient.installReferrer
                        if (response != null) {
                            rawReferrer = response.installReferrer
                            clickTimestamp = response.referrerClickTimestampSeconds
                            installBeginTimestamp = response.installBeginTimestampSeconds
                        }
                        referrerClient.endConnection()
                        onReferrerClientFinished(
                            context,
                            rawReferrer,
                            clickTimestamp,
                            installBeginTimestamp,
                            referrerClient.javaClass.name
                        )
                    } catch (ex: RemoteException) {
                        PrefHelper.Debug("onInstallReferrerSetupFinished() Remote Exception: " + ex.message)
                        onReferrerClientError()
                    } catch (ex: Exception) {
                        PrefHelper.Debug("onInstallReferrerSetupFinished() Exception: " + ex.message)
                        onReferrerClientError()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED, InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE, InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR, InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {
                        PrefHelper.Debug("responseCode: $responseCode")
                        // Play Store service is not connected now - potentially transient state.
                        onReferrerClientError()
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // "This does not remove install referrer service connection itself - this binding
                // to the service will remain active, and you will receive a call to onInstallReferrerSetupFinished(int)
                // when install referrer service is next running and setup is complete."
                // https://developer.android.com/reference/com/android/installreferrer/api/InstallReferrerStateListener.html#oninstallreferrerservicedisconnected
                PrefHelper.Debug("onInstallReferrerServiceDisconnected()")
            }
        })
        Timer().schedule(object : TimerTask() {
            override fun run() {
                PrefHelper.Debug("Google Store Referrer fetch lock released by timer")
                reportInstallReferrer()
            }
        }, 1500)
    }

    private fun onReferrerClientError() {
        erroredOut = true
        reportInstallReferrer()
    }

    fun reportInstallReferrer() {
        if (callback_ != null) {
            callback_!!.onGoogleInstallReferrerEventsFinished()
            callback_ = null
        }
    }

    internal fun onReferrerClientFinished(
        context: Context?,
        rawReferrerString: String?,
        clickTS: Long,
        InstallBeginTS: Long,
        clientName: String
    ) {
        PrefHelper.getInstance(context!!)?.appStoreReferrer = rawReferrerString
        PrefHelper.Debug("$clientName onReferrerClientFinished() Referrer: $rawReferrerString Click Timestamp: $clickTS Install Timestamp: $InstallBeginTS")
        reportInstallReferrer()
    }

    interface IGoogleInstallReferrerEvents {
        fun onGoogleInstallReferrerEventsFinished()
    }
}