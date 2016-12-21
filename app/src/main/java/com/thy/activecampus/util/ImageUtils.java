package com.thy.activecampus.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jin on 7/29.
 */

public class ImageUtils {

    public static String userheadPath = null;


    public static File getOutputMediaFile(String directory) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                directory);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG LOAD IMAGE", "Oops! Failed create "
                        + directory + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public static void saveBitmap(Bitmap bmp) {

        //在SD卡上创建目录
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.thy.activeCompus");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        userheadPath = tmpDir.getAbsolutePath()+"userhead.png";
        File img = new File(userheadPath);
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static String getUserheadPath(){
        return userheadPath;
    }
}
