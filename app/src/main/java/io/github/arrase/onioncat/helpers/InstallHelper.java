package io.github.arrase.onioncat.helpers;


import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.arrase.onioncat.constants.OcatConstant;

public class InstallHelper {

    public static void copyAssets(Context context) {
        InputStream in;
        OutputStream out;

        File appBin = context.getDir(OcatConstant.BINARY_DIRECTORY, Application.MODE_PRIVATE);
        File ocat_bin = new File(appBin.getAbsolutePath() + "/ocat");

        if (ocat_bin.exists()) return;

        AssetManager assetManager = context.getAssets();

        try {
            in = assetManager.open("ocat");
            out = new FileOutputStream(ocat_bin);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            ocat_bin.setExecutable(true, false);
            ocat_bin.setReadable(true, false);

        } catch (IOException e) {
            Log.e("OCAT", "Failed to copy ocat bin", e);
            e.printStackTrace();
        }

    }
}
