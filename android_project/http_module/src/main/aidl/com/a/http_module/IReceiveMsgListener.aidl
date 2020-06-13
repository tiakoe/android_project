// IReceiveMsgListener.aidl
package com.a.http_module;
import com.a.http_module.Msg;

interface IReceiveMsgListener {
   void onReceive(in Msg msg);
}
