package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.nio.ByteBuffer;

public final class DoubleSendPack extends SmartPack {
    private final double body;
    private int params;

    public DoubleSendPack(double body) {
        this.body = body;
    }

    public DoubleSendPack(double body, int params) {
        this.body = body;
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public int type() {
        return PackConstants.DOUBLE_PACK_TYPE;
    }

    @Override
    public int params() {
        return params;
    }

    @Override
    public int bodyLength() {
        return 8;
    }

    @Override
    public void addBody(ByteBuffer buffer) {
        buffer.putDouble(body);
    }
}
