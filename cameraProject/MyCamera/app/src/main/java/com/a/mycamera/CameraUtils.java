package com.a.mycamera;

import android.app.Activity;
import android.util.Size;
import android.widget.Toast;

/**
 * create by 72088385
 * on 2020/9/28
 */

public class CameraUtils {

    /**
     * 录像尺寸
     * @param choices
     * @return
     */
    public static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 1560 / 720 && size.getWidth() <= 1080) {
                return size;
            }
        }
        return choices[choices.length - 1];
    }

    public static void showToast(Activity activity,final String text) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
