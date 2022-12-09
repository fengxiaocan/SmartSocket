package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.nio.ByteBuffer;

public final class IntSendPack extends SmartPack {
    private final int body;

    public IntSendPack(int body) {
        this.body = body;
    }

    @Override
    public int type() {
        return PackConstants.INT_PACK_TYPE;
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
