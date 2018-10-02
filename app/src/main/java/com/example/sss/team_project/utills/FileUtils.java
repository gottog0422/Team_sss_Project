package com.example.sss.team_project.utills;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {
    public static Uri createCacheFile(Context _context) {
        Uri uri;
        String url = "tmp_" + String.valueOf(System.currentTimeMillis() + ".jpg");
        uri = Uri.fromFile(new File(_context.getExternalCacheDir(), url));
        return uri;
    }

    //특정 Uri 경로에 Bitmap 저장.
    public static File BitmapSaveToFileCach(Context context, Uri uri, Bitmap bitmap, int quality) {
        //Bitmap 을 지정된 uri로 저장 한다.
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(uri.getPath());
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
        return file;
    }
}
