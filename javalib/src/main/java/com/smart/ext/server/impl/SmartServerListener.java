package com.smart.ext.server.impl;

import com.smart.socket.common.interfaces.common_interfacies.server.IClient;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.smart.socket.common.interfaces.common_interfacies.server.IServerActionListener;
import com.smart.socket.common.interfaces.common_interfacies.server.IServerShutdown;

import java.util.HashMap;
import java.util.Map;

public abstract class SmartServerListener implements IServerActionListener {
    protected int serverPort;
    protected Map<IClient, IClientIOCallback> callbackMap= new HashMap<>();

    public SmartServerListener(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public abstract IClientIOCallback onCreateClientIOCallback(IClient client, int serverPort, IClientPool clientPool);

    @Override
    public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
        IClientIOCallback callback = onCreateClientIOCallback(client, serverPort, clientPool);
        callbackMap.put(client, callback);
        client.addIOCallback(callback);
    }

    @Override
    public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
        IClientIOCallback callback = callbackMap.get(client);
        client.removeIOCallback(callback);
    }

    @Override
    public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
        shutdown.shutdown();
    }
}
