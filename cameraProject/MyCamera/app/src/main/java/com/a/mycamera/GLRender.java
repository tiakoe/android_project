package com.a.mycamera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.TextureView;

import com.a.mycamera.view.AutoFitTextureView;

public class GLRender implements SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "GLRender";

    private static final int MSG_INIT = 1;
    private static final int MSG_RENDER = 2;
    private static final int MSG_DESTROY = 3;

    private Context mContext;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private AutoFitTextureView mPreviewView;
    private int mOESTextureId;
    private float[] transformMatrix = new float[16];

    private EGLManager mEGLManager;
    private FilterEngine mFilterEngine;
    private SurfaceTexture mOESSurfaceTexture;

    public void GLRender(AutoFitTextureView textureView){
        this.mPreviewView=textureView;
    }

    public void initRenderData(){
        mHandlerThread = new HandlerThread("Render-Thread");
        mHandlerThread.start();
    }

    public void closeRender(){
        if(mHandlerThread!=null){
            mHandlerThread.quitSafely();
            mHandlerThread=null;
        }
    }



    public void init(AutoFitTextureView previewView, Context context) {
        mContext = context;
        mPreviewView = previewView;
        initOESTexture();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_INIT:
                        initEGL();
                        return;
                    case MSG_RENDER:
                        drawFrame();
                        return;
                    case MSG_DESTROY:
                        return;
                    default:
                        return;
                }
            }
        };
        mHandler.sendEmptyMessage(MSG_INIT);
    }

    private void initEGL() {
        mEGLManager = new EGLManager(mPreviewView.getSurfaceTexture());
        mFilterEngine = new FilterEngine(mOESTextureId, mContext);
    }

    private void drawFrame() {
        long t1 = System.currentTimeMillis();
        Log.d(TAG, "currentThread2: "+Thread.currentThread().getName());
        mOESSurfaceTexture.updateTexImage();
        mOESSurfaceTexture.getTransformMatrix(transformMatrix);
        mEGLManager.eglMakeCurrent();
        GLES20.glViewport(0, 0, mPreviewView.getHeight(), mPreviewView.getWidth());
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1f, 1f, 0f, 0f);
        mFilterEngine.drawTexture(transformMatrix);
        mEGLManager.eglSwapBuffers();
        long t2 = System.currentTimeMillis();
        Log.i(TAG, "drawFrame: time = " + (t2 - t1));
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_RENDER);
        }
    }

    private void initOESTexture() {
        mOESTextureId = Utils.createOESTextureObject();
        mOESSurfaceTexture = new SurfaceTexture(mOESTextureId);
        mOESSurfaceTexture.setOnFrameAvailableListener(this);
    }

    public SurfaceTexture getOESTexture() {
        return mOESSurfaceTexture;
    }
}
