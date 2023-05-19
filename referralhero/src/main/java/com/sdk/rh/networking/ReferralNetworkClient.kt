package com.sdk.rh.networking

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sdk.rh.PrefHelper
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

    fun <T> callApiPost(
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

        val request = Request.Builder().url(url!!).post(requestBody).build()

        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call, e)
            }

            override fun onResponse(call: Call, response: Response) {
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