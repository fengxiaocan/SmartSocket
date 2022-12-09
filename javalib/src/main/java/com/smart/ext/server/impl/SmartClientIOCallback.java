package com.smart.ext.server.impl;

import com.smart.ext.pack.PulseSendPack;
import com.smart.ext.parse.SmartParseData;
import com.smart.socket.common.interfaces.common_interfacies.server.IClient;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.smart.socket.core.pojo.OriginalData;

public abstract class SmartClientIOCallback implements IClientIOCallback {
    private PulseSendPack respondPack = new PulseSendPack();

    @Override
    public final void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool) {
        SmartParseData dataPack = new SmartParseData(originalData);
        if (dataPack.isPulse()) {
            respondPulse(client);
        }else {
            onClientRead(dataPack, client, clientPool);
        }
    }

    public abstract void onClientRead(SmartParseData dataPack, IClient client, IClientPool<IClient, String> clientPool);

    /**
     * 回应心跳包
     *
     * @param client
     */
    public final void respondPulse(IClient client) {
        client.send(respondPack);
    }
}
