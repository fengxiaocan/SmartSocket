package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BaseTransfer {
    /**
     * 发送时单个数据包的总长度
     */
    protected int writePackageBytes = 8192;
    /**
     * 读取时单次读取的缓存字节长度,数值越大,读取效率越高.但是相应的系统消耗将越大
     */
    protected int readPackageBytes = 8192;
    //文件的传输协议
    protected FileTransferProtocol protocol = new DefaultFileTransferProtocol();
    protected Set<String> futureSet = new HashSet<>();
    protected ExecutorService executor = Executors.newCachedThreadPool();

    protected boolean submit(String key, Runnable runnable) {
        if (!futureSet.contains(key)) {
            if (!executor.isShutdown() && !executor.isTerminated()) {
                executor.submit(runnable);
                return true;
            }
        }
        return false;
    }

    public void shadow() {
        executor.shutdown();
    }

    protected static void writeInt(OutputStream os, int version) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.rewind();
        byteBuffer.putInt(version);
        os.write(byteBuffer.array());
        os.flush();
    }

    protected static int readInt(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        int read = is.read(bytes);
        if (read == 4) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            return byteBuffer.getInt();
        }
        return -1;
    }


    protected void sendFile(Socket socket, File file, Headers headers) throws Exception {
        FileInputStream fis = null;
        try {
            OutputStream os = socket.getOutputStream();
            //开始
            protocol.begin();
            //写入协议版本
            writeInt(os, protocol.protocolVersion());
            //写入header
            protocol.writeHeaders(os, headers);
            //创建数组
            byte[] array = new byte[writePackageBytes];
            //开始读取数据
            fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(array)) > 0) {
                os.write(array, 0, len);
                os.flush();
            }
            os.close();
        } finally {
            closeIo(fis);
            //协议结束
            protocol.finish();
        }
    }

    protected void sendFile(Socket socket, FileTransfer transfer, Headers headers) throws Exception {
        try {
            OutputStream os = socket.getOutputStream();
            //开始
            protocol.begin();
            //写入协议版本
            writeInt(os, protocol.protocolVersion());
            //写入header
            protocol.writeHeaders(os, headers);
            //创建数组
            byte[] array = new byte[writePackageBytes];
            //开始读取数据
            int len;
            transfer.begin();
            while ((len = transfer.read(array)) > 0) {
                os.write(array, 0, len);
                os.flush();
            }
            os.close();
        } finally {
            transfer.finish();
            //协议结束
            protocol.finish();
        }
    }

    protected void receiveFile(Socket socket, File file) throws Exception {
        FileOutputStream fos = null;
        try {
            InputStream is = socket.getInputStream();
            //开始
            protocol.begin();
            //读取协议版本
            int version = readInt(is);
            if (version != protocol.protocolVersion()) {
                throw new UnsupportedTransferVersionException("Un supported file transfer version:" + version + ",The expected version is" + protocol.protocolVersion());
            }
            //读取header
            protocol.readHeaders(is);

            //创建数组
            byte[] array = new byte[readPackageBytes];
            //正式开始读写文件
            fos = new FileOutputStream(file);
            int len;
            while ((len = is.read(array)) > 0) {
                fos.write(array, 0, len);
                fos.flush();
            }
            is.close();
        } finally {
            closeIo(fos);
            protocol.finish();
        }
    }

    protected void receiveFile(Socket socket, FileReceive receive) throws Exception {
        try {
            InputStream is = socket.getInputStream();
            //开始
            protocol.begin();
            //读取协议版本
            int version = readInt(is);
            if (version != protocol.protocolVersion()) {
                throw new UnsupportedTransferVersionException("Un supported file transfer version:" + version + ",The expected version is" + protocol.protocolVersion());
            }
            //读取header
            Headers headers = protocol.readHeaders(is);
            //正式开始读写文件
            receive.begin(headers);

            //创建数组
            byte[] array = new byte[readPackageBytes];
            int len;
            while ((len = is.read(array)) > 0) {
                receive.receive(array, len);
            }
            is.close();
        } finally {
            receive.finish();
            //协议结束
            protocol.finish();
        }
    }

    protected final void closeIo(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (Exception e) {
            }
        }
    }

    protected final void closeIo(Socket io) {
        if (io != null) {
            try {
                io.close();
            } catch (Exception e) {
            }
        }
    }

    protected final void closeIo(ServerSocket io) {
        if (io != null) {
            try {
                io.close();
            } catch (Exception e) {
            }
        }
    }

    public int getWritePackageBytes() {
        return writePackageBytes;
    }

    public BaseTransfer setWritePackageBytes(int writePackageBytes) {
        if (writePackageBytes > 0) {
            this.writePackageBytes = writePackageBytes;
        }
        return this;
    }

    public int getReadPackageBytes() {
        return readPackageBytes;
    }

    public BaseTransfer setReadPackageBytes(int readPackageBytes) {
        if (readPackageBytes > 0) {
            this.readPackageBytes = readPackageBytes;
        }
        return this;
    }

    public FileTransferProtocol getProtocol() {
        return protocol;
    }

    public BaseTransfer setFileTransferProtocol(FileTransferProtocol protocol) {
        this.protocol = protocol;
        return this;
    }
}
