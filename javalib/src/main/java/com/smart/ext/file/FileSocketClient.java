package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executors;

public class FileSocketClient extends BaseTransfer {
    protected final String host;
    protected final int port;
    protected OnFileClientListener listener;
    /**
     * 连接超时时间
     */
    protected int connectTimeout = 0;

    public FileSocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public FileSocketClient setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    private Socket createSocket() throws IOException {
        Socket socket = new Socket(host, port);
        if (connectTimeout > 0) {
            socket.setSoTimeout(connectTimeout);
        }
        return socket;
    }

    public FileSocketClient setListener(OnFileClientListener listener) {
        this.listener = listener;
        return this;
    }

    public void reset() {
        synchronized (this) {
            if (executor.isShutdown() || executor.isTerminated()) {
                executor = Executors.newCachedThreadPool();
            }
        }
    }

    /**
     * 发送文件
     *
     * @param transfer
     * @param headers
     * @return
     */
    public boolean send(final FileTransfer transfer, final Headers headers) {
        final String key = String.valueOf(transfer.hashCode());
        return submit(key, new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = createSocket();
                    onConnection(socket);
                    sendFile(socket, transfer, headers);
                } catch (Throwable e) {
                    if (listener != null) {
                        listener.onSocketConnectionFailed(host, port, e);
                    }
                } finally {
                    closeIo(socket);
                    onDisconnection(socket);
                    futureSet.remove(key);
                }
            }
        });
    }

    /**
     * 发送文件
     *
     * @param file
     * @param headers
     * @return
     */
    public boolean send(final File file, final Headers headers) {
        final String key = file.getAbsolutePath();
        return submit(key, new Runnable() {
            public void run() {
                Socket socket = null;
                try {
                    socket = createSocket();
                    onConnection(socket);
                    sendFile(socket, file, headers);
                } catch (Throwable e) {
                    if (listener != null) {
                        listener.onSocketConnectionFailed(host, port, e);
                    }
                } finally {
                    closeIo(socket);
                    //断开连接
                    onDisconnection(socket);
                    futureSet.remove(key);
                }
            }
        });
    }

    /**
     * 接收文件
     *
     * @param file
     * @return
     */
    public boolean receive(final File file) {
        final String key = file.getAbsolutePath();
        return submit(key, new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = createSocket();
                    onConnection(socket);
                    receiveFile(socket, file);
                } catch (Throwable e) {
                    if (listener != null) {
                        listener.onSocketConnectionFailed(host, port, e);
                    }
                } finally {
                    closeIo(socket);
                    //断开连接
                    onDisconnection(socket);
                    futureSet.remove(key);
                }
            }
        });
    }

    /**
     * 接收文件
     *
     * @param receive
     * @return
     */
    public boolean receive(final FileReceive receive) {
        final String key = String.valueOf(receive.hashCode());
        return submit(key, new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = createSocket();
                    onConnection(socket);
                    receiveFile(socket, receive);
                } catch (Throwable e) {
                    if (listener != null) {
                        listener.onSocketConnectionFailed(host, port, e);
                    }
                } finally {
                    closeIo(socket);
                    //断开连接
                    onDisconnection(socket);
                    futureSet.remove(key);
                }
            }
        });
    }

    private void onConnection(Socket socket) {
        if (listener != null) {
            InetAddress inetAddress = socket.getInetAddress();
            String hostName = inetAddress.getHostName();
            int port = socket.getPort();
            listener.onSocketConnectionSuccess(hostName, port);
        }
    }

    private void onDisconnection(Socket socket) {
        if (listener != null) {
            InetAddress inetAddress = socket.getInetAddress();
            String hostName = inetAddress.getHostName();
            int port = socket.getPort();
            listener.onSocketDisconnection(hostName, port);
        }
    }

    @Override
    public FileSocketClient setWritePackageBytes(int writePackageBytes) {
        super.setWritePackageBytes(writePackageBytes);
        return this;
    }

    @Override
    public FileSocketClient setReadPackageBytes(int readPackageBytes) {
        super.setReadPackageBytes(readPackageBytes);
        return this;
    }

    @Override
    public FileSocketClient setFileTransferProtocol(FileTransferProtocol protocol) {
        super.setFileTransferProtocol(protocol);
        return this;
    }
}
