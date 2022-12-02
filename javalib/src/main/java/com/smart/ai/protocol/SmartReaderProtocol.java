package com.smart.ai.protocol;


import com.smart.socket.core.protocol.IReaderProtocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SmartReaderProtocol implements IReaderProtocol {
    //第一位:预留body长度,第二位:预留类型,第三位:预留参数
    public static final int HEADER_LENGTH = 4 + 4 + 4;

    /**
     * 返回不能为零或负数的报文头长度(字节数)。
     * 返回的值应符合服务器文档中的报文头的固定长度值(字节数)
     *
     * @return 固定报文头的长度(字节数)
     */
    @Override
    public int getHeaderLength() {
        return HEADER_LENGTH;
    }

    @Override
    public int getBodyLength(byte[] header, ByteOrder byteOrder) {
        if (header == null || header.length < getHeaderLength()) {
            return 0;
        }
        ByteBuffer bb = ByteBuffer.wrap(header);
        bb.order(byteOrder);
        return bb.getInt();
    }
}