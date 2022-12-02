package com.smart.socket.common.interfaces.common_interfacies.server;


import com.smart.socket.core.iocore.interfaces.ISendPack;
import com.smart.socket.core.pojo.OriginalData;

public interface IClientIOCallback {

    void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool);

    void onClientWrite(ISendPack pack, IClient client, IClientPool<IClient, String> clientPool);

}
