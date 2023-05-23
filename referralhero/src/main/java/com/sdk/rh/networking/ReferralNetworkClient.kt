package com.sdk.rh.networking

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sdk.rh.PrefHelper
import com.sdk.rh.RHUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException
import java.lang.reflect.Type

class ReferralNetworkClient {
    private val client: OkHttpClient = OkHttpClient()
    private val gson: Gson = Gson()

    fun callApiGet(endpoint: String, queryParams: Map<String?, String?>?, callback: Callback?) {
        val urlBuilder: HttpUrl.Builder =
            (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()!!.newBuilder()
        queryParams?.let {
            for ((key, value) in it) {
                key?.let { urlBuilder.addQueryParameter(it, value) }
            }
        }
        val url: String = urlBuilder.build().toString()
        val request: Request = Request.Builder().url(url).build()
        val call = client.newCall(request)
        call.enqueue(callback!!)
    }


    suspend fun <T> serverRequestCallBackAsync(
        context: Context,
        endpoint: String,
        params: HashMap<String, String?>,
        responseType: Type
    ): ApiResponse {
        val formBuilder = FormBody.Builder()
        params.forEach { (key, value) ->
            value?.let { formBuilder.add(key, it) }
        }
        val requestBody: RequestBody = formBuilder.build()

        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder = Request.Builder()
            .url(url!!)
            .addHeader("Authorization", PrefHelper.getInstance(context)?.rhAccessTokenKey.toString())
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)

        val request = requestBuilder.build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            Log.e("Response", responseString.toString())

            val apiResponseType = TypeToken.getParameterized(
                ApiResponse::class.java, responseType
            ).type
            val parsedResponse: ApiResponse = gson.fromJson(responseString, apiResponseType)
            parsedResponse
        }
    }

    fun <T> serverRequestCallBackAsync(
        context_: Context,
        endpoint: String,
        params: HashMap<String, String?>,
        responseType: Type,
        callback: ServerCallback
    ) {
        val formBuilder = FormBody.Builder()
        params.let {
            for ((key, value) in params) {
                value?.let { it1 -> formBuilder.add(key, it1) }
            }
        }
        val requestBody: RequestBody = formBuilder.build()


        val urlBuilder = (PrefHelper.aPIBaseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
        val url = urlBuilder?.build()?.toString()

        val requestBuilder = Request.Builder()
            .url("https://dev.referralhero.com/api/sdk/v1/lists/MF4345c63888/subscribers/")
            .addHeader("Authorization", RHUtil.readRhKey(context_))
            .addHeader("Accept", "application/vnd.referralhero.v1")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
        val request = requestBuilder.build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call, e)
                Log.e("Response", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e("Response", response.toString())
                try {
                    val responseString = response.body?.string()
                    Log.e("Response", responseString.toString())
                    val apiResponseType = TypeToken.getParameterized(
                        ApiResponse::class.java, responseType
                    ).type
                    val parsedResponse: ApiResponse = gson.fromJson(responseString, apiResponseType)
                    callback.onResponse(call, parsedResponse)
                } catch (e: Exception) {
                    callback.onFailure(call, e)
                }
            }
        })
    }
}