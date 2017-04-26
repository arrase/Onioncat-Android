package io.github.arrase.onioncat.helpers;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

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
}
