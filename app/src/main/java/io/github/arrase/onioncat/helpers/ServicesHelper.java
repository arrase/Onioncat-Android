package io.github.arrase.onioncat.helpers;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import io.github.arrase.onioncat.R;
import io.github.arrase.onioncat.constants.OcatConstant;

public class ServicesHelper {
    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopOcat(Context context) {
        final Context mContext = context;

        new Thread(new Runnable() {
            public void run() {
                Process p;

                File appBinHome = mContext.getDir(OcatConstant.BINARY_DIRECTORY, Application.MODE_PRIVATE);

                try {
                    p = Runtime.getRuntime().exec("su");

                    DataOutputStream os = new DataOutputStream(p.getOutputStream());
                    os.writeBytes("/system/bin/kill $(cat " + appBinHome.getAbsolutePath() + "/ocat.pid)\n");
                    os.writeBytes("exit\n");
                    os.flush();

                    p.waitFor();

                } catch (IOException | InterruptedException e) {
                    // Silent block
                }
            }
        }).start();
    }

    public static String getOcatIPv6(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String onion = preferences.getString(context.getString(R.string.pref_server_onion), null);
        if (onion == null) return null;

        List<String> commandAndParameters = Arrays.asList("./ocat", "-i", onion);

        File dir = context.getDir(OcatConstant.BINARY_DIRECTORY, Application.MODE_PRIVATE);

        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.command(commandAndParameters);
        builder.directory(dir);

        try {
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }

    }
}
