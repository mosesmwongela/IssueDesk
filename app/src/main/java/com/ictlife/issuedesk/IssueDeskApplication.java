package com.ictlife.issuedesk;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.ictlife.issuedesk.rest.ApiManager;


public class IssueDeskApplication extends Application {
    private static IssueDeskApplication instance;
    private static Context appContext;
    public static ApiManager apiManager;

    public static IssueDeskApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        apiManager = ApiManager.getInstance();
    }
}