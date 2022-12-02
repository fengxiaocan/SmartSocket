package com.smart;

import com.smart.socket.client.sdk.OkSocket;
import com.smart.socket.common.interfaces.common_interfacies.server.IClient;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.smart.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.smart.socket.core.iocore.interfaces.ISendPack;
import com.smart.socket.core.pojo.OriginalData;
import com.smart.socket.server.action.ServerActionAdapter;
import com.smart.socket.server.impl.OkServerOptions;
import com.smart.ai.protocol.SmartReaderProtocol;

import java.nio.ByteBuffer;

public class Test {
    public static void main(String[] args) {
        OkServerOptions.Builder builder = new OkServerOptions.Builder(OkServerOptions.getDefault())
                .setReaderProtocol(new SmartReaderProtocol())
                .setMaxReadDataMB(1000)
                .setWritePackageBytes(1024);

        OkSocket.server(33333).registerReceiver(new ServerActionAdapter() {
            @Override
            public void onServerListening(int serverPort) {
                System.err.println("onServerListening" + serverPort);
            }

            @Override
            public void onClientConnected(IClient client, int serverPort, IClientPool clientPool) {
                System.err.println("onClientConnected" + client.getHostIp());
                client.addIOCallback(new IClientIOCallback() {
                    @Override
                    public void onClientRead(OriginalData data, IClient client, IClientPool<IClient, String> clientPool) {
                        System.err.println("onClientWrite:" + new String(data.getBodyBytes()));
                    }

                    @Override
                    public void onClientWrite(ISendPack send, IClient client, IClientPool<IClient, String> clientPool) {
                    }
                });
            }

            @Override
            public void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool) {
                System.err.println("onClientDisconnected:" + client.getHostIp());
            }

            @Override
            public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
                System.err.println("onServerWillBeShutdown:" + serverPort);
            }

            @Override
            public void onServerAlreadyShutdown(int serverPort) {
                System.err.println("onServerAlreadyShutdown:" + serverPort);
            }
        }).listen(builder.build());

        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putChar((char) 12700);
        byte[] bytes = buffer.array();
        for (int i = 0; i < bytes.length; i++) {
            System.err.println(i + ":" + bytes[i]);
        }
    }

}
