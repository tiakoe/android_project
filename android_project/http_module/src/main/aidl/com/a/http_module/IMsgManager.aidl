// IMsg.aidl
package com.a.http_module;
import com.a.http_module.IReceiveMsgListener;
import com.a.http_module.Msg;

interface IMsgManager {
   void sendMsg(in Msg msg);
   void registerReceiveListener(IReceiveMsgListener receiveListener);
   void unregisterReceiveListener(IReceiveMsgListener receiveListener);
}
