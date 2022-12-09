package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class StringSendPack extends SmartPack {
    public static final Charset UTF8 = Charset.forName("UTF-8");
    private final byte[] body;

    public StringSendPack(String content) {
        this.body = content.getBytes(UTF8);
    }

    @Override
    public int type() {
        return PackConstants.STRING_PACK_TYPE;
    }

    @Override
    public int bodyLength() {
        return body.length;
    }

    @Override
    public void addBody(ByteBuffer buffer) {
        buffer.put(body);
    }
}
