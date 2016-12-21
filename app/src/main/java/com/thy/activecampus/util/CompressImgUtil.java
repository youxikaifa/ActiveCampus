package com.thy.activecampus.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Jin on 9/28.
 */

public class CompressImgUtil {

    /**
     * 尺寸压缩
     * @param imgPath
     * @return
     */
    public Bitmap getBmp(String imgPath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        BitmapFactory.decodeFile(imgPath, newOpts); //计算图片的比例
        newOpts.inJustDecodeBounds = true;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath,newOpts);
    }


    /**
     * 质量压缩
     * @param image
     * @return
     */
    public Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 获取file对象
     * 将file存在内存中，再将file删除
     * @param
     */
    public String getSmallFilePath(String largeFilePath){
        String prefix = largeFilePath.substring(largeFilePath.lastIndexOf(".")+1);
        Bitmap bitmap = getBmp(largeFilePath);
        File file=new File("/storage/emulated/0/DCIM/Camera/"+Math.random()+"activecompusupload.jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            if (prefix.equals("png")){
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            }else if(prefix.equals("jpg") || prefix.equals("jpeg")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }else{
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, bos);
            }

            bos.flush();
            bos.close();
            Log.v("IMAGE","图片保存到文件夹下");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }
}
