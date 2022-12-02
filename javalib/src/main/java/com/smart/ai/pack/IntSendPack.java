package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.nio.ByteBuffer;

public final class IntSendPack extends SmartPack {
    private final int body;
    private int params;

    public IntSendPack(int body) {
        this.body = body;
    }

    public IntSendPack(int body, int params) {
        this.body = body;
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public int type() {
        return PackConstants.INT_PACK_TYPE;
    }

    @Override
    public int params() {
        return params;
    }

    @Override
    public int bodyLength() {
        return 4;
    }

    @Override
    public void addBody(ByteBuffer buffer) {
        buffer.putInt(body);
    }
}
