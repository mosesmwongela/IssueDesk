package com.ictlife.issuedesk.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "issuedesk";

    private static final String IS_LOGGED_IN = "IsUserLoggedIn";

    private static final String USER_TOKEN = "token";
    private static final String USER_FULL_NAME = "full_name";
    private static final String USER_EMAIL = "user_email";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserToken(String token) {
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }

    public String getUserToken() {
        return pref.getString(USER_TOKEN, null);
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, null);
    }

    public void setUserFullName(String fullName) {
        editor.putString(USER_FULL_NAME, fullName);
        editor.commit();
    }

    public String getUserFullName() {
        return pref.getString(USER_FULL_NAME, null);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

}
