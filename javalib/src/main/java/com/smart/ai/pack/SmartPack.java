package com.smart.ai.pack;

import com.smart.ai.protocol.SmartReaderProtocol;
import com.smart.socket.core.iocore.interfaces.ISendPack;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class SmartPack implements ISendPack {

    public abstract int type();

    public abstract int params();

    public abstract int bodyLength();

    public abstract void addBody(ByteBuffer buffer);

    @Override
    public final byte[] parse() {
        //根据服务器的解析规则,构建byte数组
        int length = bodyLength();
        int headerLength = SmartReaderProtocol.HEADER_LENGTH;
        ByteBuffer bb = ByteBuffer.allocate(headerLength + length);
        bb.order(ByteOrder.BIG_ENDIAN);
        //第一个4位为预留长度
        bb.putInt(length);
        //第二个预留4位为类型
        bb.putInt(type());
        //第三个预留4位为自定义参数
        bb.putInt(params());
        //最后的才是数据
        addBody(bb);

        return bb.array();
    }
}
