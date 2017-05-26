package com.foxy_corporation.d_studiotestapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Vlad on 26.05.2017.
 */

public class Utils {

    private Utils() {}

    public static boolean isInternetAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
