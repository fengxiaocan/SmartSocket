package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.nio.ByteBuffer;

public final class LongSendPack extends SmartPack {
    private final long body;

    public LongSendPack(long body) {
        this.body = body;
    }

    @Override
    public int type() {
        return PackConstants.LONG_PACK_TYPE;
    }

    @Override
    public int bodyLength() {
        return 8;
    }

    @Override
    public void addBody(ByteBuffer buffer) {
        buffer.putLong(body);
    }
}
