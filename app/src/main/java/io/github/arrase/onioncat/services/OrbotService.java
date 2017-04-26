package io.github.arrase.onioncat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import info.guardianproject.netcipher.proxy.OrbotHelper;
import info.guardianproject.netcipher.proxy.StatusCallback;
import io.github.arrase.onioncat.constants.OcatConstant;

public class OrbotService extends Service {
    private final String TAG = "OrbotService";

    private OrbotHelper orbotHelper;
    private int mStartId;
    private Intent mStartIntent;

    private StatusCallback statusCallback = new StatusCallback() {
        private boolean waitPolipo = false; // Ugly fix

        @Override
        public void onEnabled(Intent intent) {
            Log.d(TAG, "onEnabled");

            if (waitPolipo) { // Ugly fix
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitPolipo = false;
            }

            switch (mStartIntent.getAction()) {
                case OcatConstant.START_OCAT:
                    Intent ocat = new Intent(getApplicationContext(), OcatService.class);
                    ocat.setAction(OcatConstant.START_OCAT);
                    getApplicationContext().startService(ocat);
                    break;
                case OcatConstant.START_ORBOT:
                    LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(OrbotService.this);
                    broadcaster.sendBroadcast(new Intent(OcatConstant.START_ORBOT_END));
                    break;
            }

            orbotHelper.removeStatusCallback(statusCallback);
            OrbotService.this.stopSelf(mStartId);
        }

        @Override
        public void onStarting() {
            Log.d(TAG, "onStarting");
            waitPolipo = true; // Ugly fix
        }

        @Override
        public void onStopping() {
            Log.d(TAG, "onStopping");
        }

        @Override
        public void onDisabled() {
            Log.d(TAG, "onDisabled");
        }

        @Override
        public void onStatusTimeout() {
            Log.d(TAG, "onStatusTimeout");
            orbotHelper.removeStatusCallback(statusCallback);
            OrbotService.this.stopSelf(mStartId);
        }

        @Override
        public void onNotYetInstalled() {
            Log.d(TAG, "onNotYetInstalled");
            orbotHelper.removeStatusCallback(statusCallback);
            OrbotService.this.stopSelf(mStartId);
        }
    };

    public OrbotService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        orbotHelper = OrbotHelper.get(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        mStartId = startId;
        mStartIntent = intent;
        orbotHelper.addStatusCallback(statusCallback);
        orbotHelper.init();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
