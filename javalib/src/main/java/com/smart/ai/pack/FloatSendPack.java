package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.nio.ByteBuffer;

public final class FloatSendPack extends SmartPack {
    private final float body;
    private int params;

    public FloatSendPack(float body) {
        this.body = body;
    }

    public FloatSendPack(float body, int params) {
        this.body = body;
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public int type() {
        return PackConstants.FLOAT_PACK_TYPE;
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
        buffer.putFloat(body);
    }
}
