package com.smart.socket;

import android.util.Log;

import com.smart.socket.client.sdk.OkSocket;
import com.smart.socket.common.interfaces.common_interfacies.server.IClient;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.smart.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.smart.socket.server.action.ServerActionAdapter;
import com.smart.socket.server.impl.OkServerOptions;

public class SmartSocket {
    private int port = 33333;

    public void startServer(){
        OkServerOptions.Builder builder = new OkServerOptions.Builder(OkServerOptions.getDefault()).setMaxReadDataMB(1000)
                .setWritePackageBytes(1024);

        OkSocket.server(port).registerReceiver(new ServerActionAdapter() {
            @Override
            public void onServerListening(int serverPort) {
                Log.e("noah","onServerListening"+ serverPort);
            }

            @Override
            public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {

                Log.e("noah", "onClientConnected" + client.getHostIp());
            }

            @Override
            public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {

            }

            @Override
            public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {

            }

            @Override
            public void onServerAlreadyShutdown(int serverPort) {

            }
        }).listen(builder.build());
    }

}
