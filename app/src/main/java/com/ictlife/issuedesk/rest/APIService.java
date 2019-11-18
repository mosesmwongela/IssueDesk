package com.ictlife.issuedesk.rest;

import com.google.gson.JsonElement;
import com.ictlife.issuedesk.ui.create.issue.Issue;
import com.ictlife.issuedesk.ui.create.user.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APIService {

    //dashboard data
    @GET("/issue_tracking/dashboard")
    Call<JsonElement> getDashboardData(@Header("Authorization") String user_token);

    //get issues
    @GET("/issue_tracking/issues")
    Call<JsonElement> getIssues(@Header("Authorization") String user_token,
                                @QueryMap Map<String, String> params);

    //customers data
    @GET("/issue_tracking/customers")
    Call<JsonElement> getCustomers(@Header("Authorization") String user_token);

    //users data
    @GET("/issue_tracking/users")
    Call<JsonElement> getUsers(@Header("Authorization") String user_token);

    //create issue
    @POST("/issue_tracking/issues")
    Call<JsonElement> createIssue(@Header("Authorization") String user_token, @Body Issue issue);

    //Edit issue
    @POST("/issue_tracking/issues/{issue_id}")
    Call<JsonElement> editIssue(@Header("Authorization") String user_token, @Body Issue issue, @Path("issue_id") long user_id);

    //create user
    @POST("/issue_tracking/users")
    Call<JsonElement> createUser(@Header("Authorization") String user_token, @Body User user);


}