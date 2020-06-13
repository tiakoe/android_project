package com.a.http_module;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AidlMainActivity extends AppCompatActivity {

    MyService.MyBinder binder = null;
    ServiceConnection mConnection;

    private EditText mEditText;
    private List<Msg> mMsgs = new ArrayList<>();
    private IMsgManager mIMsgManager;
    private Handler mHandler;

    static class MyHandler extends Handler {
        private ListAdapter mAdapter;
        private ListView mListView;
        private List<Msg> mMsgs;

        private MyHandler(ListAdapter adapter, ListView mListView, List<Msg> mMsgs) {
            this.mAdapter = new WeakReference<>(adapter).get();
            this.mListView = new WeakReference<>(mListView).get();
            this.mMsgs = new WeakReference<>(mMsgs).get();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            mListView.smoothScrollToPosition(mMsgs.size() - 1);
        }
    }

    private IReceiveMsgListener mReceiveMsgListener = new IReceiveMsgListener.Stub() {

        @Override
        public void onReceive(Msg msg) {
            msg.setTime(System.currentTimeMillis());
            mMsgs.add(msg);
            mHandler.sendEmptyMessage(1);
        }
    };

    //
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        //当承载IBinder的进程消失时接收回调的接口
        @Override
        public void binderDied() {
            if (null == mIMsgManager) {
                return;
            }
            mIMsgManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mIMsgManager = null;
            //断线重来逻辑
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aidl_activity_main);

        ListView mListView = (ListView) findViewById(R.id.listview);
        mEditText = (EditText) findViewById(R.id.edit_text);
        ListAdapter mAdapter = new ListAdapter(this, mMsgs);
        mListView.setAdapter(mAdapter);

        mHandler = new MyHandler(mAdapter, mListView,mMsgs);

//        建立连接时，从onServiceConnected中拿到bind转化为自己的bind；
//        IMsgManager就是bind接口；
//
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//
                binder = (MyService.MyBinder) iBinder;
                mIMsgManager = IMsgManager.Stub.asInterface(iBinder);
                try {
                    //链接到死亡代理，当IBinder死亡时收到回调
                    mIMsgManager.asBinder().linkToDeath(mDeathRecipient, 0);
                    mIMsgManager.registerReceiveListener(mReceiveMsgListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        //注意Activity和Service是同一进程才能使用Intent通信
        Intent intent = new Intent(AidlMainActivity.this, MyService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);//开启服务

        findViewById(R.id.button).setOnClickListener(v -> {
            if (TextUtils.isEmpty(mEditText.getText().toString())) {
                Toast.makeText(AidlMainActivity.this, "消息为空", Toast.LENGTH_SHORT).show();
                return;
            }
            binder.sendMsg(new Msg(mEditText.getText().toString().trim()));
        });
        findViewById(R.id.btn_exit).setOnClickListener(v -> AidlMainActivity.this.finish());
    }


    @Override
    protected void onDestroy() {
        //解除注册
        if (null != mIMsgManager && mIMsgManager.asBinder().isBinderAlive()) {
            try {
                mIMsgManager.unregisterReceiveListener(mReceiveMsgListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //解除绑定服务
        unbindService(mConnection);
        super.onDestroy();
    }
}
