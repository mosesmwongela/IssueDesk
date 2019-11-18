package com.ictlife.issuedesk.rest;


import com.google.gson.JsonElement;
import com.ictlife.issuedesk.ui.create.issue.Issue;
import com.ictlife.issuedesk.ui.create.user.User;

import java.util.Map;

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

    //getIssues
    public void getIssues(String user_token, Map<String, String> params, Callback<JsonElement> callback) {
        Call<JsonElement> apiCall = apiService.getIssues(user_token, params);
        apiCall.enqueue(callback);
    }

    //get customers
    public void getCustomers(String user_token, Callback<JsonElement> callback) {
        Call<JsonElement> apiCall = apiService.getCustomers(user_token);
        apiCall.enqueue(callback);
    }

    //get users
    public void getUsers(String user_token, Callback<JsonElement> callback) {
        Call<JsonElement> apiCall = apiService.getUsers(user_token);
        apiCall.enqueue(callback);
    }

    //create_issue
    public void createIssue(String user_token, Issue issue, Callback<JsonElement> callback) {
        Call<JsonElement> apiCall = apiService.createIssue(user_token, issue);
        apiCall.enqueue(callback);
    }

    //edit issue
    public void editIssue(String user_token, Issue issue, int issue_id, Callback<JsonElement> callback) {
        Call<JsonElement> apiCall = apiService.editIssue(user_token, issue, issue_id);
        apiCall.enqueue(callback);
    }

    //create user
    public void createUser(String user_token, User user, Callback<JsonElement> callback) {
        Call<JsonElement> apiCall = apiService.createUser(user_token, user);
        apiCall.enqueue(callback);
    }

}