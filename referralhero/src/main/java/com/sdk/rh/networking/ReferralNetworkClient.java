package com.sdk.rh.networking;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sdk.rh.PrefHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReferralNetworkClient {

    private OkHttpClient client;
    private final Gson gson;

    public ReferralNetworkClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public void callApiGet(String endpoint, Map<String, String> queryParams, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(PrefHelper.getAPIBaseUrl() + endpoint).newBuilder();
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public <T> void callApiPost(String endpoint, Map<String, String> params, Type responseType, ServerCallback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = formBuilder.build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(PrefHelper.getAPIBaseUrl() + endpoint).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseString = response.body().string();
                    Log.e("responseString",responseString);
                    Type apiResponseType = TypeToken.getParameterized(ApiResponse.class, responseType).getType();
                    ApiResponse parsedResponse = gson.fromJson(responseString, apiResponseType);
                    callback.onResponse(call, parsedResponse);
                } catch (Exception e) {
                    callback.onFailure(call, e);
                }
            }
        });
    }


}
