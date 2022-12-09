package com.smart.ext.client.impl;

import com.smart.ext.pack.PulseSendPack;
import com.smart.ext.parse.SmartParseData;
import com.smart.socket.client.sdk.OkSocket;
import com.smart.socket.client.sdk.client.ConnectionInfo;
import com.smart.socket.client.sdk.client.connection.AbsReconnectionManager;
import com.smart.socket.core.pojo.OriginalData;

public abstract class SmartSocketListener extends AbsReconnectionManager {
    private PulseSendPack pulseSendPack = new PulseSendPack();

    @Override
    public final void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
        SmartParseData dataPack = new SmartParseData(data);
        if (dataPack.isPulse()) {
            //接收到心跳包回应后,需要喂狗,否则时间到达后会自动断开连接
            OkSocket.open(info).getPulseManager().feed();
        }else {
            onSocketReadResponse(info, action, dataPack);
        }
    }

    public void onSocketReadResponse(ConnectionInfo info, String action, SmartParseData dataPack) {

    }

    @Override
    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
        startPulse(info);
    }

    /**
     * 开始心跳
     * @param info
     */
    public final void startPulse(ConnectionInfo info) {
        //连接成功其他操作...
        //链式编程调用,给心跳管理器设置心跳数据,一个连接只有一个心跳管理器,因此数据只用设置一次,如果断开请再次设置.
        OkSocket.open(info)
                .getPulseManager()
                .setPulseSender(pulseSendPack)//只需要设置一次,下一次可以直接调用pulse()
                .pulse();//开始心跳,开始心跳后,心跳管理器会自动进行心跳触发
    }
}
