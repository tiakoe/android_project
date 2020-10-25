package com.a.mycamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class OpenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("dd_broadCastReceiver","onReceiver...");

        Intent mBootIntent = new Intent(context, CameraActivity.class);
        // 必须设置FLAG_ACTIVITY_NEW_TASK
        mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mBootIntent);

    }

}
