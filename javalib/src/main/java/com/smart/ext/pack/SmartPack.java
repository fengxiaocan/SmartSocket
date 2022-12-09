package com.smart.ext.pack;

import com.smart.ext.head.Headers;
import com.smart.ext.protocol.SmartReaderProtocol;
import com.smart.socket.core.iocore.interfaces.ISendPack;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class SmartPack extends HeaderPack implements ISendPack {

    public abstract int type();

    public abstract int bodyLength();

    public abstract void addBody(ByteBuffer buffer);

    @Override
    public final byte[] parse() {
        //根据服务器的解析规则,构建byte数组
        Headers header = this.header;
        byte[] headerBytes = null;
        int headerArrayLength = 0;
        if (header != null && !header.isEmpty()) {
            headerBytes = header.toArrays();
            headerArrayLength = headerBytes.length;
        }
        int length = bodyLength();

        int capacity = SmartReaderProtocol.HEADER_LENGTH + headerArrayLength + length;
        ByteBuffer bb = ByteBuffer.allocate(capacity);
        //指定顺序
        bb.order(ByteOrder.BIG_ENDIAN);
        //第一个4位为预留数据的总长度
        bb.putInt(length);
        //第二个预留4位为类型
        bb.putInt(type());
        //第三个预留4位为自定义header的长度
        bb.putInt(headerArrayLength);
        //添加header
        if (headerArrayLength>0) {
            bb.put(headerBytes);
        }
        //最后的才是数据
        addBody(bb);
        return bb.array();
    }
}
