package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.nio.ByteBuffer;

public final class CharSendPack extends SmartPack {
    private final char body;

    public CharSendPack(char body) {
        this.body = body;
    }

    @Override
    public int type() {
        return PackConstants.CHAR_PACK_TYPE;
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
