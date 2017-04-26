package io.github.arrase.onioncat.services;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import io.github.arrase.onioncat.R;
import io.github.arrase.onioncat.constants.OcatConstant;

public class OcatService extends Service {
    public OcatService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int id = startId;

        if (intent != null && intent.getAction().equals(OcatConstant.START_OCAT)) {

            final NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, getNotification());

            new Thread(new Runnable() {
                public void run() {
                    Process p;

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String onion = preferences.getString(getString(R.string.pref_server_onion), null);

                    File appBinHome = getDir(OcatConstant.BINARY_DIRECTORY, Application.MODE_PRIVATE);
                    String ocat_path = appBinHome.getAbsolutePath() + "/ocat";

                    if (onion != null) {
                        try {
                            p = Runtime.getRuntime().exec("su");

                            DataOutputStream os = new DataOutputStream(p.getOutputStream());
                            os.writeBytes(ocat_path + " -P " + ocat_path + ".pid -T /dev/tun -r -B " + onion + "\n");
                            os.writeBytes("exit\n");
                            os.flush();
                            p.waitFor();

                        } catch (IOException | InterruptedException e) {
                            // Silent block
                        }
                    }

                    mNotificationManager.cancel(id);
                    stopSelf(id);
                }
            }).start();
        }

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true)
                        .setContentTitle(getString(R.string.ocat_is_running));

        return mBuilder.build();
    }
}
