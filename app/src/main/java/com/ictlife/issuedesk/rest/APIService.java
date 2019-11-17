package com.ictlife.issuedesk.rest;

import com.google.gson.JsonElement;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.QueryMap;

public interface APIService {

    //dashboard data
    @GET("/issue_tracking/dashboard")
    Call<JsonElement> getDashboardData(@Header("Authorization") String user_token);

    //get issues
    @GET("/issue_tracking/issues")
    Call<JsonElement> getIssues(@Header("Authorization") String user_token,
                                @QueryMap Map<String, String> params);


}