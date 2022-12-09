package com.smart;

import com.smart.ext.file.FileSocketServer;
import com.smart.ext.file.OnFileServerListener;
import com.smart.ext.head.Headers;
import com.smart.ext.pack.StringSendPack;
import com.smart.ext.parse.SmartParseData;
import com.smart.ext.server.SmartServerSocket;
import com.smart.ext.server.impl.SmartClientIOCallback;
import com.smart.ext.server.impl.SmartServerListener;
import com.smart.socket.common.interfaces.common_interfacies.server.IClient;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientIOCallback;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.smart.socket.common.interfaces.common_interfacies.server.IServerShutdown;
import com.smart.socket.core.iocore.interfaces.ISendPack;

import java.io.File;
import java.net.Socket;

public class Test {
    public static void main(String[] args) {
        SmartServerSocket.server(new SmartServerListener(33333) {
            @Override
            public void onClientConnected(final IClient client, int serverPort, IClientPool clientPool) {
                super.onClientConnected(client, serverPort, clientPool);
                System.err.println("onClientConnected:" + client.getHostName());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            client.send(new StringSendPack("看过《风云》漫画的小伙伴都知道，摩诃无量是聂风和步惊云的合璧技能，威力非常可怕，堪称《风云》系列威能最强的武学，漫画中步惊云和聂风凭借这一招击败了众多强敌，然而令人意外的是，在《风云》第三部面对连城志这个最恐怖敌人的时候，风云二人却没有使用摩诃无量，这是怎么回事呢？"));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onClientDisconnected(final IClient client, int serverPort, IClientPool clientPool) {
                super.onClientDisconnected(client, serverPort, clientPool);
                System.err.println("onClientDisconnected:" + client.getHostName());
            }

            @Override
            public void onServerWillBeShutdown(int serverPort, IServerShutdown shutdown, IClientPool clientPool, Throwable throwable) {
                super.onServerWillBeShutdown(serverPort, shutdown, clientPool, throwable);
                System.err.println("onServerWillBeShutdown");
            }

            @Override
            public IClientIOCallback onCreateClientIOCallback(IClient client, int serverPort, IClientPool clientPool) {
                return new SmartClientIOCallback() {
                    @Override
                    public void onClientRead(SmartParseData dataPack, IClient client, IClientPool<IClient, String> clientPool) {
                        if (dataPack.isString()) {
                            System.err.println("收到客户端的信息:" + dataPack.asString());
                        }
                    }

                    @Override
                    public void onClientWrite(ISendPack pack, IClient client, IClientPool<IClient, String> clientPool) {
                    }
                };
            }

            @Override
            public void onServerListening(int serverPort) {
                System.err.println("onServerListening:" + serverPort);
            }

            @Override
            public void onServerAlreadyShutdown(int serverPort) {
                System.err.println("onServerAlreadyShutdown:" + serverPort);
            }
        }).listen();

        Headers headers = new Headers();
        File file = new File(".", "88957204.gif");
        headers.addHeader("name",file.getName());
        FileSocketServer server = new FileSocketServer(55066);
        server.setMaxTransferCount(500);
        server.setListener(new OnFileServerListener.IMPL() {
            @Override
            public void onServerListening(int serverPort) {
                System.err.println("FileSocketServer onServerListening:" + serverPort);
            }

            @Override
            public void onClientConnected(Socket socket, int serverPort) {
                System.err.println("onClientConnected:" + getHostName(socket));
            }

            @Override
            public void onClientDisconnected(Socket socket, int serverPort) {
                System.err.println("onClientDisconnected:" + getHostName(socket));
            }

            private String getHostName(Socket socket) {
                if (socket != null) {
                    return socket.getLocalAddress().getHostName();
                }
                return "0.0.0.0";
            }

            @Override
            public void onClientTransferComplete(Socket socket, int serverPort) {
                System.err.println("onClientTransferComplete:" + getHostName(socket));
            }

            @Override
            public void onClientTransferError(Socket socket, int serverPort, Throwable throwable) {
                System.err.println("onClientTransferError:" + getHostName(socket));
                throwable.printStackTrace();
            }

            @Override
            public void onServerError(int serverPort) {
                System.err.println("onServerError:" + serverPort);
            }

            @Override
            public void onServerShadow(int serverPort) {
                System.err.println("onServerShadow:" + serverPort);
            }
        });
        server.sendServer(file, headers);
    }

}
