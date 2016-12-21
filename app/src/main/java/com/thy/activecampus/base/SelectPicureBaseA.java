package com.thy.activecampus.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jin on 7/29.
 */

public class SelectPicureBaseA extends BaseA {

    public static final String[] items = {"相册", "拍照", "取消"};
    public static final int IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int RESIZE_REQUEST_CODE = 2;
    public static final String IMAGE_FILE_NAME = System.currentTimeMillis()+".jpg";
    public static Uri uri = null;

    public void selectPictrue() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    public void takePhoto() {
        if (isSdcardExisting()) {
            Intent cameraIntent = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "请插入sd卡", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public boolean isSdcardExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void resizeImage(Uri u) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(u, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    public Uri getImageUri() {

        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_NAME));

        return uri;
    }

    public void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            Drawable drawable = new BitmapDrawable(photo);
            uri = saveBitmap(photo);
        }
    }


    /**
     * 将得到的一个Bitmap保存到SD卡上，得到一个URI地址
     * @param bmp
     * @return
     */
    private Uri saveBitmap(Bitmap bmp){
        //在SD卡上创建目录
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.thy.activeCompus");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }

        File img = new File(tmpDir.getAbsolutePath() + System.currentTimeMillis()+".png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
