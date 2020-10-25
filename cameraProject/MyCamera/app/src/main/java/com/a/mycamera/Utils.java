package com.a.mycamera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.TextureView;

import com.a.mycamera.view.AutoFitTextureView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.opengles.GL10;

import retrofit2.http.PUT;

/**
 * create by 72088385
 * on 2020/9/16
 */

public class Utils {

    public static final String REVIEW_ACTION = "com.vivo.gallery.ACTION_VIEW";

    public static void gotoGallery(Activity activity, Uri mUri) {
        if (mUri == null) {
            return;
        }
        Bundle bundle = new Bundle();

        try {
            Intent intent = new Intent(REVIEW_ACTION, mUri);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.d("dd_ActivityNotFoundException", "" + e);
            }
        }
    }

    public static boolean isGrid = false;

    public static int defaultWidth = 720;

    public static int defaultHeight = 720 * 4 / 3;

    public static int curW = 720;

    public static int curH = 720 * 4 / 3;

    public static Uri curUri;

    public static int curLayoutIndex = 0;

    public static boolean focusFlag = true;

    private static final String rootPath = "/storage/emulated/0/DCIM/Camera/";

    public static String videoPath = "";

    public static boolean isFilter=false;

    /**
     * 这个方法用来把已经存在的一个文件存储到相册
     * @param context 用来发送广播
     * @param srcPath 需要拷贝的文件的地址
     */
    public static void saveFileToAlbum(Context context, String srcPath) {
        if (TextUtils.isEmpty(srcPath)) {
            return;
        }
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return;
        }
        //如果root文件夹没有需要新建一个
        createDirIfNotExist();

        //拷贝文件到picture目录下
        File destFile = new File(rootPath + System.currentTimeMillis() + ".mp4");
        videoPath = destFile.getAbsolutePath();
        copyFile(srcFile, destFile);

        //将该文件扫描到相册
        MediaScannerConnection.scanFile(context, new String[]{destFile.getPath()}, null, null);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(destFile);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static void createDirIfNotExist() {
        File file = new File(rootPath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(File src, File dest) {
        if (!src.getAbsolutePath().equals(dest.getAbsolutePath())) {
            try {
                InputStream in = new FileInputStream(src);
                FileOutputStream out = new FileOutputStream(dest);
                byte[] buf = new byte[1024];

                int len;
                while ((len = in.read(buf)) >= 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int createOESTextureObject() {

        // 1.生成一个纹理，返回一个id
        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);

        // 2.把纹理绑定到GL_TEXTURE_EXTERNAL_OES上，可以想象成指针赋值
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0]);

        // 3.对GL_TEXTURE_EXTERNAL_OES设一堆参数，这些参数都设到我们上一步绑定的纹理上了。
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER,
            GL10.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER,
            GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S,
            GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T,
            GL10.GL_CLAMP_TO_EDGE);

        // 3.把0绑定到GL_TEXTURE_EXTERNAL_OES，解绑了我们的纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0];
    }

    public static String readShaderFromResource(Context context, int resourceId) {
        StringBuilder builder = new StringBuilder();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = context.getResources().openRawResource(resourceId);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    @SuppressLint("StaticFieldLeak")
    public static GLRender mRenderer =null;

    public static boolean isOnce = true;

    public static SurfaceTexture getSurfaceTexture(AutoFitTextureView textureView, Context context,
        boolean... isFilter) {
        if (isFilter!=null&&isFilter.length!=0&&isFilter[0]) {
            if (isOnce) {
                isOnce = false;
                if(mRenderer==null){
                    mRenderer=new GLRender();
                }
                mRenderer.initRenderData();
            }
            mRenderer.init(textureView, context);
            return mRenderer.getOESTexture();
        }
            return textureView.getSurfaceTexture();

        //        if (isOnce) {
        //            isOnce = false;
        //            mRenderer.initRenderData();
        //        }
        //        mRenderer.init(textureView, context);
        //        return  textureView.getSurfaceTexture();
        //        return mRenderer.getOESTexture();
    }

    //    public static Context mContext;
    //
    //    public static void  setContextValue(Context context){
    //        mContext=context;
    //    }
    //
    //    public  static  SurfaceTexture surfaceTexture;

}
