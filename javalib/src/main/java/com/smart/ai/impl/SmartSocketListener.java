package com.smart.ai.impl;

import com.smart.ai.parse.SmartPackParse;
import com.smart.socket.client.sdk.client.ConnectionInfo;
import com.smart.socket.client.sdk.client.connection.AbsReconnectionManager;
import com.smart.socket.core.pojo.OriginalData;

public abstract class SmartSocketListener extends AbsReconnectionManager {
    @Override
    public final void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
        onSocketReadResponse(info, action, data);
    }

    public void onSocketReadResponse(ConnectionInfo info, String action, SmartPackParse dataPack) {

    }
}
