package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.nio.ByteBuffer;

public final class LongSendPack extends SmartPack {
    private final long body;
    private int params;

    public LongSendPack(long body) {
        this.body = body;
    }

    public LongSendPack(long body, int params) {
        this.body = body;
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public int type() {
        return PackConstants.LONG_PACK_TYPE;
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
        buffer.putLong(body);
    }
}
