package com.sdk.referral.networking

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sdk.referral.model.*
import com.sdk.referral.utils.PrefHelper
import com.sdk.referral.utils.RHUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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
    ): ApiResponse<SubscriberData> {

        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!).addHeader("Authorization", RHUtil.readRhKey(context))
                .addHeader("Accept", "application/vnd.referralhero.v1")
                .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val parsedResponse: ApiResponse<SubscriberData> = gson.fromJson(
                responseString,
                object : TypeToken<ApiResponse<SubscriberData>>() {}.type
            )
            if (response.code == 200) {
                parsedResponse
            } else {
                ApiResponse("error", parsedResponse.message, parsedResponse.code, null, null, 0)
            }
        }
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
    ): ApiResponse<SubscriberData> {

        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val requestBody: RequestBody = Gson().toJson(referralParams).toRequestBody(jsonMediaType)
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder =
            Request.Builder().url(url!!).addHeader("Authorization", RHUtil.readRhKey(context))
                .addHeader("Accept", "application/vnd.referralhero.v1").post(requestBody)

        val request = requestBuilder.build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val parsedResponse: ApiResponse<SubscriberData> = gson.fromJson(
                responseString,
                object : TypeToken<ApiResponse<SubscriberData>>() {}.type
            )
            if (response.code == 200) {
                parsedResponse
            } else {
                ApiResponse("error", parsedResponse.message, parsedResponse.code, null, null, 0)
            }

        }
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
    ): ApiResponse<SubscriberData> {

        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder =
            Request.Builder().url(url!!).addHeader("Authorization", RHUtil.readRhKey(context))
                .addHeader("Accept", "application/vnd.referralhero.v1")
                .addHeader("Content-Type", "application/json").delete()

        val request = requestBuilder.build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val parsedResponse: ApiResponse<SubscriberData> = gson.fromJson(
                responseString,
                object : TypeToken<ApiResponse<SubscriberData>>() {}.type
            )
            if (response.code == 200) {
                parsedResponse
            } else {
                ApiResponse("error", parsedResponse.message, parsedResponse.code, null, null, 0)
            }
        }
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
    ): ApiResponse<SubscriberData> {

        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val requestBody: RequestBody = Gson().toJson(referralParams).toRequestBody(jsonMediaType)
        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder =
            Request.Builder().url(url!!).addHeader("Authorization", RHUtil.readRhKey(context))
                .addHeader("Accept", "application/vnd.referralhero.v1").patch(requestBody)

        val request = requestBuilder.build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val parsedResponse: ApiResponse<SubscriberData> = gson.fromJson(
                responseString,
                object : TypeToken<ApiResponse<SubscriberData>>() {}.type
            )
            if (response.code == 200) {
                parsedResponse
            } else {
                ApiResponse("error", parsedResponse.message, parsedResponse.code, null, null, 0)
            }

        }
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
    ): ApiResponse<ListSubscriberData> {

        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!).addHeader("Authorization", RHUtil.readRhKey(context))
                .addHeader("Accept", "application/vnd.referralhero.v1")
                .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val parsedResponse: ApiResponse<ListSubscriberData> = gson.fromJson(
                responseString,
                object : TypeToken<ApiResponse<ListSubscriberData>>() {}.type
            )
            if (response.code == 200) {
                parsedResponse
            } else {
                ApiResponse("error", parsedResponse.message, parsedResponse.code, null, null, 0)
            }
        }
    }

    /**
    - Created a new function `serverRequestGetLeaderboardAsync` to handle GET requests.
    - Removed the creation of the request body since it's not needed for GET requests.
    - Utilized the `get()` method to specify the request type as GET.
    - Retained the logic for building the URL and adding headers.
    - Used `withContext` and `Dispatchers.IO` for performing the network request asynchronously.
    - Handled the response based on the HTTP status code, returning the parsed response if code is 200, otherwise returning a new `ApiResponse` object with the error details.
     * */
    suspend fun <T> serverRequestGetLeaderboardAsync(
        context: Context, endpoint: String
    ): ApiResponse<RankingDataContent> {

        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!).addHeader("Authorization", RHUtil.readRhKey(context))
                .addHeader("Accept", "application/vnd.referralhero.v1")
                .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val parsedResponse: ApiResponse<RankingDataContent> = gson.fromJson(
                responseString,
                object : TypeToken<ApiResponse<RankingDataContent>>() {}.type
            )
            if (response.code == 200) {
                parsedResponse
            } else {
                ApiResponse("error", parsedResponse.message, parsedResponse.code, null, null, 0)
            }
        }
    }

    suspend fun serverRequestRewardAsync(
        context: Context, endpoint: String
    ): ApiResponse<ListSubscriberData> {

        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()
        val requestBuilder =
            Request.Builder().url(url!!).addHeader("Authorization", RHUtil.readRhKey(context))
                .addHeader("Accept", "application/vnd.referralhero.v1")
                .addHeader("Content-Type", "application/json").get()

        val request = requestBuilder.build()
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            val parsedResponse: ApiResponse<ListSubscriberData> = gson.fromJson(
                responseString,
                object : TypeToken<ApiResponse<ListSubscriberData>>() {}.type
            )
            if (response.code == 200) {
                parsedResponse
            } else {
                ApiResponse("error", parsedResponse.message, parsedResponse.code, null, null, 0)
            }
        }
    }

}