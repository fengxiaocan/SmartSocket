package com.smart.ext.server;

import com.smart.ext.protocol.SmartReaderProtocol;
import com.smart.ext.server.impl.SmartServerListener;
import com.smart.socket.client.sdk.OkSocket;
import com.smart.socket.common.interfaces.common_interfacies.server.IServerManager;
import com.smart.socket.server.impl.OkServerOptions;

public class SmartServerSocket {
    private IServerManager<OkServerOptions> manager;

    private SmartServerSocket(IServerManager<OkServerOptions> manager) {
        this.manager = manager;
    }

    public static SmartServerSocket server(SmartServerListener listener) {
        return new SmartServerSocket(OkSocket.server(listener.getServerPort()).registerReceiver(listener));
    }

    public IServerManager<OkServerOptions> listen(OkServerOptions.Builder builder) {
        if (!manager.isLive()) {
            builder.setReaderProtocol(new SmartReaderProtocol());
            manager.listen(builder.build());
        }
        return manager;
    }

    public IServerManager<OkServerOptions> listen() {
        if (!manager.isLive()) {
            OkServerOptions.Builder builder = new OkServerOptions.Builder(OkServerOptions.getDefault()).setReaderProtocol(new SmartReaderProtocol());
            manager.listen(builder.build());
        }
        return manager;
    }
}
