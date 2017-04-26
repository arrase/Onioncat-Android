package io.github.arrase.onioncat.services;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import io.github.arrase.onioncat.constants.OcatConstant;

public class OcatServiceStop extends Service {
    public OcatServiceStop() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int id = startId;

        if (intent != null && intent.getAction().equals(OcatConstant.STOP_OCAT)) {

            new Thread(new Runnable() {
                public void run() {
                    Process p;

                    File appBinHome = getDir(OcatConstant.BINARY_DIRECTORY, Application.MODE_PRIVATE);

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
}
