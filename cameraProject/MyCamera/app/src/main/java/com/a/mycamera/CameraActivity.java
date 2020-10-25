package com.a.mycamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.a.mycamera.base.BaseActivity;
import com.a.mycamera.view.AutoFitTextureView;
import com.a.mycamera.view.AwbSeekBar;
import com.a.mycamera.view.CircleImageView;
import com.a.mycamera.view.CountDownView;
import com.a.mycamera.view.GridLineView;
import com.a.mycamera.view.HorizontalRecycleView;
import com.a.mycamera.view.OverlayView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * create by 72088385
 * on 2020/9/11
 */
public class CameraActivity extends BaseActivity implements
    ICamera2.TakePhotoListener,
    SensorEventListener, ICamera2.CameraReady,
    HorizontalRecycleView.OnSelectedPositionChangedListener {

    private static final String TAG = "MyCameraVideoActivity";

    /**
     * 当前的显示面板状态
     */
    public int TEXTURE_STATE = AppConstant.TEXTURE_PREVIEW_STATE;

    public static ConcurrentHashMap<String, String> logMap = new ConcurrentHashMap<>();

    @BindView(R.id.log)
    TextView log;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(value = R.id.logSwitch)
    Switch mLogSwitch;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(value = R.id.lineSwitch)
    Switch mLineSwitch;

    @BindView(value = R.id.count_timer)
    TextView mCountTimer;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.switch_ae)
    Switch switchAe;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.awbSwitch)
    Switch awbSwitch;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.filterSwitch)
    Switch filterSwitch;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.ae_seekBar)
    SeekBar mAESeekBar;

    private CountDownView countDownView;

    //-----

    @BindView(R.id.app_bar)
    LinearLayout mAppBarLinearLayout;

    @BindView(R.id.switch_flash)
    ImageView mSwitchFlash;

    @BindView(R.id.switch_hdr)
    TextView mSwitchHDR;

    @BindView(R.id.switch_color)
    ImageView mSwitchColor;

    @BindView(R.id.switch_ratio)
    TextView mSwitchRatio;

    @BindView(R.id.switch_setting)
    ImageView mSwitchSetting;

    @BindView(R.id.app_bar_ratio)
    LinearLayout mAppBarLinearLayoutRatio;

    @BindView(R.id.ratio_current)
    TextView mRatio_current;

    @BindView(R.id.ratio_4_3)
    TextView mRatio_4_3;

    @BindView(R.id.ratio_100)
    TextView mRatio_100;

    @BindView(R.id.ratio_16_9)
    TextView mRatio_16_9;

    @BindView(R.id.ratio_1_1)
    TextView mRatio_1_1;

    @BindView(R.id.camera_photo)
    ImageView mCameraPhoto;

    @BindView(R.id.textureView)
    AutoFitTextureView mTextureView;

    @BindView(R.id.overlay_view)
    OverlayView mOverlayView;

    @BindView(R.id.grid_view)
    GridLineView mGridLineView;

    @BindView(R.id.camera_record_seek_bar)
    SeekBar mCameraRecordSeekBar;

    @BindView(R.id.video_time)
    TextView mVideoTime;

    @BindView(R.id.camera_switch_lens)
    ImageView mSwitchLens;

    /**
     * 功能菜单
     */
    @BindView(R.id.camera_menu)
    HorizontalRecycleView mCameraRecycleViewMenu;

    @BindView(R.id.pick_video_or_photo)
    CircleImageView mCameraPick;

    @BindView(R.id.camera_start)
    ImageButton mCameraStart;

    @BindView(R.id.video_mine_play)
    ImageButton mVideoMinePlay;

    @BindView(R.id.video_seek_bar)
    SeekBar mVideoSeekBar;

    @BindView(R.id.video_seek_time)
    TextView mVideoSeekTime;

    /**
     * 焦点框
     */
    @BindView(R.id.camera_focus)
    ImageView mCameraFocus;

    /**
     * zoom 缩小
     */
    @BindView(R.id.camera_minus)
    ImageView mCameraMinus;

    /**
     * scale zoom 条
     */
    @BindView(R.id.camera_scale)
    SeekBar mCameraScale;

    /**
     * zoom 放大
     */
    @BindView(R.id.camera_add)
    ImageView mCameraAdd;

    @BindView(R.id.camera_scale_bar_layout)
    RelativeLayout mCameraScaleBarLayout;

    /**
     * 底部切换布局
     */
    @BindView(R.id.layout_bottom)
    RelativeLayout mLayoutBottom;

    /**
     * 白平衡调节
     */
    @BindView(R.id.awb_seekBar)
    AwbSeekBar mAWBSeekBar;

    /**
     * awb
     */
    @BindView(R.id.layout_awb)
    LinearLayout layoutAwb;

    @BindView(R.id.rl_camera)
    RelativeLayout rlCamera;

    /**
     * 提示文本
     */
    @BindView(R.id.tip_text)
    TextView mTipText;

    /**
     * 相机模式
     */
    private int MODE;

    /**
     * 视频保存路径
     */
    private String mVideoPath;

    /**
     * 拍照工具类
     */
    private CameraHelper cameraHelper;

    /**
     * 菜单适配器
     */
    private MyMenuAdapter mMenuAdapter;

    /**
     * 当前拍照模式
     */
    public static int NOW_MODE;

    /**
     * 触摸事件处理类
     */
    private CameraTouch mCameraTouch;

    /**
     * 放大缩小seekBar 是否可以隐藏
     */
    private boolean isCanHind;

    /**
     * 手动对焦 动画
     */
    private FocusAnimation mFocusAnimation;

    /**
     * 前 后 摄像头标识
     */
    public static ICamera2.CameraType mNowCameraType = ICamera2.CameraType.BACK;

    /**
     * 是否在 录制中
     */
    private boolean hasRecording = false;

    /**
     * 图片路径
     */
    private String mCameraPath;

    /**
     * 倒计时
     */
    private Disposable mDisposable;

    /**
     * 是否有拍照权限
     */
    private boolean isNoPermissionPause;

    /**
     * 定义文字动画
     */
    private AlphaAnimation mAlphaInAnimation;

    private AlphaAnimation mAlphaOutAnimation;

    SparseArray<Size> mRatioSizeArr = new SparseArray<>();

    private Runnable mImageFocusRunnable = new Runnable() {
        @Override
        public void run() {
            mCameraFocus.setVisibility(View.GONE);
        }
    };

    /**
     * 2s后隐藏
     */
    private Runnable SeekBarLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            mCameraScaleBarLayout.setVisibility(View.GONE);
        }
    };

    /**
     * 视频播放模式控件隐藏
     */
    private Runnable mHindViewRunnable = new Runnable() {
        @Override
        public void run() {
            hindPlayView();
        }
    };

    /**
     * 底部 布局集合
     */
    private List<View> mLayoutList = new LinkedList<>();

    /**
     * visible与invisible之间切换的动画
     */
    private TranslateAnimation mShowAction;

    public CameraActivity() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {

        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);

        Utils.defaultWidth = displaySize.x;
        Utils.defaultHeight = displaySize.y;
        Log.d(TAG, "initData: displaySize:" + displaySize);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        cameraHelper = new CameraHelper(this, rotation);
        cameraHelper.setTakePhotoListener(this);
        cameraHelper.setCameraReady(this);
        cameraHelper.setShowTextView(mTipText);

        //        设置AE、AWB滑动监听
        mAESeekBar.setOnSeekBarChangeListener(new CameraSeekBarListener());
        mAWBSeekBar.setmOnAwbSeekBarChangeListener(cameraHelper);
        //设置mTextureView触摸缩放监听
        mTextureView.setOnTouchListener(new TextureViewTouchListener());
        //        设置缩放监听
        mCameraScale.setOnSeekBarChangeListener(new ScaleSeekBarChangeListener());
        setOnSwitchListener();

        mRatioSizeArr.put(R.id.ratio_1_1, new Size(1, 1));
        mRatioSizeArr.put(R.id.ratio_4_3, new Size(4, 3));
        mRatioSizeArr.put(R.id.ratio_16_9, new Size(16, 9));

        countDownView = new CountDownView(4000, 1000, mCountTimer, "");

        // 将底部布局依次添加到列表中
        mLayoutList.clear();
        mLayoutList.add(mLayoutBottom);

        // 初始化切换动画
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(100);

        mFocusAnimation = new FocusAnimation();
        // 淡入动画
        mAlphaInAnimation = new AlphaAnimation(0.0f, 1.0f);
        mAlphaInAnimation.setDuration(500);
        // 淡出动画
        mAlphaOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mAlphaOutAnimation.setDuration(500);

        // 初始化拍照模式
        MODE = AppConstant.CAMERA_MODE;

        mCameraTouch = new CameraTouch();

        mLogSwitch.setChecked(false);

        Utils.isOnce = true;

    }

    private void initPath() {
        mCameraPath = cameraHelper.getPhotoFilePath();
        mVideoPath = cameraHelper.getVideoFilePath();
    }

    @Override
    protected void initView() {
        if (MODE == AppConstant.CAMERA_MODE) {
            initCameraMode();
        }

    }

    /**
     * 初始化拍照
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initCameraMode() {
        initRecycleViewMenu();
        registerSensor();
    }

    /**
     * 初始化摄像头
     */
    private void initCamera(ICamera2.CameraType cameraType) {
        Log.d(TAG, "initCamera: ");
        if (cameraHelper == null || !checkPermission()) {
            return;
        }

        cameraHelper.setTextureView(mTextureView);
        cameraHelper.setOverlayView(mOverlayView);
        cameraHelper.setActivity(this);

        boolean isSuccessOpen = cameraHelper.openCamera(cameraType);
        if (isSuccessOpen) {
            System.out.println("打开相机成功");
        } else {
            System.out.println("打开相机失败");
        }
    }

    private boolean checkPermission() {
        if (!isHasPermission()) {
            isNoPermissionPause = true;
            requestPermissions(
                new String[]{Manifest.permission.CAMERA,
                             Manifest.permission.READ_EXTERNAL_STORAGE,
                             Manifest.permission.WRITE_EXTERNAL_STORAGE,
                             Manifest.permission.RECORD_AUDIO},
                1);
            Log.d(TAG, "checkPermission: true");
            return false;
        }
        initPath();
        return true;
    }

    private boolean isHasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void initRecycleViewMenu() {
        List<String> menus = new ArrayList<>();
        menus.add("拍照");
        menus.add("录像");
        mMenuAdapter = new MyMenuAdapter(this, menus, mCameraRecycleViewMenu);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCameraRecycleViewMenu.setLayoutManager(linearLayoutManager);
        mCameraRecycleViewMenu.setAdapter(mMenuAdapter);
        mCameraRecycleViewMenu.setOnSelectedPositionChangedListener(this);
    }

    /**
     * switch监听
     */
    private void setOnSwitchListener() {

        mLineSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLineSwitch.isChecked()) {
                    Utils.isGrid = true;
                } else {
                    Utils.isGrid = false;
                }
                updateGridLine();
            }
        });

        switchAe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchAe.isChecked()) {
                    mAESeekBar.setVisibility(View.VISIBLE);
                } else {
                    mAESeekBar.setVisibility(View.GONE);
                }
            }
        });

        awbSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awbSwitch.isChecked()) {
                    layoutAwb.setVisibility(View.VISIBLE);
                } else {
                    layoutAwb.setVisibility(View.GONE);
                }
            }
        });

        filterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterSwitch.isChecked()) {
                    Utils.isFilter=true;
                } else {
                    Utils.isFilter=false;
                }
//                Utils.mRenderer.closeRender();
                Utils.mRenderer=null;
                cameraHelper.closeCamera();
                cameraHelper.openCamera(mNowCameraType);
            }
        });
    }

    //   当前的比例id
    static int mRatioId = R.id.ratio_4_3;

    /**
     * appBar 点击
     */
    @OnClick({R.id.switch_flash,
              R.id.switch_hdr,
              R.id.switch_color,
              R.id.switch_ratio,
              R.id.switch_setting})
    public void doSwitchAppBar(View view) {
        switch (view.getId()) {
            case R.id.switch_flash:
                Object o = mSwitchFlash.getTag();
                if (o == null || ((int) o) == 0) {
                    mSwitchFlash.setBackgroundResource(R.mipmap.flash_auto);
                    mSwitchFlash.setTag(1);
                    cameraHelper.flashSwitchState(ICamera2.FlashState.AUTO);
                } else if (((int) o) == 1) {
                    mSwitchFlash.setBackgroundResource(R.mipmap.flash_open);
                    mSwitchFlash.setTag(2);
                    cameraHelper.flashSwitchState(ICamera2.FlashState.OPEN);
                } else {
                    mSwitchFlash.setBackgroundResource(R.mipmap.flash_close);
                    mSwitchFlash.setTag(0);
                    cameraHelper.flashSwitchState(ICamera2.FlashState.CLOSE);
                }
                break;
            case R.id.switch_hdr:
                break;
            case R.id.switch_color:
                break;
            case R.id.switch_ratio:
                mAppBarLinearLayout.setVisibility(View.GONE);
                mAppBarLinearLayoutRatio.setVisibility(View.VISIBLE);
                TextView textView = findViewById(mRatioId);
                textView.setTextColor(getColor(R.color.orange));

                break;
            case R.id.switch_setting:
                break;
            default:
                break;
        }
    }

    /**
     * appBarRatio 点击
     */
    @OnClick({R.id.ratio_current, R.id.ratio_4_3, R.id.ratio_100, R.id.ratio_16_9, R.id.ratio_1_1})
    public void doSwitchRatio(View view) {
        TextView preTextView = findViewById(mRatioId);
        preTextView.setTextColor(getColor(R.color.white));

        TextView textView = (TextView) view;
        if (textView.getId() != R.id.ratio_current) {
            mRatioId = textView.getId();
        }
        mSwitchRatio.setText(textView.getText());

        TextView ratioCurrent = findViewById(R.id.ratio_current);
        ratioCurrent.setText(textView.getText());

        mAppBarLinearLayoutRatio.setVisibility(View.GONE);

        mAppBarLinearLayout.setVisibility(View.VISIBLE);

        updateScreenSize(view.getId());
        updateGridLine();

    }

    @OnClick({R.id.camera_switch_lens})
    public void doSwitchLens(View view) {
        if (mNowCameraType == ICamera2.CameraType.FRONT) {
            cameraHelper.switchCamera(ICamera2.CameraType.BACK);
            mNowCameraType = ICamera2.CameraType.BACK;
        } else {
            cameraHelper.switchCamera(ICamera2.CameraType.FRONT);
            mNowCameraType = ICamera2.CameraType.FRONT;
        }
        mCameraTouch.resetScale();
    }

    private Size mRatioSize = new Size(4, 3);

    private void updateScreenSize(int id) {
        if (id == R.id.ratio_current) {
            return;
        }
        Point displaySize = new Point();

        if (id == R.id.ratio_100) {
            getWindowManager().getDefaultDisplay().getSize(displaySize);
            mRatioSize = new Size(displaySize.y, displaySize.x);
            Log.d("dd_displaySize.x", "" + displaySize.x);
        } else {
            mRatioSize = mRatioSizeArr.get(id);
        }

        Log.d(TAG, "updateScreenSize: "+cameraHelper.mRatioSize);

        cameraHelper.mRatioSize = mRatioSize;

        cameraHelper.closeCamera();
        cameraHelper.openCamera(mNowCameraType);

        log("mRatioSize", mRatioSize + "");
        log("mPreviewSize", cameraHelper.mPreviewSize + "");
        log("mTextureView", mTextureView.getWidth() + "x" + mTextureView.getHeight());

    }

    /**
     * 传感器继承方法 重力发生改变
     * 根据重力方向 动态旋转拍照图片角度(暂时关闭该方法)
     * <p>
     * 使用以下方法
     * int rotation = getWindowManager().getDefaultDisplay().getRotation();
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float light = event.values[0];
            cameraHelper.setLight(light);
        }
    }

    /**
     * 注册陀螺仪传感器
     */
    private void registerSensor() {
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Sensor mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mSensor == null) {
            return;
        }
        mSensorManager.registerListener(this, mSensor, Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, mLightSensor, Sensor.TYPE_LIGHT);
    }

    /**
     * 当已注册传感器的精度发生变化时调用
     * @param sensor sensor
     * @param accuracy 传感器的新精度
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    File curFile = null;

    /**
     * 拍照完成回调
     * @param file 文件
     * @param photoRotation 角度
     * @param width 宽度
     * @param height 高度
     */
    @Override
    public void onTakePhotoFinish(final File file, int photoRotation, int width, int height) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                curFile = file;
                mCameraPick.setImageURI(Utils.curUri);
                mCameraPick.setVisibility(View.VISIBLE);
                mCameraPick.requestLayout();
            }
        });
    }

    /**
     * 相机准备完毕
     */
    @Override
    public void onCameraReady() {
        mCameraStart.setClickable(true);
    }

    /**
     * NavigationBar 点击
     */
    @Override
    public void selectedPositionChanged(int pos) {
        Log.e(TAG, "selectedPositionChanged: " + pos);
        switch (pos) {
            case 0: {
                setAppBarPhotoStatus();

                Size size;
                if (mRatioId == R.id.ratio_100) {
                    //            todo: 将appbar 设置透明
                    Point displaySize = new Point();
                    getWindowManager().getDefaultDisplay().getSize(displaySize);
                    size = new Size(displaySize.y, displaySize.x);
                } else {
                    size = mRatioSizeArr.get(mRatioId);
                }
                if (cameraHelper.mPreviewSize != null) {
                    mTextureView.setAspectRatio(size.getHeight(), size.getWidth());
                    cameraHelper.mRatioSize = size;
                }

                showLayout(0, false);

                mCountTimer.setVisibility(View.GONE);
                mAppBarLinearLayout.setVisibility(View.VISIBLE);
                mAppBarLinearLayoutRatio.setVisibility(View.GONE);

                NOW_MODE = AppConstant.CAMERA_TAKE_PHOTO;
                cameraHelper.setCameraState(ICamera2.CameraMode.TAKE_PHOTO);

                break;
            }
            case 1: {
                setAppBarVideoStatus();
                mTextureView.setAspectRatio(720, 1560);
                cameraHelper.mRatioSize = new Size(1560, 720);
                mRatioSize = new Size(1560, 720);

                showLayout(0, false);

                mCountTimer.setVisibility(View.VISIBLE);
                mAppBarLinearLayout.setVisibility(View.GONE);
                mAppBarLinearLayoutRatio.setVisibility(View.GONE);

                NOW_MODE = AppConstant.CAMERA_RECORD_MODE;
                resetVideoMode();
                break;
            }
            default:
        }
    }

    private void setAppBarVideoStatus() {
        Window window = getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //刘海屏适配
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            //设置底部导航栏透明
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 拍照
     */
    private void setAppBarPhotoStatus() {
        setAppBarVideoStatus();
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
        window.setAttributes(lp);
        final View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    /**
     * 录像时长倒计时
     */
    @SuppressLint("SetTextI18n")
    private void recordCountDown() {
        mVideoTime.setVisibility(View.VISIBLE);
        mCameraRecordSeekBar.setVisibility(View.VISIBLE);
        final int count = 10;
        mDisposable = Observable.interval(1, 1, TimeUnit.SECONDS)
            .take(count + 1)
            .map(new Function<Long, Long>() {
                @Override
                public Long apply(Long aLong) {
                    return count - aLong;
                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) {
                    long time = 11 - aLong;
                    if (time < 10) {
                        mVideoTime.setText("0:0" + time);
                    } else {
                        mVideoTime.setText("0:" + time);
                    }
                    mCameraRecordSeekBar.setProgress((int) time);
                    if (time == AppConstant.VIDEO_MAX_TIME) {
                        mVideoTime.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recordVideoOrTakePhoto();
                                hindVideoRecordSeekBar();
                            }
                        }, 300);
                    }
                }
            });
    }

    /**
     * 选择照片
     */
    @OnClick(R.id.pick_video_or_photo)
    public void pickVideoOrPhoto() {
        if (Utils.curUri != null) {
            Utils.gotoGallery(this, Utils.curUri);
        }
    }

    /**
     * 拍照或者录像
     */
    @OnClick(R.id.camera_start)
    public void recordVideoOrTakePhoto() {
        if (!isHasPermission()) {
            return;
        }

        //拍照
        if (NOW_MODE == AppConstant.CAMERA_TAKE_PHOTO && mCameraPath != null) {
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            cameraHelper.setDeviceRotation(rotation);
            cameraHelper.takePhone(mCameraPath, ICamera2.MediaType.JPEG);
        }
        //录制视频
        if (NOW_MODE == AppConstant.CAMERA_RECORD_MODE) {
            if (!hasRecording) {
                // 暂停录像
                mVideoPath = cameraHelper.getVideoFilePath();
                hasRecording = cameraHelper.startVideoRecord(mVideoPath,
                    MediaRecorder.OutputFormat.MPEG_4);
                //倒计时动画
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countDownView.start();
                    }
                });
                //  正在录像
                if (hasRecording) {
                    showRecordingUI();
                    recordCountDown();
                    TEXTURE_STATE = AppConstant.TEXTURE_RECORD_STATE;
                }
            } else {
                // 停止录像
                hasRecording = false;
                disposableTime();
                showStopRecordUI();
                cameraHelper.stopVideoRecord();
                saveVideo();
                resetVideoMode();
            }
        }
    }

    private void showStopRecordUI() {
        mVideoSeekTime.setVisibility(View.GONE);
        mCameraPick.setVisibility(View.VISIBLE);
        mCameraStart.setImageResource(R.drawable.ic_camera);
        mSwitchLens.setVisibility(View.VISIBLE);
        mCameraRecycleViewMenu.setVisibility(View.VISIBLE);
        mCameraStart.setVisibility(View.VISIBLE);
        mVideoTime.setVisibility(View.GONE);
        mVideoTime.setText("0:00");
        mCameraRecordSeekBar.setVisibility(View.GONE);
        mCameraRecordSeekBar.setProgress(0);
    }

    /**
     * 取消倒计时
     */
    private void disposableTime() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = null;
    }

    /**
     * 正在录像UI
     */
    private void showRecordingUI() {
        mCameraStart.setImageResource(R.mipmap.ic_recording);
        mSwitchLens.setVisibility(View.GONE);
        mCameraPick.setVisibility(View.GONE);
        mCameraRecycleViewMenu.setVisibility(View.GONE);
        mTextureView.setVisibility(View.VISIBLE);
    }

    /**
     * 重新设置录像模式
     */
    private void resetVideoMode() {
        cameraHelper.setCameraState(ICamera2.CameraMode.RECORD_VIDEO);
        cameraHelper.closeCamera();
        mCameraStart.setClickable(true);
        if (cameraHelper != null) {
            cameraHelper.startBackgroundThread();
        }
        Objects.requireNonNull(cameraHelper).openCamera(mNowCameraType);
        mCameraTouch.resetScale();
    }

    /**
     * 保存视频到相册
     * @param path
     * @param duration
     */
    private void saveVideo(String path, long duration) {
        File file = new File(path);
        Log.d("dd_file.getPath", "" + file.getPath());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.VideoColumns.TITLE, file.getName());
        values.put(MediaStore.Video.VideoColumns.DISPLAY_NAME, file.getName() + ".mp4");
        values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Video.VideoColumns.MIME_TYPE, "video/mpeg");
        values.put(MediaStore.Video.VideoColumns.DURATION, duration);
        values.put(MediaStore.Video.VideoColumns.SIZE, file.length());
        values.put(MediaStore.Video.VideoColumns.DATA, file.getPath());
        Utils.curUri = this.getContentResolver()
            .insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        //                todo:  视频显示  TODO：相册返回重新设置比例
        assert Utils.curUri != null;
        Log.d(TAG, "saveVideo:curUri " + Utils.curUri);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCameraPick.setImageBitmap(getVideoThumb(Utils.curUri));
                mCameraPick.requestLayout();
            }
        });
    }

    public Bitmap getVideoThumb(Uri uri) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(this, uri);
        return media.getFrameAtTime();
    }

    private void updateGridLine() {
        Utils.curW = mTextureView.getWidth();
        Utils.curH = mTextureView.getHeight();
        mGridLineView.requestLayout();
    }

    /**
     * 保存录像视频
     */
    private void saveVideo() {
        if (NOW_MODE == AppConstant.CAMERA_RECORD_MODE) {
            saveVideo(mVideoPath, 2);
            //            关闭聚焦
            Utils.focusFlag = false;
        }
        TEXTURE_STATE = AppConstant.TEXTURE_PREVIEW_STATE;
    }

    /**
     * 移除对焦 消失任务
     */
    private void removeImageFocusRunnable() {
        mCameraFocus.removeCallbacks(mImageFocusRunnable);
    }

    /**
     * 添加 延时消失任务
     */
    private void imageFocusDelayedHind() {
        mCameraFocus.postDelayed(mImageFocusRunnable, 500);
    }

    /**
     * seekBar 添加延时消失任务
     */
    private void seekBarDelayedHind() {
        if (isCanHind) {
            mCameraScaleBarLayout.postDelayed(SeekBarLayoutRunnable, 2000);
        }
        isCanHind = false;
    }

    /**
     * 移除隐藏 seekBar消失的任务
     */
    private void removeSeekBarRunnable() {
        isCanHind = true;
        mCameraScale.removeCallbacks(SeekBarLayoutRunnable);
    }

    /**
     * 隐藏视频录像的进度条
     */
    private void hindVideoRecordSeekBar() {
        mCameraRecordSeekBar.setVisibility(View.GONE);
        mCameraRecordSeekBar.setProgress(0);
    }

    /**
     * 移动焦点图标
     */
    private void moveFocus(int x, int y) {
        mCameraFocus.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams
            = (RelativeLayout.LayoutParams) mCameraFocus.getLayoutParams();
        mCameraFocus.setLayoutParams(layoutParams);

        mFocusAnimation.setDuration(500);
        mFocusAnimation.setRepeatCount(0);
        mFocusAnimation.setOldMargin(x, y);
        mCameraFocus.startAnimation(mFocusAnimation);

        if (mCameraScaleBarLayout.isShown()) {
            mCameraScaleBarLayout.setVisibility(View.GONE);
        }

        // bug已解决
        if (NOW_MODE == AppConstant.CAMERA_RECORD_MODE && !Utils.focusFlag) {
            return;
        }
        cameraHelper.requestFocus(x, y);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //  TODO：待修改      修复相册返回后的变形
        //                cameraHelper.mPreviewSize = new Size(960, 720);
        //        updateScreenSize(R.id.ratio_current);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cameraHelper != null) {
            cameraHelper.startBackgroundThread();
        }

        //        Utils.mRenderer.init(mTextureView, this);
        //        initData();

        //TODO：待修改
        Log.d("dd_mTextureVieweeeee", "" + mTextureView.getWidth());
        mTextureView.setAspectRatio(mRatioSize.getHeight(), mRatioSize.getWidth());
        cameraHelper.mPreviewSize = new Size(
            Utils.defaultWidth * mRatioSize.getWidth() / mRatioSize.getHeight(),
            Utils.defaultWidth);

        System.out.println("onResume--mRatioSize:" + mRatioSize);
        System.out.println("cameraHelper.mPreviewSize:" + cameraHelper.mPreviewSize);

        if (mTextureView.isAvailable()) {
            if (MODE == AppConstant.CAMERA_MODE) {
                if (TEXTURE_STATE == AppConstant.TEXTURE_PREVIEW_STATE) {
                    //预览状态
                    initCamera(mNowCameraType);
                }
            }
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener
        = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            if (MODE == AppConstant.CAMERA_MODE) {
                if (TEXTURE_STATE == AppConstant.TEXTURE_PREVIEW_STATE) {
                    Log.d(TAG, "onSurfaceTextureAvailable: ");
                    initCamera(mNowCameraType);
                }
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            log2("SizeChanged--1",mTextureView.getHeight()+","+mTextureView.getWidth());
            Size size = cameraHelper.configureTransform(width, height);
            //                    todo:  TextureView  cameraTextureView,
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            configureTransformRepeat(size, rotation, width, height);

            log2("SizeChanged--2",mTextureView.getHeight()+","+mTextureView.getWidth());
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    public void configureTransformRepeat(Size mPreviewSize, int rotation, int viewWidth,
        int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: isNoPermissionPause：" + isNoPermissionPause);
        if (isNoPermissionPause) {
            isNoPermissionPause = false;
            return;
        }
        Log.e("camera", "mode:" + MODE);
        if (MODE == AppConstant.CAMERA_MODE) {
            if (TEXTURE_STATE == AppConstant.TEXTURE_PREVIEW_STATE) {
                cameraHelper.closeCamera();
                cameraHelper.stopBackgroundThread();
            }
        }
    }

    /**
     * 隐藏播放界面的控件出来
     */
    private void hindPlayView() {
        mVideoSeekBar.setVisibility(View.GONE);
        mVideoMinePlay.setVisibility(View.GONE);
        mVideoSeekTime.setVisibility(View.GONE);
    }

    /**
     * 显示和隐藏控件
     * @param showWhat mLayoutList中的索引号
     * @param showOrNot 是否显示该空间的调节开关
     */
    private void showLayout(int showWhat, boolean showOrNot) {
        Utils.curLayoutIndex = showWhat;

        View v = mLayoutList.get(showWhat);
        if (showOrNot) {
            //全部隐藏但是AF/AE的显示出来
            for (int i = 0; i < mLayoutBottom.getChildCount(); i++) {
                if (mLayoutBottom.getChildAt(i).getVisibility() == View.VISIBLE) {
                    mLayoutBottom.getChildAt(i).setVisibility(View.GONE);
                }
            }
            v.startAnimation(mShowAction);
            v.setVisibility(View.VISIBLE);
        } else {
            //全部隐藏但是capture的显示出来
            for (int i = 0; i < mLayoutBottom.getChildCount(); i++) {
                if (mLayoutBottom.getChildAt(i).getVisibility() == View.VISIBLE) {
                    mLayoutBottom.getChildAt(i).setVisibility(View.GONE);
                }
            }
            rlCamera.startAnimation(mShowAction);
            rlCamera.setVisibility(View.VISIBLE);
        }
    }

    //    缩放监听
    private class ScaleSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                float scale = (float) progress / (float) seekBar.getMax() *
                    cameraHelper.getMaxZoom();
                cameraHelper.cameraZoom(scale);
                mCameraTouch.setScale(scale);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            removeSeekBarRunnable();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            seekBarDelayedHind();
        }
    }

    //    mTextureView 触摸监听
    private class TextureViewTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("dd_MotionEvent", "" + event.getAction());
            switch (event.getAction()) {
                // 用户两指按下事件
                case MotionEvent.ACTION_DOWN:
                    mCameraTouch.onScaleStart();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 2) {
                        mCameraTouch.onScale0(event);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (event.getPointerCount() == 1) {
                        moveFocus((int) event.getX(), (int) event.getY());
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mCameraTouch.onScaleEnd0(event);
                    return true;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * TextureView 触摸方法
     */
    private class CameraTouch {

        private float mOldScale = 1.0f;

        //        放大缩小的倍数
        private float mScale;

        private float mSpan = 0;

        private float mOldSpan;

        /**
         * 指间距离
         */
        private float mPreDistance = 0;

        public void onScale0(MotionEvent event) {
            //D/dd_event.getX(1): 276.6305
            //D/dd_event.getX(0): 386.0
            //D/dd_event.getY(1): 688.3695
            //D/dd_event.getY(0): 557.0
            Log.d("dd_event.getX(0)", "" + event.getX(0));
            Log.d("dd_event.getY(0)", "" + event.getY(0));

            Log.d("dd_event.getX(1)", "" + event.getX(1));
            Log.d("dd_event.getY(1)", "" + event.getY(1));

            System.out.println("--------------");

            if (event.getPointerCount() == 2) {
                if (mPreDistance == 0) {
                    mPreDistance = distance(event);
                }

                float distance = distance(event);
                Log.d("dd_distance", "" + distance);

                //                 210.47136
                Log.d("dd_mFirstDistance", "" + mPreDistance);
                float scale;
                //                放大
                if (distance > mPreDistance) {
                    scale = (distance - mPreDistance) / 80;
                    scale += mSpan;
                    mOldSpan = scale;
                    mScale = scale;
                    //                 0.055804826  -> 1.9732252
                    Log.d("dd_mScale", "" + mScale);
                    //                    缩小
                } else if (distance < mPreDistance) {
                    scale = distance / mPreDistance;
                    mOldSpan = scale;
                    mScale = scale * mOldScale;
                    Log.d("dd_mOldSpan", "" + mOldSpan);
                    Log.d("dd_mScale_2", "" + mScale);
                } else {
                    return;
                }

                //                log("mScale", mScale + "");
                //                log("mOldSpan", mOldSpan + "");

                cameraHelper.cameraZoom(mScale);
                mCameraScale.setProgress(
                    (int) ((mScale / cameraHelper.getMaxZoom()) * mCameraScale.getMax()));

                if (mScale < 1.0f) {
                    mCameraScale.setProgress(0);
                }
            }
        }

        public void onScale(MotionEvent event) {
            //D/dd_event.getX(1): 276.6305
            //D/dd_event.getX(0): 386.0
            //D/dd_event.getY(1): 688.3695
            //D/dd_event.getY(0): 557.0
            Log.d("dd_event.getX(0)", "" + event.getX(0));
            Log.d("dd_event.getY(0)", "" + event.getY(0));

            Log.d("dd_event.getX(1)", "" + event.getX(1));
            Log.d("dd_event.getY(1)", "" + event.getY(1));

            System.out.println("--------------");

            if (event.getPointerCount() == 2) {
                if (mPreDistance == 0) {
                    mPreDistance = distance(event);
                }

                float distance = distance(event);
                Log.d("dd_distance", "" + distance);

                //                 210.47136
                Log.d("dd_mFirstDistance", "" + mPreDistance);
                float scale;
                //                放大
                if (distance > mPreDistance) {
                    scale = (distance - mPreDistance) / 80;
                    scale += mSpan;
                    mOldSpan = scale;
                    mScale = scale;
                    //                 0.055804826  -> 1.9732252
                    Log.d("dd_mScale", "" + mScale);
                    //                    缩小
                } else if (distance < mPreDistance) {
                    scale = distance / mPreDistance;
                    mOldSpan = scale;
                    mScale = scale * mOldScale;
                    Log.d("dd_mOldSpan", "" + mOldSpan);
                    Log.d("dd_mScale_2", "" + mScale);
                } else {
                    return;
                }

                cameraHelper.cameraZoom(mScale);
                mCameraScale.setProgress(
                    (int) ((mScale / cameraHelper.getMaxZoom()) * mCameraScale.getMax()));

                if (mScale < 1.0f) {
                    mCameraScale.setProgress(0);
                }
            }
        }

        /**
         * scale 开始
         */
        public void onScaleStart() {
            Log.d("dd_onScaleStart", "");
            mPreDistance = 0;
            setScaleMax((int) cameraHelper.getMaxZoom());
            mCameraScaleBarLayout.setVisibility(View.VISIBLE);

            removeSeekBarRunnable();
        }

        private void onScaleEnd0(MotionEvent event) {
            if (mScale < 1.0f) {
                //                mOldScale = 1.0f;
            } else if (mScale > cameraHelper.getMaxZoom()) {
                mOldScale = cameraHelper.getMaxZoom();
            } else {
                mOldScale = mScale;
            }
            mSpan = mOldSpan;

            if (event != null) {
                seekBarDelayedHind();
            }
        }

        /**
         * scale 结束
         * @param event MotionEvent
         */
        private void onScaleEnd(MotionEvent event) {
            if (mScale < 1.0f) {
                mOldScale = 1.0f;
            } else if (mScale > cameraHelper.getMaxZoom()) {
                mOldScale = cameraHelper.getMaxZoom();
            } else {
                mOldScale = mScale;
            }
            mSpan = mOldSpan;

            if (event != null) {
                seekBarDelayedHind();
            }
        }

        /**
         * 重置 缩放
         */
        public void resetScale() {
            mOldScale = 1.0f;
            mSpan = 0f;
            mPreDistance = 0f;
            mCameraScale.setProgress(0);
        }

        public void setScale(float scale) {
            mScale = scale;
            mOldSpan = scale;
            onScaleEnd(null);
        }

        /**
         * 计算两个手指间的距离
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        private void setScaleMax(int max) {
            mCameraScale.setMax(max * 100);
        }
    }

    /**
     * 对焦动画
     */
    private class FocusAnimation extends Animation {

        private int width = 160;

        //        焦点框
        private int mFocusWidth = 120;

        private int oldMarginLeft;

        private int oldMarginTop;

        /**
         * 聚焦动画
         * @param interpolatedTime 点击： 0到1 依次递增
         * @param t
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) mCameraFocus.getLayoutParams();
            //130 130 0 0  ->  130 130 285 0
            Log.d("dd_layoutParams", "" + layoutParams.width + " " + layoutParams.height + " " +
                layoutParams.leftMargin + " " + layoutParams.rightMargin);

            int w = (int) (width * (1 - interpolatedTime));
            if (w < mFocusWidth) {
                w = mFocusWidth;
            }
            layoutParams.width = w;
            layoutParams.height = w;
            if (w == mFocusWidth) {
                mCameraFocus.setLayoutParams(layoutParams);
                return;
            }
            layoutParams.leftMargin = oldMarginLeft - (w / 2);
            layoutParams.topMargin = oldMarginTop + (w / 8);

            //            300 300 201 0   ->   133 133 285 0
            Log.d("dd_layoutParams——2", "" + layoutParams.width + " " + layoutParams.height + " " +
                layoutParams.leftMargin + " " + layoutParams.rightMargin);

            mCameraFocus.setLayoutParams(layoutParams);
        }

        public void setOldMargin(int oldMarginLeft, int oldMarginTop) {
            this.oldMarginLeft = oldMarginLeft;
            this.oldMarginTop = oldMarginTop;
            removeImageFocusRunnable();
            imageFocusDelayedHind();
        }
    }

    /**
     * 曝光 ae 滑动监听事件
     */
    private class CameraSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.ae_seekBar: {
                    if (switchAe.isChecked()) {
                        // 曝光增益
                        if (cameraHelper.getRange1() == null) {
                            break;
                        }
                        //                        [-4, 4]
                        Log.e(TAG, "曝光增益范围：" + cameraHelper.getRange1().toString());
                        //                        51。。50
                        Log.d("dd_progress", "" + progress);
                        int maxValue = cameraHelper.getRange1().getUpper();
                        int minValue = cameraHelper.getRange1().getLower();
                        int all = maxValue - minValue;
                        int time = 100 / all;
                        int ae = ((progress / time) - maxValue) > maxValue ? maxValue :
                            Math.max(((progress / time) - maxValue), minValue);
                        //                     0。。 -1
                        Log.d("dd_ae", "" + ae);
                        cameraHelper.setAERegions(ae);
                        mTipText.setText("曝光增益：" + ae);

                    } else {
                        // 曝光时间
                        if (cameraHelper.getEtr() == null) {
                            mTipText.setText("获取曝光时间失败");
                            break;
                        }
                        Log.e(TAG, "曝光时间范围：" + cameraHelper.getEtr().toString());
                        long max = cameraHelper.getEtr().getUpper();
                        long min = cameraHelper.getEtr().getLower();
                        long ae = ((progress * (max - min)) / 100 + min);
                        cameraHelper.setAeTime(ae);
                        mTipText.setText("曝光时间：" + ae);
                    }
                    break;
                }
                default:
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mTipText.setVisibility(View.VISIBLE);
            mTipText.startAnimation(mAlphaInAnimation);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mTipText.startAnimation(mAlphaOutAnimation);
            mTipText.setVisibility(View.INVISIBLE);
        }
    }

    private void log(String key, String value) {
        logMap.put(key, value);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : logMap.entrySet()) {
            stringBuffer.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        if (mLogSwitch.isChecked()) {
            runOnUiThread(() -> log.setText(stringBuffer.toString()));
        } else {
            runOnUiThread(() -> log.setText(""));
        }
    }

    private <T> void log2(T key, T value) {
        logMap.put((String) (key), (String) (value));
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : logMap.entrySet()) {
            stringBuffer.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        if (mLogSwitch.isChecked()) {
            runOnUiThread(() -> log.setText(stringBuffer.toString()));
        } else {
            runOnUiThread(() -> log.setText(""));
        }
    }

}
