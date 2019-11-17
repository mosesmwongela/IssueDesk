package com.ictlife.issuedesk.rest;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface APIService {

    //dashboard data
    @GET("/issue_tracking/dashboard")
    Call<JsonElement> getDashboardData(@Header("Authorization") String user_token);


}