package com.foxy_corporation.d_studiotestapp.view;

import android.app.Application;
import android.content.Context;

/**
 * Created by Vlad on 26.05.2017.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
