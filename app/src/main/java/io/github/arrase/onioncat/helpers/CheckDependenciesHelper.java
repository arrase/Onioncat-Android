package io.github.arrase.onioncat.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import io.github.arrase.onioncat.R;

public class CheckDependenciesHelper {
    public static boolean checkAll(Context context) {
        return checkOrbot(context) && checkServer(context);
    }

    public static boolean checkOrbot(Context context) {
        return OrbotHelper.isOrbotInstalled(context);
    }


    public static boolean checkServer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return (preferences.getString(context.getString(R.string.pref_server_onion), null) != null);
    }
}
