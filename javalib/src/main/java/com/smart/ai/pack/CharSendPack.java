package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.nio.ByteBuffer;

public final class CharSendPack extends SmartPack {
    private final char body;
    private int params;

    public CharSendPack(char body) {
        this.body = body;
    }

    public CharSendPack(char body, int params) {
        this.body = body;
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public int type() {
        return PackConstants.CHAR_PACK_TYPE;
    }

    @Override
    public int params() {
        return params;
    }

    @Override
    public int bodyLength() {
        return 2;
    }

    @Override
    public void addBody(ByteBuffer buffer) {
        buffer.putChar(body);
    }
}
