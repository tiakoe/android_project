package com.example.myapplication.sensor_event;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ExampleSensorEvent01 extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    // 加速度计
    private float[] accValues = new float[3];
    // 地磁场传感器
    private float[] magValues = new float[3];
    // 旋转矩阵，用来保存磁场和加速度的数据
    private float[] rotationMatrix = new float[9];
    // 模拟方向传感器的数据（原始数据为弧度）
    private float[] orientationAngles = new float[3];

    //    UI
    private LevelView mLevelView;
    private TextView mTvHorz;
    private TextView mTvVert;

    //    Sensor
    Sensor acc_sensor;
    Sensor mag_sensor;
    Sensor game_sensor;
    Sensor prox_sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_sensor_event_01);

        mLevelView = findViewById(R.id.gv_hv);
        mTvVert = findViewById(R.id.tvv_vertical);
        mTvHorz = findViewById(R.id.tvv_horz);

        // 获取系统传感器服务
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        /*
         * TYPE_ACCELEROMETER      加速度计传感器
         * TYPE_MAGNETIC_FIELD    地磁场传感器
         * TYPE_GEOMAGNETIC_ROTATION_VECTOR     地磁旋转矢量传感器，功耗低，精度低
         * TYPE_PROXIMITY     近程传感器
         * */

        //选择传感器类型
        assert sensorManager != null;
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        game_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        prox_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }

    @Override
    public void onResume() {
        super.onResume();

        // 给传感器注册监听
        if (acc_sensor != null) {
            sensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mag_sensor != null) {
            sensorManager.registerListener(this, mag_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (game_sensor != null) {
            sensorManager.registerListener(this, game_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (prox_sensor != null) {
            sensorManager.registerListener(this, prox_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    protected void onPause() {
        // 取消方向传感器的监听
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        // 取消方向传感器的监听
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("sensor.getName", sensor.getName());
        Log.d("sensor.getVendor", sensor.getVendor());

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // 获取手机触发event的传感器的类型
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, accValues,
                        0, accValues.length);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, magValues,
                        0, magValues.length);
                break;
        }

        //        更新
        SensorManager.getRotationMatrix(rotationMatrix, null, accValues, magValues);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        /**【0】方位角（绕 z 轴旋转的角度）。此为设备当前指南针方向与磁北向之间的角度。如果设备的上边缘面朝磁北向，则方位角为 0 度；如果上边缘朝南，则方位角为 180
         * 度。与之类似，如果上边缘朝东，则方位角为 90 度；如果上边缘朝西，则方位角为 270 度。
         【1】俯仰角（绕 x
         轴旋转的角度）。此为平行于设备屏幕的平面与平行于地面的平面之间的角度。如果将设备与地面平行放置，且其下边缘最靠近您，同时将设备上边缘向地面倾斜，则俯仰角将变为正值。沿相反方向倾斜（将设备上边缘向远离地面的方向移动）将使俯仰角变为负值。值的范围为 -180 度到 180 度。
         【2】倾侧角（绕 y
         轴旋转的角度）。此为垂直于设备屏幕的平面与垂直于地面的平面之间的角度。如果将设备与地面平行放置，且其下边缘最靠近您，同时将设备左边缘向地面倾斜，则侧倾角将变为正值。沿相反方向倾斜（将设备右边缘移向地面）将使侧倾角变为负值。值的范围为 -90 度到 90 度。
         */
        // 获取　沿着X轴倾斜时　与Y轴的夹角
        float pitchAngle = orientationAngles[1];

        // 获取　沿着Y轴的滚动时　与X轴的角度
        float rollAngle = orientationAngles[2];
        showAngleChanged(rollAngle, pitchAngle);

        float distance = event.values[0];
        Log.d("distance", String.valueOf(distance));

    }

    /**
     * 角度变更后显示到界面
     *
     * @param rollAngle
     * @param pitchAngle
     */
    @SuppressLint("SetTextI18n")
    private void showAngleChanged(float rollAngle, float pitchAngle) {

        mLevelView.setAngle(rollAngle, pitchAngle);

        mTvHorz.setText((int) Math.toDegrees(rollAngle) + "°");
        mTvVert.setText((int) Math.toDegrees(pitchAngle) + "°");
    }
}
