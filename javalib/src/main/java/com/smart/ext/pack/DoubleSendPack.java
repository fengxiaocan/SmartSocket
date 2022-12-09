package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.nio.ByteBuffer;

public final class DoubleSendPack extends SmartPack {
    private final double body;

    public DoubleSendPack(double body) {
        this.body = body;
    }

    @Override
    public int type() {
        return PackConstants.DOUBLE_PACK_TYPE;
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
