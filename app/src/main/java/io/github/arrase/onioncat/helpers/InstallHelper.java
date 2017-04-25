package io.github.arrase.onioncat.helpers;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InstallHelper {

    public static void copyAssets(Context context, File appBin) {
        AssetManager assetManager = context.getAssets();
        String[] files;

        try {
            files = assetManager.list("");

            for (String filename : files) {
                InputStream in;
                OutputStream out;

                try {
                    in = assetManager.open(filename);
                    out = new FileOutputStream(new File(appBin.getAbsolutePath() + filename));

                    copyFile(in, out);

                    in.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
