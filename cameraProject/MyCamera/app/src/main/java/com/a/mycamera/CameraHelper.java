package com.a.mycamera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.a.mycamera.view.AutoFitTextureView;
import com.a.mycamera.view.AwbSeekBar;
import com.a.mycamera.view.OverlayView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.a.mycamera.CameraActivity.NOW_MODE;

/**
 * create by 72088385
 * on 2020/9/11
 */
public class CameraHelper implements ICamera2, AwbSeekBar.OnAwbSeekBarChangeListener {

    private static final String TAG = "CameraHelper";

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    Size mRatioSize = new Size(4, 3);

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private Matrix mFaceDetectionMatrix;

    private int mRightArg = 0;

    private boolean mSwappedDimensions;

    /**
     * 淡入 淡出 动画
     */
    private final Animation mAlphaInAnimation;

    private final Animation mAlphaOutAnimation;

    /**
     * 设备旋转方向
     */
    private int mDeviceRotation;

    private int mPhotoRotation;

    /**
     * 光强
     */
    private float mLight;

    private AtomicBoolean mIsCameraOpen;

    private CameraManager mCameraManager;

    private TakePhotoListener mTakePhotoListener;

    private CameraReady mCameraReady;

    /**
     * 摄像头的id集合
     */
    private String[] mCameraIds;

    /**
     * 预览尺寸，后台提供的尺寸
     */
    public Size mPreviewSize;

    /**
     * 可缩放区域
     */
    private Size mZoomSize;

    private Size mVideoSize;

    private Context mContext;

    /**
     * 相机曝光范围
     */
    private Range<Integer> range1;

    /**
     * 需要打开的摄像头id
     */
    private String mCameraId;

    private MediaRecorder mMediaRecorder;

    private CaptureRequest.Builder mPreviewBuilder;

    private CameraDevice mCameraDevice;

    private CameraCaptureSession mPreviewSession;

    private AutoFitTextureView mTextureView;

    private Rect activeArraySizeRect;

    private OverlayView mOverlayView;

    private Activity mActivity;

    /**
     * 后台线程
     */
    private HandlerThread mBackgroundThread;

    /**
     * Render的后台线程
     */
    //    public HandlerThread mRenderHandlerThread;

    /**
     * 后台handle
     */
    private Handler mBackgroundHandler;

    /**
     * Render的后台handle
     */
    //    private Handler mRenderHandler;

    private AtomicBoolean mIsRecordVideo = new AtomicBoolean();

    private CameraType mNowCameraType;

    /**
     * 拍照保存图片
     */
    private ImageReader mImageReader;

    /**
     * 是否支持闪光灯
     */
    private boolean mFlashSupported;

    /**
     * 图片的路径
     */
    private String mPhotoPath;

    /**
     * 相机传感器的方向
     */
    private int mSensorOrientation;

    /**
     * 最大的放大倍数
     */
    private float mMaxZoom = 0;

    /**
     * 放大的矩阵，拍照使用
     */
    private Rect mZoomRect;

    /**
     * 摄像头支持的分辨率流集合
     */
    private StreamConfigurationMap mMap;

    private FlashState mNowFlashState = FlashState.CLOSE;

    private boolean mIsCapture = false;

    private CameraCharacteristics mCharacteristics;

    private boolean mNoAFRun = false;

    private boolean mIsAFRequest = false;

    private CameraMode CAMERA_STATE = CameraMode.TAKE_PHOTO;

    private Surface mPreViewSurface;

    private Surface mRecordSurface;

    private Rect mFocusRect;

    private int rotation = 0;

    /**
     * 根据摄像头管理器获取一个帮助类
     */
    public CameraHelper(Context context, int rotation) {
        this.mContext = context;
        this.rotation = rotation;
        mIsCameraOpen = new AtomicBoolean(false);
        CameraManager cameraManager = (CameraManager) context.getSystemService(
            Context.CAMERA_SERVICE);
        mCameraManager = cameraManager;
        try {
            mCameraIds = mCameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mFocusRect = new Rect();

        mAlphaInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.alpha_in);
        mAlphaOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.alpha_out);
    }

    private TextView mTextView;

    /**
     * 状态文字 赋值
     * @param mTextView tv
     */
    public void setShowTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }

    @Override
    public void cameraZoom(float scale) {
        if (scale <= 1.0f) {
            updatePreview();
            return;
        }
        // 4.0
        Log.d("dd_cameraZoom——mMaxZoom", "" + mMaxZoom);
        //        1.0 -> 4.27
        Log.d("dd_cameraZoom——scale", "" + scale);

        if (scale <= mMaxZoom) {
            //  2.6 防止超出宽高的一半
            int cropW = (int) ((mZoomSize.getWidth() / (mMaxZoom * 2.6)) * scale);
            int cropH = (int) ((mZoomSize.getHeight() / (mMaxZoom * 2.6)) * scale);

            Rect zoom = new Rect(cropW, cropH,
                mZoomSize.getWidth() - cropW,
                mZoomSize.getHeight() - cropH);
            mPreviewBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);
            mZoomRect = zoom;
            updatePreview();
        }
    }

    /**
     * 获取最大zoom
     * @return 放大数值 4.0
     */
    public float getMaxZoom() {
        Log.d("dd_最大zoom", "" + mMaxZoom);
        return mMaxZoom;
    }


    @SuppressLint("MissingPermission")
    @Override
    public boolean openCamera(CameraType cameraType) {
        if (mIsCameraOpen.get()) {
            Log.d(TAG, "openCamera: mIsCameraOpen:" + mIsCameraOpen);
            return true;
        }
        mIsCameraOpen.set(true);
        mZoomRect = null;

        int cameraTypeId = setCameraTypeId(cameraType);

        try {
            for (String cameraId : mCameraIds) {
                mCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                Integer facing = mCharacteristics.get(CameraCharacteristics.LENS_FACING);
                // 曝光增益 范围
                range1 = mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
                //获取曝光时间 范围
                etr = mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);

                if (facing != null && facing != cameraTypeId) {
                    continue;
                }

                // 获取最大 放大倍数
                Float maxZoom = mCharacteristics.get(
                    CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
                if (maxZoom != null) {
                    mMaxZoom = maxZoom;
                }

                //获取摄像头支持的流配置信息
                mMap = mCharacteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (mMap == null) {
                    Log.d(TAG, "openCamera: mMap:" + mMap);
                    return false;
                }

                Log.d("dd_xxxx", "" + mTextureView.getWidth());
                Log.d(TAG, "mRatioSize: " + mRatioSize);
                Log.d(TAG, "Utils.defaultWidth:" + Utils.defaultWidth);
                mPreviewSize = new Size(
                    Utils.defaultWidth * mRatioSize.getWidth() / mRatioSize.getHeight(),
                    Utils.defaultWidth);

//                mTextureView.setAspectRatio(mPreviewSize.getHeight(),
//                    mPreviewSize.getWidth());

                Log.d("dd_xxxxmTextureView1", "" + mTextureView.getWidth()+","+mTextureView.getHeight());

                mTextureView.setAspectRatio(mPreviewSize.getHeight(),
                    mPreviewSize.getWidth());

                Log.d("dd_xxxxmPreviewSize", "" + mPreviewSize);
                Log.d("dd_xxxxmTextureView2", "" + mTextureView.getWidth()+","+mTextureView.getHeight());

                initImageReader();

                //获取摄像头角度
                mSensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

                // 人脸检测Size
                activeArraySizeRect = mCharacteristics.get(
                    CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

                mVideoSize = CameraUtils.chooseVideoSize(mMap.getOutputSizes(MediaRecorder
                    .class));

                //检查是否支持闪光灯
                Boolean available = mCharacteristics.get(
                    CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;

                this.mCameraId = cameraId;
            }
            Log.d(TAG, "mCameraManager>openCamera: ");
            mCameraManager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private int setCameraTypeId(CameraType cameraType) {
        this.mNowCameraType = cameraType;
        int cameraTypeId;
        switch (cameraType) {
            default:
            case BACK:
                cameraTypeId = CameraCharacteristics.LENS_FACING_BACK;
                break;
            case FRONT:
                cameraTypeId = CameraCharacteristics.LENS_FACING_FRONT;
                break;
            case USB:
                cameraTypeId = CameraCharacteristics.LENS_FACING_EXTERNAL;
                break;
        }
        return cameraTypeId;
    }

    /**
     * 初始化 ImageReader
     */
    private void initImageReader() {
        Size imageSize = new Size(
            mPreviewSize.getWidth() * mRatioSize.getWidth() / mRatioSize.getHeight(),
            mPreviewSize.getWidth());

        mZoomSize = Collections.max(Arrays.asList(mMap.getOutputSizes(ImageFormat.JPEG)),
            new CompareSizesByArea());
        if (mImageReader != null) {
            mImageReader.close();
        }
        mImageReader = ImageReader.newInstance(imageSize.getWidth(),
            imageSize.getHeight(), ImageFormat.JPEG, 2);
    }

    private void configFaceCheck(Integer facing) {
        Rect activeArraySizeRect = mCharacteristics.get(
            CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        assert activeArraySizeRect != null;
        mRightArg = activeArraySizeRect.right;
        mFaceDetectionMatrix = new Matrix();
        mFaceDetectionMatrix.setRotate(mSensorOrientation);
        float s1 = mPreviewSize.getWidth() / (float) activeArraySizeRect.width();
        float s2 = mPreviewSize.getHeight() / (float) activeArraySizeRect.height();
        mFaceDetectionMatrix.postScale(
            facing == CameraCharacteristics.LENS_FACING_FRONT ? -s1 : s1, s2);

        mSwappedDimensions = false;

        int displayRotation = mActivity.getWindowManager()
            .getDefaultDisplay()
            .getRotation();

        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                    mSwappedDimensions = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                    mSwappedDimensions = true;
                }
                break;
            default:
        }

        if (mSwappedDimensions) {
            mFaceDetectionMatrix.postTranslate(mPreviewSize.getHeight(),
                mPreviewSize.getWidth());
        } else {
            mFaceDetectionMatrix.postTranslate(mPreviewSize.getWidth(),
                mPreviewSize.getHeight());
        }
    }

    public Size configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return null;
        }
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            //            预览的中心移动到mTextureView的中心来
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            //            src：mTextureView   dst：mPreviewSize
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            //            取最大值保证：mTextureView 被填满
            float scale = Math.max(
                (float) viewHeight / mPreviewSize.getHeight(),
                (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
        return mPreviewSize;
    }

    @Override
    public void closeCamera() {
        Log.e("camera", "关闭摄像头");
        mIsCameraOpen.set(false);

        closePreviewSession();
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    @Override
    public boolean switchCamera(CameraType cameraType) {
        closeCamera();
        return openCamera(cameraType);
    }

    /**
     * 人脸坐标
     * @param result
     */
    private void showFace(CaptureResult result) {
        Integer mode = result.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
        Face[] faces = result.get(CaptureResult.STATISTICS_FACES);
        if (faces != null && mode != null) {
            final ArrayList<Rect> arrayList = new ArrayList<>();
            if (faces.length > 0) {
                for (int i = 0; i < faces.length; i++) {
                    if (faces[i].getScore() > 30) {

                        double scaleWidth = mTextureView.getHeight() * 1.0000f /
                            activeArraySizeRect.width();
                        double scaleHeight = mTextureView.getWidth() * 1.0000f /
                            activeArraySizeRect.height();

                        int left = (int) (faces[i].getBounds().left * scaleWidth);
                        int top = (int) (faces[i].getBounds().top * scaleHeight);
                        int right = (int) (faces[i].getBounds().right * scaleWidth);
                        int bottom = (int) (faces[i].getBounds().bottom * scaleHeight);

                        Rect rect = new Rect(mTextureView.getWidth() - bottom,
                            mTextureView.getHeight() - right, mTextureView.getWidth() - top,
                            mTextureView.getHeight() - left);
                        if (CameraActivity.mNowCameraType == ICamera2.CameraType.BACK) {
                            rect = new Rect(mTextureView.getWidth() - bottom, left,
                                mTextureView.getWidth() - top, right);
                        }
                        arrayList.add(rect);
                    }
                }

            }
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mOverlayView.setRect(arrayList);
                    mOverlayView.requestLayout();
                }
            });
        }
    }

    /**
     * 更新预览界面
     */
    public void updatePreview() {
        if (mCameraDevice == null || mPreviewBuilder == null) {
            return;
        }
        try {
            mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            //            人脸检测
            mPreviewBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE,
                CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL);

            mPreviewSession.setRepeatingRequest(
                mPreviewBuilder.build(),
                mCaptureCallback2,
                mBackgroundHandler
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resumePreview() {
        try {
            if (!mNoAFRun) {
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_IDLE);
            }
            if (!isLegacyLocked()) {
                //告诉相机锁定焦点
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_CANCEL);
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_IDLE);
            }
            mIsAFRequest = false;
            mCameraState = 0;
            mPreviewSession.capture(mPreviewBuilder.build(), null,
                mBackgroundHandler);
            updatePreview();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //    开始录像
    @Override
    public boolean startVideoRecord(String path, int mediaType) {
        if (mIsRecordVideo.get()) {
            new Throwable("video record is recording");
        }
        if (path == null) {
            new Throwable("path can not null");
        }
        if (mediaType != MediaRecorder.OutputFormat.MPEG_4) {
            new Throwable("this mediaType can not support");
        }
        if (!setVideoRecordParam(path)) {
            return false;
        }
        startRecordVideo();
        return true;
    }

    /**
     * 设置录像参数
     * @param path
     * @return
     */
    private boolean setVideoRecordParam(String path) {
        mMediaRecorder = new MediaRecorder();
        Log.d("dd_path5", "" + path);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(path);

        int bitRate = 10000000;
        mMediaRecorder.setVideoEncodingBitRate(bitRate);
        mMediaRecorder.setVideoFrameRate(15);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        mMediaRecorder.setAudioEncodingBitRate(8000);
        mMediaRecorder.setAudioChannels(1);
        mMediaRecorder.setAudioSamplingRate(8000);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        if (mNowCameraType == CameraType.BACK) {
            //后置摄像头图像要旋转90度
            mMediaRecorder.setOrientationHint(90);
        } else {
            //前置摄像头图像要旋转270度
            mMediaRecorder.setOrientationHint(270);
        }
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void stopVideoRecord() {
        if (mIsRecordVideo.get()) {
            mIsRecordVideo.set(false);
        } else {
            return;
        }
        mMediaRecorder.setOnErrorListener(null);
        mMediaRecorder.setOnInfoListener(null);
        //        mMediaRecorder.setPreviewDisplay(null);
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        //        mMediaRecorder.release();
    }

    //    开始拍照
    @Override
    public boolean takePhone(String path, MediaType mediaType) {
        this.mPhotoPath = path;
        setTakePhotoFlashMode(mPreviewBuilder);
        updatePreview();
        // lockFocus();

        if (!mNoAFRun) {
            if (mIsAFRequest) {
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_AUTO);
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, AFRegions);
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, AERegions);
            }
            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_START);
        }
        if (!isLegacyLocked()) {
            // 告诉相机锁定焦点
            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        }
        mCameraState = WAITING_LOCK;
        if (!mFlashSupported) {
            capturePhoto();
        } else {
            switch (mNowFlashState) {
                case CLOSE:
                    capturePhoto();
                    break;
                case OPEN:
                case AUTO:
                    mBackgroundHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mPreviewSession.capture(mPreviewBuilder.build(), mCaptureCallback,
                                    mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 800);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private boolean isLegacyLocked() {
        return mCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) ==
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
    }

    /**
     * 设置3A参数
     * @param builder
     */
    private void setup3AControlsLocked(CaptureRequest.Builder builder) {
        //        启用由相机设备运行的auto-magical 3A
        builder.set(CaptureRequest.CONTROL_MODE,
            CaptureRequest.CONTROL_MODE_AUTO);

        Float minFocusDist =
            mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);

        // 如果MINIMUM_FOCUS_DISTANCE为0，则镜头为固定焦点，我们需要跳过自动对焦
        mNoAFRun = (minFocusDist == null || minFocusDist == 0);

        if (!mNoAFRun) {
            // 如果有“连续图片”模式可用，请使用它，否则默认为“自动”
            if (contains(mCharacteristics.get(
                CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES),
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)) {
                builder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            } else {
                builder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_AUTO);
            }
        }

        //  如果有自动魔术闪光控制模式，请使用它，否则默认为“开”模式，此模式保证始终可用。
        if (mNowFlashState != FlashState.CLOSE) {
            if (contains(mCharacteristics.get(
                CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES),
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)) {
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            } else {
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON);
            }
        }
        //如果有auto-magical白平衡控制模式，请使用它。
        if (contains(mCharacteristics.get(
            CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES),
            CaptureRequest.CONTROL_AWB_MODE_AUTO)) {
            // 如果此设备支持AWB，则允许AWB自动运行
            builder.set(CaptureRequest.CONTROL_AWB_MODE,
                CaptureRequest.CONTROL_AWB_MODE_AUTO);
        }
    }

    /**
     * 真正拍照
     */
    private void capturePhoto() {
        mIsCapture = true;
        final CaptureRequest.Builder captureBuilder;
        try {
            //设置拍照后的回调监听
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
            captureBuilder = mCameraDevice.createCaptureRequest(
                CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            //设置自动对焦
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 使用与预览相同的AE和AF模式
      /*      if(mNowFlashState != FlashState.CLOSE) {
                if(mFlashSupported)
                    setup3AControlsLocked(captureBuilder);
            }*/

            mPhotoRotation = getOrientation(mDeviceRotation);
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, mPhotoRotation);

            //放大的矩阵
            if (mZoomRect != null) {
                captureBuilder.set(CaptureRequest.SCALER_CROP_REGION, mZoomRect);
            }
            setTakePhotoFlashMode(captureBuilder);
            captureBuilder.setTag(1);
            mPreviewSession.stopRepeating();
            mPreviewSession.abortCaptures();
            mBackgroundHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mPreviewSession.capture(captureBuilder.build(),
                            new CameraCaptureSession.CaptureCallback() {
                                @Override
                                public void onCaptureCompleted(CameraCaptureSession session,
                                    CaptureRequest request, TotalCaptureResult result) {

                                }

                                @Override
                                public void onCaptureFailed(
                                    CameraCaptureSession session, CaptureRequest request,
                                    CaptureFailure failure) {

                                }

                            }, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }, 200);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTextureView(AutoFitTextureView textureView) {
        this.mTextureView = textureView;
    }

    @Override
    public void setOverlayView(OverlayView overlayView) {
        this.mOverlayView = overlayView;
    }

    @Override
    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void setTakePhotoListener(TakePhotoListener mTakePhotoListener) {
        this.mTakePhotoListener = mTakePhotoListener;
    }

    @Override
    public void setCameraReady(CameraReady cameraReady) {
        this.mCameraReady = cameraReady;
    }

    @Override
    public void flashSwitchState(FlashState mFlashState) {
        mNowFlashState = mFlashState;
        if (CAMERA_STATE == CameraMode.TAKE_PHOTO) {
            setTakePhotoFlashMode(mPreviewBuilder);
            updatePreview();
        }
    }

    @Override
    public void setCameraState(CameraMode cameraMode) {
        CAMERA_STATE = cameraMode;
        if (CAMERA_STATE == CameraMode.TAKE_PHOTO) {
            setTakePhotoFlashMode(mPreviewBuilder);
            updatePreview();
        } else if (CAMERA_STATE == CameraMode.RECORD_VIDEO) {
            updatePreview();
        }
    }

    private MeteringRectangle[] AFRegions;

    private MeteringRectangle[] AERegions;

    @Override
    public void requestFocus(float x, float y) {
        mIsAFRequest = true;
        MeteringRectangle rect = new MeteringRectangle(mFocusRect,
            MeteringRectangle.METERING_WEIGHT_MAX);

        AFRegions = new MeteringRectangle[]{rect};
        AERegions = new MeteringRectangle[]{rect};
        //(x:0, y:0, w:0, h:0, wt:1000)
        Log.e("AFRegions", "AFRegions:" + AFRegions[0].toString());

        try {
            final CaptureRequest.Builder mFocusBuilder =
                mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mFocusBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            mFocusBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, AFRegions);
            mFocusBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, AERegions);
            //人脸检测
            mFocusBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE,
                CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL);

            if (mZoomRect != null) {
                mFocusBuilder.set(CaptureRequest.SCALER_CROP_REGION, mZoomRect);
            }

            mFocusBuilder.addTarget(mPreViewSurface);

            if (CAMERA_STATE == CameraMode.RECORD_VIDEO) {
                if (mRecordSurface != null) {
                    mFocusBuilder.addTarget(mRecordSurface);
                    setRecordVideoFlashMode(mFocusBuilder);
                }
            }

            mPreviewSession.setRepeatingRequest(mFocusBuilder.build(),
                null, mBackgroundHandler);

            mFocusBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                CameraMetadata.CONTROL_AF_TRIGGER_START);

            mPreviewSession.capture(mFocusBuilder.build(),
                null, mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启后台线程
     */
    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread(CameraHelper.class.getSimpleName());
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        Log.d(TAG, "开启后台线程");
        //        startRenderThread();
    }

    //    public void startRenderThread() {
    //        mRenderHandlerThread = new HandlerThread("RenderThread");
    //        mRenderHandlerThread.start();
    //        Utils.mRenderer.configHandlerThread(mRenderHandlerThread);
    //    }

    /**
     * 停止后台进程
     */
    public void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
        }
        try {
            if (mBackgroundThread != null) {
                mBackgroundThread.join();
            }
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //        stopRenderThread();
    }

    //    public void stopRenderThread() {
    //
    //        if (mRenderHandlerThread != null) {
    //            mRenderHandlerThread.quitSafely();
    //        }
    //        try {
    //            if (mRenderHandlerThread != null) {
    //                mRenderHandlerThread.join();
    //            }
    //            mRenderHandlerThread = null;
    //            Utils.mRenderer.mRenderHandler = null;
    //        } catch (InterruptedException e) {
    //            e.printStackTrace();
    //        }
    //
    //    }

    /**
     * 设置闪光灯模式
     * @param builder
     */
    private void setTakePhotoFlashMode(CaptureRequest.Builder builder) {
        if (!mFlashSupported || builder == null) {
            return;
        }
        switch (mNowFlashState) {
            case CLOSE:
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_OFF);
                break;
            case OPEN:
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                break;
            case AUTO:
                builder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                break;
            default:
                break;
        }
    }

    /**
     * 设置 录像闪光灯
     */
    private void setRecordVideoFlashMode(CaptureRequest.Builder builder) {
        if (!mFlashSupported) {
            return;
        }
        switch (mNowFlashState) {
            case CLOSE:
                builder.set(CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_OFF);
                break;
            case OPEN:
                builder.set(CaptureRequest.FLASH_MODE,
                    CaptureRequest.FLASH_MODE_TORCH);
                break;
            case AUTO:
                if (mLight < 10.0f) {
                    builder.set(CaptureRequest.FLASH_MODE,
                        CaptureRequest.FLASH_MODE_TORCH);
                }
                break;
            default:
        }
    }

    /**
     * 设置光线强度
     */
    public void setLight(float light) {
        this.mLight = light;
    }

    /**
     * 开始录像
     */
    private void startRecordVideo() {
        try {
            closePreviewSession();
            Log.d("dd_mLargest", "" + mPreviewSize);
            if (mCameraDevice == null) {
                return;
            }

            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

            setRecordVideoFlashMode(mPreviewBuilder);

            //            输出： 720 1560
            log("dd_mTextureView_startRecordVideo",
                "" + mTextureView.getWidth() + " " + mTextureView.getHeight());
            //4160x3120
            log("dd_mLargest_startRecordVideo", "" + mPreviewSize);

            //       配置滤镜的SurfaceTexture
            SurfaceTexture surfaceTexture = Utils.getSurfaceTexture(mTextureView,
                mTextureView.getContext(),Utils.isFilter);

            //            无滤镜写法
            //            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            assert surfaceTexture != null;
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            mPreViewSurface = new Surface(surfaceTexture);

            mPreviewBuilder.addTarget(mPreViewSurface);

            mRecordSurface = mMediaRecorder.getSurface();

            mPreviewBuilder.addTarget(mRecordSurface);
            List<Surface> surfaceList = new ArrayList<>();
            surfaceList.add(mPreViewSurface);
            surfaceList.add(mRecordSurface);
            if (mZoomRect != null) {
                //放大的矩阵
                mPreviewBuilder.set(CaptureRequest.SCALER_CROP_REGION, mZoomRect);
            }
            mCameraDevice.createCaptureSession(surfaceList,
                new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(CameraCaptureSession session) {
                        mPreviewSession = session;
                        updatePreview();
                        mIsRecordVideo.set(true);
                        mMediaRecorder.start();
                    }

                    @Override
                    public void onConfigureFailed(CameraCaptureSession session) {

                    }
                }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * 设置当前相机位置
     * @param rotation 角度
     */
    public void setDeviceRotation(int rotation) {
        this.mDeviceRotation = rotation;
    }

    /**
     * seekBar 滑动监听事件
     */
    @Override
    public void doInProgress1() {
        mTextView.setText("自动");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_AUTO);
        updatePreview();
    }

    @Override
    public void doInProgress2() {
        mTextView.setText("多云");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
            CameraMetadata.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT);
        updatePreview();
    }

    @Override
    public void doInProgress3() {
        mTextView.setText("白天");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
            CameraMetadata.CONTROL_AWB_MODE_DAYLIGHT);
        updatePreview();
    }

    @Override
    public void doInProgress4() {
        mTextView.setText("日光灯");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
            CameraMetadata.CONTROL_AWB_MODE_FLUORESCENT);
        updatePreview();
    }

    @Override
    public void doInProgress5() {
        mTextView.setText("白炽灯");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
            CameraMetadata.CONTROL_AWB_MODE_INCANDESCENT);
        updatePreview();
    }

    @Override
    public void doInProgress6() {
        mTextView.setText("阴影");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_SHADE);
        updatePreview();
    }

    @Override
    public void doInProgress7() {
        mTextView.setText("黄昏");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
            CameraMetadata.CONTROL_AWB_MODE_TWILIGHT);
        updatePreview();
    }

    @Override
    public void doInProgress8() {
        mTextView.setText("暖光");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
            CameraMetadata.CONTROL_AWB_MODE_WARM_FLUORESCENT);
        updatePreview();
    }

    @Override
    public void onStopTrackingTouch(int num) {
        switch (num) {
            case 0:
                mTextView.setText("自动");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_AUTO);
                break;
            case 10:
                mTextView.setText("多云");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_CLOUDY_DAYLIGHT);
                break;
            case 20:
                mTextView.setText("白天");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_DAYLIGHT);
                break;
            case 30:
                mTextView.setText("日光灯");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_FLUORESCENT);
                break;
            case 40:
                mTextView.setText("白炽灯");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_INCANDESCENT);
                break;
            case 50:
                mTextView.setText("阴影");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_SHADE);
                break;
            case 60:
                mTextView.setText("黄昏");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_TWILIGHT);
                break;
            case 70:
                mTextView.setText("暖光");
                mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_WARM_FLUORESCENT);
                break;
            default:
        }
        updatePreview();
        mTextView.startAnimation(mAlphaOutAnimation);
        mTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.startAnimation(mAlphaInAnimation);
    }

    /**
     * 异步保存照片
     */
    private class PhotoSaver implements Runnable {

        /**
         * 图片文件
         */
        private File mFile;

        /**
         * 拍照的图片
         */
        private Image mImage;

        public PhotoSaver(Image image, File file) {
            this.mImage = image;
            this.mFile = file;
        }

        @Override
        public void run() {

            ByteBuffer byteBuffer = mImage.getPlanes()[0].getBuffer();
            byte[] buffer = new byte[byteBuffer.remaining()];
            byteBuffer.get(buffer);
            OutputStream outputStream = null;
            try {
                //                写入相册
                Uri uri = mContext.getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                Utils.curUri = uri;
                Log.d("dd_PhotoSaver-Uri", "" + uri);
                assert uri != null;
                outputStream = mContext.getContentResolver().openOutputStream(uri);
                assert outputStream != null;
                outputStream.write(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                byteBuffer.clear();
                //                关闭相册输入流
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                resumePreview();
                if (mTakePhotoListener != null) {
                    mTakePhotoListener.onTakePhotoFinish(mFile, mPhotoRotation, 0, 0);
                }
            }

        }
    }

    private static final int WAITING_LOCK = 1;

    private int mCameraState = 0;

    private CameraCaptureSession.CaptureCallback mCaptureCallback
        = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
            TotalCaptureResult result) {
            //            showFace(result);
            process(result);
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request,
            CaptureResult partialResult) {
            //            showFace(partialResult);
            process(partialResult);
        }

        private void process(CaptureResult result) {
            switch (mCameraState) {
                case WAITING_LOCK:
                    boolean readyToCapture = true;
                    if (!mNoAFRun) {
                        Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                        if (afState == null) {
                            break;
                        }

                        // 如果自动对焦已达到锁定状态，我们准备好进行捕捉
                        readyToCapture =
                            (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED ||
                                afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED);
                    }

                    // 如果我们在非旧版设备上运行，则还应该等到自动曝光和自动白平衡收敛后再拍照。
                    if (!isLegacyLocked()) {
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        Integer awbState = result.get(CaptureResult.CONTROL_AWB_STATE);
                        if (aeState == null || awbState == null) {
                            break;
                        }

                        readyToCapture = readyToCapture &&
                            aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED &&
                            awbState == CaptureResult.CONTROL_AWB_STATE_CONVERGED;
                    }

                    // 如果我们尚未完成捕获前的序列，但达到了最大等待超时，那就太糟糕了！无论如何都开始捕获。
                    if (!readyToCapture) {
                        readyToCapture = true;
                    }

                    if (readyToCapture) {
                        // 为每个用户点击“图片”按钮捕获一次。
                        capturePhoto();
                        // 此后，相机将返回到预览的正常状态。
                        mCameraState = 0;
                    }
                default:
            }
        }
    };

    private CameraCaptureSession.CaptureCallback mCaptureCallback2
        = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request,
            TotalCaptureResult result) {
            showFace(result);
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request,
            CaptureResult partialResult) {
            showFace(partialResult);
        }
    };

    /**
     * 拍照的有效回调
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener =
        new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                if (mIsCapture) {
                    Image image = reader.acquireNextImage();
                    mBackgroundHandler.post(new PhotoSaver(image, new File(mPhotoPath)));
                    //                    new Thread(new PhotoSaver(image, new File(mPhotoPath)))
                    //                    .start();
                    mIsCapture = false;
                }
            }
        };

    /**
     * 打开摄像头状态回调
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.d(TAG, "onOpened: ");
            mCameraDevice = camera;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            Log.d(TAG, "onDisconnected:");
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.d(TAG, "onError: ");
            camera.close();
            mCameraDevice = null;
        }
    };

    /**
     * 创建预览会话
     * @return
     */
    @Override
    public boolean createCameraPreviewSession() {
        if (mBackgroundHandler == null) {
            System.out.println("mBackgroundHandler为空");
            return false;
        }
        try {
            //        配置滤镜SurfaceTexture
            Log.d(TAG, "createCameraPreviewSession: " + mTextureView.getWidth());
            Log.d(TAG, "mPreviewSize:" + mPreviewSize);
            Log.d(TAG,
                "currentThread:" + Thread.currentThread().getName());

//            TODO:mTextureView 为上一次的
//            log("mTextureView11", mTextureView.getWidth() + "," + mTextureView.getHeight());
//            log("mPreviewSize11", mPreviewSize.getWidth() + "," + mPreviewSize.getHeight());

//            mTextureView.setAspectRatio(mRatioSize.getHeight(), mRatioSize.getWidth());
//            mPreviewSize = new Size(
//                Utils.defaultWidth * mRatioSize.getWidth() / mRatioSize.getHeight(),
//                Utils.defaultWidth);

//            log("mTextureView22", mTextureView.getWidth() + "," + mTextureView.getHeight());
//            log("mPreviewSize22", mPreviewSize.getWidth() + "," + mPreviewSize.getHeight());


            SurfaceTexture surfaceTexture = Utils.getSurfaceTexture(mTextureView,
                mTextureView.getContext(),Utils.isFilter);
//                        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();

            assert surfaceTexture != null;
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            mPreViewSurface = new Surface(surfaceTexture);
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            mPreviewBuilder.addTarget(mPreViewSurface);

            if (mZoomRect != null) {
                mPreviewBuilder.set(CaptureRequest.SCALER_CROP_REGION, mZoomRect);    //放大的矩阵
            }

            mCameraDevice.createCaptureSession(Arrays.asList(mPreViewSurface,
                mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    mPreviewSession = session;
                    setup3AControlsLocked(mPreviewBuilder);
                    //预览重复请求
                    updatePreview();
                    if (mCameraReady != null) {
                        mCameraReady.onCameraReady();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    CameraUtils.showToast(mActivity, "Failed");
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 选择一个适合的预览尺寸，不然有一些机型不支持
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
        int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // 收集至少与预览Surface一样大的受支持分辨率
        List<Size> bigEnough = new ArrayList<>();
        // 收集小于预览Surface的支持的分辨率
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                    option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // 选择足够大的最小的。如果没有足够大的，则从不够大的中选最大的一个
        if (bigEnough.size() > 0) {
            System.out.println("dd_bigEnough");
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            System.out.println("dd_notBigEnough");
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            System.out.println("dd_choices[0]" + choices[0]);
            return choices[0];
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * 如果给定数组包含给定整数，则返回true。
     * @param modes 要检查的数组
     * @param mode 要获取的整数
     * @return 如果数组包含给定的整数，则为true，否则为false
     */
    private static boolean contains(int[] modes, int mode) {
        if (modes == null) {
            return false;
        }
        for (int i : modes) {
            if (i == mode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取视频路径
     * @return
     */
    public String getVideoFilePath() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM), "Camera");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(
            file.getPath() + File.separator + System.currentTimeMillis() + ".mp4");
        Log.d("dd_file1", "" + file1.getAbsolutePath());
        try {
            if (!file1.exists()) {
                boolean newFile = file1.createNewFile();
                if (newFile) {
                    return file1.getAbsolutePath();
                }
            } else {
                return file1.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("dd_getVideoFilePath", file1.getAbsolutePath());
        return String.valueOf(
            new File(mContext.getExternalFilesDir(null), System
                .currentTimeMillis() + ".mp4"));
    }

    /**
     * 获取照片路径
     * @return
     */
    public String getPhotoFilePath() {
        return String.valueOf(
            new File(mContext.getExternalFilesDir(null), System.currentTimeMillis() + ".jpg"));
    }

    public Range<Integer> getRange1() {
        return range1;
    }

    /**
     * 设置ae 属性
     */
    public void setAERegions(int ae) {
        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, ae);
        updatePreview();
    }

    /**
     * 曝光时间
     */
    private Range<Long> etr;

    public Range<Long> getEtr() {
        return etr;
    }

    /**
     * 设置ae 属性
     */
    public void setAeTime(long aeTime) {
        mPreviewBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, aeTime);
        updatePreview();
    }

    private void log(String key, String value) {
        CameraActivity.logMap.put(key, value);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : CameraActivity.logMap.entrySet()) {
            stringBuffer.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        mActivity.runOnUiThread(() -> {
            TextView log = mActivity.findViewById(R.id.log);
            log.setText(stringBuffer.toString());
        });
    }

}
