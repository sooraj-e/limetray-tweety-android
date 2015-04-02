package com.app.android.limetray;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by blackadmin on 2/4/15.
 */
public class Util {
    private Util() {
    }

    public static final boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (null != netInfo && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
