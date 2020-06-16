// IReceiveMsgListener.aidl
package com.a.http_module;
import com.a.http_module.Msg;
// Declare any non-default types here with import statements

interface IReceiveMsgListener {
   void onReceive(in Msg msg);
}
