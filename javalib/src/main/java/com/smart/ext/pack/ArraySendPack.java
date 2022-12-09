package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.nio.ByteBuffer;

public final class ArraySendPack extends SmartPack {
    private final byte[] body;

    public ArraySendPack(byte[] body) {
        this.body = body;
    }

    @Override
    public int type() {
        return PackConstants.BYTES_PACK_TYPE;
    }

    @Override
    public int bodyLength() {
        return body.length;
    }

    @Override
    public void addBody(ByteBuffer buffer) {
        buffer.put(body);
    }
}
