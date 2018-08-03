package com.aicecloud.cardcamera.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by static on 2017/12/18/018.
 */

public class ImageVpShowUtil {
    public static void savePicture(final Context context, final String imageFilesName, final String imageName, String url){
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        saveImageToSD(context,resource,imageFilesName,imageName);
                    }
                });
    }
    public static void saveImageToSD(Context context, Bitmap bmp, String imageFilesName, String imageName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            try {
                // 首先保存图片
                String rootFilePath = Environment.getExternalStorageDirectory().getCanonicalPath();
                File file = new File(rootFilePath);
                File appDir = new File(file ,imageFilesName);
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }
                imageName = imageName +System.currentTimeMillis()+".jpg";
                File currentFile = new File(appDir, imageName);
                fos = new FileOutputStream(currentFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                Toast.makeText(context, "图片已成功保存至"+currentFile.getPath(), Toast.LENGTH_SHORT).show();
                // 通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(new File(currentFile.getPath()))));
            } catch (Exception e) {
                Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
        }
    }
}
