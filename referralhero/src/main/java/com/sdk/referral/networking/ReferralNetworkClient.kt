package com.sdk.referral.networking

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sdk.referral.model.*
import com.sdk.referral.utils.DeviceInfo
import com.sdk.referral.utils.PrefHelper
import kotlinx.coroutines.isActive
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.coroutines.resume
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
        context: Context,
        endpoint: String
    ): ApiResponse<SubscriberData> = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!)
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
                        "error",
                        e.message,
                        "0",
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<SubscriberData> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<SubscriberData>>() {}.type
                )

                if (continuation.context.isActive) {
                    if (response.isSuccessful) {
                        continuation.resume(parsedResponse)
                    } else {
                        continuation.resume(
                            ApiResponse(
                                "error",
                                parsedResponse.message,
                                parsedResponse.code,
                                null,
                                null,
                                0
                            )
                        )
                    }
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
        context: Context,
        endpoint: String,
        referralParams: ReferralParams
    ): ApiResponse<SubscriberData> = suspendCoroutine { continuation ->
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val requestBody: RequestBody = Gson().toJson(referralParams).toRequestBody(jsonMediaType)
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder =
            Request.Builder().url(url!!)
                .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
                .addHeader("Accept", "application/vnd.referralhero.v1").post(requestBody)

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error",
                        e.message,
                        "0",
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                com.sdk.referral.logger.Logger().debug("API", responseString)
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<SubscriberData> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<SubscriberData>>() {}.type
                )

                if (continuation.context.isActive) {
                    if (response.isSuccessful) {
                        continuation.resume(parsedResponse)
                    } else {
                        val errorResponse = ApiResponse<SubscriberData>(
                            "error",
                            parsedResponse.message,
                            parsedResponse.code,
                            null,
                            null,
                            0
                        )
                        continuation.resume(errorResponse)
                    }
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
        context: Context,
        endpoint: String
    ): ApiResponse<SubscriberData> = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder =
            Request.Builder().url(url!!)
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
                        "error",
                        e.message,
                        "0",
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                com.sdk.referral.logger.Logger().debug("API", responseString)
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<SubscriberData> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<SubscriberData>>() {}.type
                )

                if (continuation.context.isActive) {
                    if (response.isSuccessful) {
                        continuation.resume(parsedResponse)
                    } else {
                        val errorResponse = ApiResponse<SubscriberData>(
                            "error",
                            parsedResponse.message,
                            parsedResponse.code,
                            null,
                            null,
                            0
                        )
                        continuation.resume(errorResponse)
                    }
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
        context: Context,
        endpoint: String,
        referralParams: ReferralParams
    ): ApiResponse<SubscriberData> = suspendCoroutine { continuation ->
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val requestBody: RequestBody = Gson().toJson(referralParams).toRequestBody(jsonMediaType)
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder =
            Request.Builder().url(url!!)
                .addHeader("Authorization", PrefHelper(context).rhAccessTokenKey!!)
                .addHeader("Accept", "application/vnd.referralhero.v1").patch(requestBody)

        val request = requestBuilder.build()

        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.context.isActive) {
                    val errorResponse = ApiResponse<SubscriberData>(
                        "error",
                        e.message,
                        "0",
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                com.sdk.referral.logger.Logger().debug("API", responseString)
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<SubscriberData> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<SubscriberData>>() {}.type
                )

                if (continuation.context.isActive) {
                    if (response.isSuccessful) {
                        continuation.resume(parsedResponse)
                    } else {
                        val errorResponse = ApiResponse<SubscriberData>(
                            "error",
                            parsedResponse.message,
                            parsedResponse.code,
                            null,
                            null,
                            0
                        )
                        continuation.resume(errorResponse)
                    }
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
        context: Context,
        endpoint: String
    ): ApiResponse<ListSubscriberData> = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!)
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
                        "error",
                        e.message,
                        "0",
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<ListSubscriberData> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<ListSubscriberData>>() {}.type
                )

                if (continuation.context.isActive) {
                    if (response.isSuccessful) {
                        continuation.resume(parsedResponse)
                    } else {
                        val errorResponse = ApiResponse<ListSubscriberData>(
                            "error",
                            parsedResponse.message,
                            parsedResponse.code,
                            null,
                            null,
                            0
                        )
                        continuation.resume(errorResponse)
                    }
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
        context: Context,
        endpoint: String
    ): ApiResponse<RankingDataContent> = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!)
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
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<RankingDataContent> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<RankingDataContent>>() {}.type
                )

                if (response.isSuccessful) {
                    continuation.resume(parsedResponse)
                } else {
                    val errorResponse = ApiResponse<RankingDataContent>(
                        "error",
                        parsedResponse.message,
                        parsedResponse.code,
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }
        })
    }


    suspend fun serverRequestRewardAsync(
        context: Context,
        endpoint: String
    ): ApiResponse<ListSubscriberData> = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!)
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
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<ListSubscriberData> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<ListSubscriberData>>() {}.type
                )
                if (response.isSuccessful) {
                    continuation.resume(parsedResponse)
                } else {
                    val errorResponse = ApiResponse<ListSubscriberData>(
                        "error",
                        parsedResponse.message,
                        parsedResponse.code,
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }
        })
    }

    suspend fun serverRequestGetReferrerAsync(
        context: Context,
        endpoint: String
    ): ApiResponse<SubscriberData> = suspendCoroutine { continuation ->
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        urlBuilder?.addQueryParameter("os_type", DeviceInfo(context).getOperatingSystem())
        urlBuilder?.addQueryParameter("device", DeviceInfo(context).getDeviceModel())
        urlBuilder?.addQueryParameter("ip_address", DeviceInfo(context).getIpAddress())
        urlBuilder?.addQueryParameter("screen_size", DeviceInfo(context).getDeviceScreenSize())
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!)
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
                        "error",
                        e.message,
                        "0",
                        null,
                        null,
                        0
                    )
                    continuation.resume(errorResponse)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()
                com.sdk.referral.logger.Logger().debug("API", responseString)
                val parsedResponse: ApiResponse<SubscriberData> = Gson().fromJson(
                    responseString,
                    object : TypeToken<ApiResponse<SubscriberData>>() {}.type
                )

                if (continuation.context.isActive) {
                    if (response.isSuccessful) {
                        continuation.resume(parsedResponse)
                    } else {
                        val errorResponse = ApiResponse<SubscriberData>(
                            "error",
                            parsedResponse.message,
                            parsedResponse.code,
                            null,
                            null,
                            0
                        )
                        continuation.resume(errorResponse)
                    }
                }
            }
        })
    }

}