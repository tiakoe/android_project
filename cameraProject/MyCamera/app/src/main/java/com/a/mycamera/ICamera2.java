package com.a.mycamera;

import android.app.Activity;
import android.view.TextureView;

import com.a.mycamera.view.AutoFitTextureView;
import com.a.mycamera.view.OverlayView;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import okhttp3.MediaType;

/**
 * 相机 接口类
 */

public interface ICamera2 {

    /**
     * 缩放
     * @param zoom 缩放比例
     */
    void cameraZoom(float zoom);

    /**
     * 打开摄像头
     */
    boolean openCamera(CameraType cameraType);

    /**
     * 关闭摄像头
     */
    void closeCamera();

    /**
     * 切换摄像头
     * @param cameraType 切换摄像头类型
     * @return boolean 是否切换成功
     */
    boolean switchCamera(CameraType cameraType);

    /**
     * 开启相机的预览模式
     */
    boolean createCameraPreviewSession();

    /**
     * 恢复预览
     */
    void resumePreview();

    /**
     * 开始视频的录制
     * @param path 存储路径
     * @param mediaType 文件类型
     */
    boolean startVideoRecord(String path, int mediaType);

    /**
     * 停止视频录制
     */
    void stopVideoRecord();

    /**
     * 拍照
     * @param path 存储路径
     * @param mediaType 文件类型
     */
    boolean takePhone(String path, MediaType mediaType);

    void setTextureView(AutoFitTextureView textureView);

    void setOverlayView(OverlayView overlayView);

    void setActivity(Activity activity);

    void setTakePhotoListener(TakePhotoListener mTakePhotoListener);

    void setCameraReady(CameraReady mCameraReady);

    void flashSwitchState(FlashState mFlashState);

    void setCameraState(CameraMode cameraMode);

    /**
     * 手动请求对焦
     * @param x
     * @param y
     */
    void requestFocus(float x, float y);

    /**
     * 摄像头模式类型
     */
    enum CameraMode {
        RECORD_VIDEO,
        TAKE_PHOTO
    }

    @IntDef(value = {DEFAULT_TYPEFACE, TAKE_PHOTO, RECORD_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    @interface Camera2Mode {

    }

    int DEFAULT_TYPEFACE = -1;

    int TAKE_PHOTO = 1;

    int RECORD_VIDEO = 2;

    /**
     * 当前只有mp4类型
     */
    enum MediaType {
        MP4,
        JPEG,
    }

    /**
     * 摄像头类型
     */
    enum CameraType {
        FRONT,
        BACK,
        USB
    }

    /**
     * 灯光状态
     */
    enum FlashState {
        CLOSE,
        AUTO,
        OPEN
    }

    interface TakePhotoListener {

        void onTakePhotoFinish(File file, int photoRotation, int width, int height);
    }

    interface CameraReady {

        void onCameraReady();
    }
}
