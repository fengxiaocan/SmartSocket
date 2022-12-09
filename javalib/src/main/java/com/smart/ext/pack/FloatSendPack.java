package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.nio.ByteBuffer;

public final class FloatSendPack extends SmartPack {
    private final float body;

    public FloatSendPack(float body) {
        this.body = body;
    }

    @Override
    public int type() {
        return PackConstants.FLOAT_PACK_TYPE;
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
