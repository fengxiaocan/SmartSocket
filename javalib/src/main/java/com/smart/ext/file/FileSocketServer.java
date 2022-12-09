package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSocketServer extends BaseTransfer {
    /**
     * 端口号
     */
    protected final int serverPort;

    /**
     * 服务器连接能力数
     */
    protected int mConnectCapacity;
    /**
     * 连接超时时间
     */
    protected int connectTimeout = 0;
    /**
     * 最大传输次数
     */
    protected int maxTransferCount = 1;
    //回调
    protected OnFileServerListener listener;

    protected volatile boolean isStop = false;

    public FileSocketServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public FileSocketServer setConnectCapacity(int mConnectCapacity) {
        this.mConnectCapacity = mConnectCapacity;
        return this;
    }

    public FileSocketServer setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public FileSocketServer setMaxTransferCount(int maxTransferCount) {
        this.maxTransferCount = maxTransferCount;
        return this;
    }

    public FileSocketServer foreverTransfer() {
        this.maxTransferCount = -1;
        return this;
    }

    public FileSocketServer setListener(OnFileServerListener listener) {
        this.listener = listener;
        return this;
    }

    private ServerSocket createServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverPort, mConnectCapacity);
        if (connectTimeout > 0) {
            serverSocket.setSoTimeout(connectTimeout);
        }
        int localPort = serverSocket.getLocalPort();
        listener.onServerListening(localPort);
        return serverSocket;
    }

    private boolean checkIsStop(AtomicInteger atomic) {
        return isStop || (maxTransferCount > 0 && atomic.get() <= 0);
    }

    public void reset() {
        synchronized (this) {
            isStop = true;
            if (executor.isShutdown() || executor.isTerminated()) {
                executor = Executors.newCachedThreadPool();
            }
        }
    }

    public void shadow() {
        isStop = true;
        executor.shutdown();
    }

    public boolean sendServer(final File file, final Headers headers) {
        final String absolutePath = file.getAbsolutePath();
        return submit(absolutePath, new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                AtomicInteger COUNT = new AtomicInteger(maxTransferCount);

                int localPort = serverPort;
                try {
                    serverSocket = createServer();
                    localPort = serverSocket.getLocalPort();
                    while (!checkIsStop(COUNT)) {
                        Socket socket = null;
                        try {
                            socket = serverSocket.accept();
                            onClientConnected(socket, localPort);
                            sendFile(socket, file, headers);
                            if (maxTransferCount > 0) {
                                COUNT.getAndDecrement();
                            }
                            onClientTransferComplete(socket, localPort);
                        } catch (Exception e) {
                            onClientTransferError(socket, localPort, e);
                        } finally {
                            closeIo(socket);
                            onClientDisconnected(socket, localPort);
                        }
                    }
                } catch (Throwable e) {
                    onServerError(localPort);
                } finally {
                    closeIo(serverSocket);
                    onServerShadow(localPort);
                    futureSet.remove(absolutePath);
                }
            }
        });
    }

    public boolean sendServer(final FileTransfer transfer, final Headers headers) {
        final String key = String.valueOf(transfer.hashCode());
        return submit(key, new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                AtomicInteger COUNT = new AtomicInteger(maxTransferCount);
                int localPort = serverPort;
                try {
                    serverSocket = createServer();
                    localPort = serverSocket.getLocalPort();
                    while (!checkIsStop(COUNT)) {
                        Socket socket = null;
                        try {
                            socket = serverSocket.accept();
                            onClientConnected(socket, localPort);
                            sendFile(socket, transfer, headers);
                            if (maxTransferCount > 0) {
                                COUNT.getAndDecrement();
                            }
                            onClientTransferComplete(socket, localPort);
                        } catch (Exception e) {
                            onClientTransferError(socket, localPort, e);
                        } finally {
                            closeIo(socket);
                            onClientDisconnected(socket, localPort);
                        }
                    }
                } catch (Throwable e) {
                    onServerError(localPort);
                } finally {
                    closeIo(serverSocket);
                    onServerShadow(localPort);
                    futureSet.remove(key);
                }
            }
        });
    }


    public boolean receiveServer(final File file) {
        final String key = file.getAbsolutePath();
        return submit(key, new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                AtomicInteger COUNT = new AtomicInteger(maxTransferCount);
                int localPort = serverPort;
                try {
                    serverSocket = createServer();
                    localPort = serverSocket.getLocalPort();
                    while (!checkIsStop(COUNT)) {
                        Socket socket = null;
                        try {
                            socket = serverSocket.accept();
                            onClientConnected(socket, localPort);
                            receiveFile(socket, file);
                            if (maxTransferCount > 0) {
                                COUNT.getAndDecrement();
                            }
                            onClientTransferComplete(socket, localPort);
                        } catch (Exception e) {
                            onClientTransferError(socket, localPort, e);
                        } finally {
                            closeIo(socket);
                            onClientDisconnected(socket, localPort);
                        }
                    }
                } catch (Throwable e) {
                    onServerError(localPort);
                } finally {
                    closeIo(serverSocket);
                    onServerShadow(localPort);
                    futureSet.remove(key);
                }
            }
        });
    }

    public boolean receiveServer(final FileReceive receive) {
        final String key = String.valueOf(receive.hashCode());
        return submit(key, new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                isStop = false;
                AtomicInteger COUNT = new AtomicInteger(maxTransferCount);
                int localPort = serverPort;
                try {
                    serverSocket = createServer();
                    localPort = serverSocket.getLocalPort();
                    while (!checkIsStop(COUNT)) {
                        Socket socket = null;
                        try {
                            socket = serverSocket.accept();
                            onClientConnected(socket, localPort);
                            receiveFile(socket, receive);
                            if (maxTransferCount > 0) {
                                COUNT.getAndDecrement();
                            }
                            onClientTransferComplete(socket, localPort);
                        } catch (Exception e) {
                            onClientTransferError(socket, localPort, e);
                        } finally {
                            closeIo(socket);
                            onClientDisconnected(socket, localPort);
                        }
                    }
                } catch (Throwable e) {
                    onServerError(localPort);
                } finally {
                    closeIo(serverSocket);
                    onServerShadow(localPort);
                    futureSet.remove(key);
                }
            }
        });
    }

    private void onServerError(int localPort) {
        if (listener != null) {
            listener.onServerError(localPort);
        }
    }

    private void onClientTransferError(Socket socket, int localPort, Exception e) {
        if (listener != null) {
            listener.onClientTransferError(socket, localPort, e);
        }
    }

    private void onClientConnected(Socket socket, int localPort) {
        if (listener != null) {
            listener.onClientConnected(socket, localPort);
        }
    }

    private void onServerShadow(int localPort) {
        if (listener != null) {
            listener.onServerShadow(localPort);
        }
    }

    private void onClientDisconnected(Socket socket, int localPort) {
        if (listener != null) {
            listener.onClientDisconnected(socket, localPort);
        }
    }

    private void onClientTransferComplete(Socket socket, int localPort) {
        if (listener != null) {
            listener.onClientTransferComplete(socket, localPort);
        }
    }

    @Override
    public FileSocketServer setWritePackageBytes(int writePackageBytes) {
        super.setWritePackageBytes(writePackageBytes);
        return this;
    }

    @Override
    public FileSocketServer setReadPackageBytes(int readPackageBytes) {
        super.setReadPackageBytes(readPackageBytes);
        return this;
    }

    @Override
    public FileSocketServer setFileTransferProtocol(FileTransferProtocol protocol) {
        super.setFileTransferProtocol(protocol);
        return this;
    }
}
