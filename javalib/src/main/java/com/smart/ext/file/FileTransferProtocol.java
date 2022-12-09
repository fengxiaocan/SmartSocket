package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileTransferProtocol {
    /**
     * 文件传输协议版本类型
     *
     * @return
     */
    int protocolVersion();

    /**
     * 开始
     */
    void begin();

    /**
     * 文件传输头文件
     *
     * @param os
     * @param headers
     */
    void writeHeaders(OutputStream os, Headers headers) throws IOException;

    /**
     * 文件传输头文件
     *
     * @param os
     */
    Headers readHeaders(InputStream os) throws IOException;

    /**
     * 文件传输结束
     */
    void finish();
}
