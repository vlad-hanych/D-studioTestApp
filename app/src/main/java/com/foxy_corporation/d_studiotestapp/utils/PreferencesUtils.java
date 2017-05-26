package com.foxy_corporation.d_studiotestapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vlad on 25.05.2017.
 */

public class PreferencesUtils {
    private static final String FILE_NAME = "prefs";

    private static final String IS_USER_LOGGED_ID = "is_logged_in";

    private static PreferencesUtils mInstance;

    private SharedPreferences mSharedPreferences;

    private PreferencesUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, 0);
    }

    public static PreferencesUtils getInstance(Context context) {
        if (mInstance == null)
            mInstance = new PreferencesUtils(context);

        return mInstance;
    }

    public void setIsUserLoggedIn(boolean isLogged) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putBoolean(IS_USER_LOGGED_ID, isLogged);

        editor.commit();
    }

    public boolean getIsUserLoggedIn() {
        return mSharedPreferences.getBoolean(IS_USER_LOGGED_ID, false);
    }

    public void setAccessToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(Constants.ACCESS_TOKEN, token);

        editor.commit();
    }

    public String getAccessToken() {
        return mSharedPreferences.getString(Constants.ACCESS_TOKEN, Constants.EMPTY_STRING);
    }

}
