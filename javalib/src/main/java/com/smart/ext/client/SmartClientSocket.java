package com.smart.ext.client;

import com.smart.ext.client.impl.SmartSocketListener;
import com.smart.ext.protocol.SmartReaderProtocol;
import com.smart.socket.client.sdk.OkSocket;
import com.smart.socket.client.sdk.client.ConnectionInfo;
import com.smart.socket.client.sdk.client.OkSocketOptions;
import com.smart.socket.client.sdk.client.connection.IConnectionManager;

public class SmartClientSocket {
    private IConnectionManager manager;

    private SmartClientSocket(IConnectionManager manager) {
        this.manager = manager;
    }

    public static SmartClientSocket socket(IConnectionManager manager) {
        return new SmartClientSocket(manager);
    }

    public static SmartClientSocket socket(ConnectionInfo connectInfo) {
        return new SmartClientSocket(OkSocket.open(connectInfo));
    }

    public static SmartClientSocket socket(String ip, int port) {
        return new SmartClientSocket(OkSocket.open(ip, port));
    }

    public SmartClientSocket registerReceiver(SmartSocketListener listener) {
        manager.registerReceiver(listener);
        return this;
    }

    public void connect() {
        build().connect();
    }

    public IConnectionManager build() {
        OkSocketOptions.Builder builder = new OkSocketOptions.Builder(OkSocketOptions.getDefault())
                .setReaderProtocol(new SmartReaderProtocol());
        manager.option(builder.build());
        return manager;
    }

    public IConnectionManager build(OkSocketOptions.Builder builder) {
        builder.setReaderProtocol(new SmartReaderProtocol());
        manager.option(builder.build());
        return manager;
    }

}
