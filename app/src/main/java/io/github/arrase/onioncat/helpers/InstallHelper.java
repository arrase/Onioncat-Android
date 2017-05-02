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

    public static void copyAsset(Context context, String name) {
        InputStream in;
        OutputStream out;

        File appBin = context.getDir(OcatConstant.BINARY_DIRECTORY, Application.MODE_PRIVATE);
        File asset = new File(appBin.getAbsolutePath() + "/" + name);

        if (asset.exists()) return;

        AssetManager assetManager = context.getAssets();

        try {
            in = assetManager.open(name);
            out = new FileOutputStream(asset);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            asset.setExecutable(true, false);
            asset.setReadable(true, false);

        } catch (IOException e) {
            Log.e("OCAT", "Failed to copy: " + name, e);
            e.printStackTrace();
        }

    }
}
