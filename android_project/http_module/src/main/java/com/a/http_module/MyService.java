package com.a.http_module;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;


public class MyService extends Service {
    //AIDL不支持正常的接口回调，使用RemoteCallbackList实现接口回调
    private RemoteCallbackList<IReceiveMsgListener> mReceiveListener = new RemoteCallbackList<>();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends IMsgManager.Stub {

        //发送消息
        @Override
        public void sendMsg(Msg msg) {
            receiveMsg(msg);
        }

        //注册
        @Override
        public void registerReceiveListener(IReceiveMsgListener receiveListener) {
            mReceiveListener.register(receiveListener);
        }

        //解除注册
        @Override
        public void unregisterReceiveListener(IReceiveMsgListener receiveListener) {
            boolean success = mReceiveListener.unregister(receiveListener);
            if (success) {
                Log.d("tag", "===  解除注册成功");
            } else {
                Log.d("tag", "===  解除注册失败 ");
            }
        }

        //        onTransact()解析外包进程的调用，然后调用本地服务方法，组装数据返回
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            try {
               return super.onTransact(code, data, reply, flags);
            } catch (RuntimeException e) {
                Log.w("MyClass", "Unexpected remote exception", e);
                throw e;
            }catch (RemoteException e2){
                Log.w("MyClass", "RemoteException", e2);
                throw e2;
            }
            //            return super.onTransact(code, data, reply, flags);
        }
    }

    //收到消息处理
    public void receiveMsg(Msg msg) {
        //通知Callback循环开始,返回N为实现mReceiveListener回调的个数
        final int N = mReceiveListener.beginBroadcast();
        msg.setMsg("我是服务器，我收到了：" + msg.getMsg());
        for (int i = 0; i < N; i++) {
            IReceiveMsgListener listener = mReceiveListener.getBroadcastItem(i);
            if (listener != null) {
                try {
                    listener.onReceive(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        //通知Callback循环结束
        mReceiveListener.finishBroadcast();
    }

}
