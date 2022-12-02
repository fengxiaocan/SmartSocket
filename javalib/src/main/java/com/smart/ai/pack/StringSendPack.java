package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class StringSendPack extends SmartPack {
    public static final Charset UTF8 = Charset.forName("UTF-8");
    private final byte[] body;
    private int params;

    public StringSendPack(String content) {
        this.body = content.getBytes(UTF8);
    }

    public StringSendPack(String content, int params) {
        this.body = content.getBytes(UTF8);
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public int type() {
        return PackConstants.STRING_PACK_TYPE;
    }

    @Override
    public int params() {
        return params;
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
