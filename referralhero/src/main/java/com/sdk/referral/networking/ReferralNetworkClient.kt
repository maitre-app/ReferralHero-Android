package com.sdk.referral.networking

import android.content.Context
import com.google.gson.Gson
import com.sdk.referral.model.ApiResponse
import com.sdk.referral.model.ListSubscriberData
import com.sdk.referral.model.RankingDataContent
import com.sdk.referral.model.ReferralParams
import com.sdk.referral.model.SubscriberData
import com.sdk.referral.utils.DeviceInfo
import com.sdk.referral.utils.ErrorCode
import com.sdk.referral.utils.PrefHelper
import kotlinx.coroutines.isActive
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ReferralNetworkClient {
    private val client: OkHttpClient = OkHttpClient()
    private val gson: Gson = Gson()


    /**
    - Created a new function `serverRequestGetAsync` to handle GET requests.
    - Removed the creation of the request body since it's not needed for GET requests.
    - Utilized the `get()` method to specify the request type as GET.
    - Retained the logic for building the URL and adding headers.
    - Used `withContext` and `Dispatchers.IO` for performing the network request asynchronously.
    - Handled the response based on the HTTP status code, returning the parsed response if code is 200, otherwise returning a new `ApiResponse` object with the error details.
     * */
    suspend fun serverRequestGetAsync(
        context: Context, endpoint: String
    ): String = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    continuation.resumeWithException(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    val responseString = response.body?.string()

                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resumeWithException(Exception("Request failed with code: ${response.message}"))
                        }
                    }
                } else {
                    continuation.resumeWithException(Exception("Request failed with code: ${response.code}"))
                }

            }
        })
    }


    /**
    - Extracted the creation of the JSON request body into a separate variable for improved readability.
    - Used Kotlin's null-safe operators for building the URL and accessing its string representation.
    - Set the appropriate media type for the request body.
    - Added headers for authorization and accept content type.
    - Used `withContext` and `Dispatchers.IO` for performing the network request asynchronously.
    - Handled the response based on the HTTP status code, returning the parsed response if code is 200, otherwise returning a new `ApiResponse` object with the error details.
     * ***/

    suspend fun serverRequestCallBackAsync(
        context: Context, endpoint: String, referralParams: ReferralParams
    ): String = suspendCoroutine { continuation ->
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val requestBody: RequestBody = Gson().toJson(referralParams).toRequestBody(jsonMediaType)
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1").post(requestBody)

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error", e.message, "0", null, null, 0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    val responseString = response.body?.string()

                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resume(responseString!!)
                        }
                    }
                } else {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error",
                        ErrorCode.getMessage(response.code),
                        response.code.toString(),
                        null,
                        null,
                        0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }
            }
        })
    }


    /**
    - Created a new function `serverRequestDeleteAsync` to handle DELETE requests.
    - Updated the function signature to include the required parameters: `context`, `endpoint`, and optional `queryParams`.
    - Utilized the `delete()` method of the `Request.Builder` class to specify the request type as DELETE.
    - Appended query parameters to the URL if provided using the `addQueryParameter` method.
    - Set the appropriate headers for authorization, accept content type, and content type.
    - Used `withContext` and `Dispatchers.IO` for performing the network request asynchronously.
    - Handled the response based on the HTTP status code, returning the parsed response if code is 200, otherwise returning a new `ApiResponse` object with the error details.
     * ***/
    suspend fun serverRequestDeleteAsync(
        context: Context, endpoint: String
    ): String = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json").delete()

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error", e.message, "0", null, null, 0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    val responseString = response.body?.string()

                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resume(responseString!!)
                        }
                    }
                } else {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error",
                        ErrorCode.getMessage(response.code),
                        response.code.toString(),
                        null,
                        null,
                        0
                    )
                    continuation.resume(Gson().toJson(errorResponse))

                }

            }
        })
    }


    /**
    - Added a new function 'serverRequestPatchAsync' to handle PATCH requests.
    - Updated the function signature to include the required parameters: `context`, `endpoint`, and `requestBody`.
    - Built the URL using the `aPIBaseUrl` and `endpoint` parameters.
    - Set the appropriate headers for authorization, accept content type, and content type.
    - Utilized the `patch()` method of the `Request.Builder` class to specify the request type as PATCH.
    - Used `withContext` and `Dispatchers.IO` for performing the network request asynchronously.
    - Handled the response based on the HTTP status code, returning the parsed response if the code is 200, otherwise returning a new `ApiResponse` object with the error details.
     * ***/
    suspend fun serverRequestPatchAsync(
        context: Context, endpoint: String, referralParams: ReferralParams
    ): String = suspendCoroutine { continuation ->
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val requestBody: RequestBody = Gson().toJson(referralParams).toRequestBody(jsonMediaType)
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1").patch(requestBody)

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error", e.message, "0", null, null, 0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    val responseString = response.body?.string()
                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resume(responseString!!)
                        }
                    }
                } else {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error",
                        ErrorCode.getMessage(response.code),
                        response.code.toString(),
                        null,
                        null,
                        0
                    )
                    continuation.resume(Gson().toJson(errorResponse))

                }


            }
        })
    }


    /**
    - Created a new function `serverRequestGetMyReferralAsync` to handle GET requests.
    - Removed the creation of the request body since it's not needed for GET requests.
    - Utilized the `get()` method to specify the request type as GET.
    - Retained the logic for building the URL and adding headers.
    - Used `withContext` and `Dispatchers.IO` for performing the network request asynchronously.
    - Handled the response based on the HTTP status code, returning the parsed response if code is 200, otherwise returning a new `ApiResponse` object with the error details.
     * */
    suspend fun serverRequestGetMyReferralAsync(
        context: Context, endpoint: String
    ): String = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    val errorResponse = ApiResponse<ListSubscriberData>(
                        "error", e.message, "0", null, null, 0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    val responseString = response.body?.string()
                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resume(responseString!!)
                        }
                    }
                } else {
                    val errorResponse = ApiResponse<ListSubscriberData>(
                        "error",
                        ErrorCode.getMessage(response.code),
                        response.code.toString(),
                        null,
                        null,
                        0
                    )
                    continuation.resume(Gson().toJson(errorResponse))

                }


            }
        })
    }


    /**
    - Created a new function `serverRequestGetLeaderboardAsync` to handle GET requests.
    - Removed the creation of the request body since it's not needed for GET requests.
    - Utilized the `get()` method to specify the request type as GET.
    - Retained the logic for building the URL and adding headers.
    - Used `withContext` and `Dispatchers.IO` for performing the network request asynchronously.
    - Handled the response based on the HTTP status code, returning the parsed response if code is 200, otherwise returning a new `ApiResponse` object with the error details.
     * */
    suspend fun serverRequestGetLeaderboardAsync(
        context: Context, endpoint: String
    ): String = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWith(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    val responseString = response.body?.string()
                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resume(responseString!!)
                        }
                    }
                } else {
                    val errorResponse = ApiResponse<RankingDataContent>(
                        "error",
                        ErrorCode.getMessage(response.code),
                        response.code.toString(),
                        null,
                        null,
                        0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }


            }
        })
    }


    suspend fun serverRequestRewardAsync(
        context: Context, endpoint: String
    ): String = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWith(Result.failure(e))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resume(responseString!!)
                        }
                    }
                } else {
                    val errorResponse = ApiResponse<ListSubscriberData>(
                        "error",
                        ErrorCode.getMessage(response.code),
                        response.code.toString(),
                        null,
                        null,
                        0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }

            }
        })
    }

    suspend fun serverRequestGetReferrerAsync(
        context: Context, endpoint: String
    ): String = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("os_type", DeviceInfo(context).getOperatingSystem())
        urlBuilder?.addQueryParameter("device", DeviceInfo(context).getDeviceModel())
        urlBuilder?.addQueryParameter("ip_address", DeviceInfo(context).getIpAddress())
        urlBuilder?.addQueryParameter("screen_size", DeviceInfo(context).getDeviceScreenSize())
        val url = urlBuilder?.build()?.toString()
        val requestBuilder = Request.Builder().url(url!!)
            .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error", e.message, "0", null, null, 0
                    )
                    continuation.resume(Gson().toJson(errorResponse))
                }
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.code == 200 || response.code == 403 || response.code == 401 || response.code == 400) {
                    val responseString = response.body?.string()
                    if (continuation.context.isActive) {
                        if (response.isSuccessful) {
                            continuation.resume(responseString!!)
                        } else {
                            continuation.resume(responseString!!)
                        }
                    }
                } else {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error",
                        ErrorCode.getMessage(response.code),
                        response.code.toString(),
                        null,
                        null,
                        0
                    )
                    continuation.resume(Gson().toJson(errorResponse))

                }


            }
        })
    }

    suspend fun getIpAddressAsync(mContext: Context): String = suspendCoroutine { continuation ->
        val url = "https://api.ipify.org/"
        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    continuation.resume("")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val ipAddress = response.body?.string() ?: ""
                    com.sdk.referral.logger.Logger().warn("IpAddress $ipAddress")
                    PrefHelper(mContext).setString("RHSDKIP", ipAddress)
                    if (continuation.context.isActive) {
                        continuation.resume(ipAddress)
                    }
                } else {
                    if (continuation.context.isActive) {
                        continuation.resume("")
                    }
                }
            }
        })
    }
}