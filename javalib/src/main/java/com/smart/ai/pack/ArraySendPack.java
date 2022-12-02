package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.nio.ByteBuffer;

public final class ArraySendPack extends SmartPack {
    private final byte[] body;
    private int params;

    public ArraySendPack(byte[] body) {
        this.body = body;
    }

    public ArraySendPack(byte[] body, int params) {
        this.body = body;
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public int type() {
        return PackConstants.BYTES_PACK_TYPE;
    }

    @Override
    public int params() {
        return params;
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
