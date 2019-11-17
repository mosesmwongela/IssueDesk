package com.ictlife.issuedesk.rest;


import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    public static final String STAGING_URL = "http://176.58.124.136:9010";
    private static final String TAG = "ApiManager";
    private static APIService apiService;
    private static ApiManager apiManager;
    String ictlife_header_value = "user";

    private ApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(STAGING_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    //getDashBoardData
    public void getDashBoardData(String user_token, Callback<JsonElement> callback) {
        Call<JsonElement> apiCall = apiService.getDashboardData(user_token);
        apiCall.enqueue(callback);
    }

}