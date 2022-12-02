package com.smart.ai.impl;

import com.smart.ai.parse.SmartPackParse;
import com.smart.socket.common.interfaces.common_interfacies.server.IClient;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.smart.socket.core.pojo.OriginalData;

public abstract class SmartClientCallback implements IClientIOCallback {
    @Override
    public final void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
        onClientRead(new SmartPackParse(originalData), client, clientPool);
    }

    public void onClientRead(SmartPackParse dataPack, IClient client, IClientPool<IClient, String> clientPool) {

    }
}
