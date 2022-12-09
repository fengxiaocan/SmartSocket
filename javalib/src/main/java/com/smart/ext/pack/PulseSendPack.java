package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;
import com.smart.socket.core.iocore.interfaces.IPulseSender;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class PulseSendPack extends SmartPack implements IPulseSender {
    public static final Charset UTF8 = Charset.forName("UTF-8");
    private final byte[] body;

    public PulseSendPack() {
        this.body = "pulse".getBytes(UTF8);
    }

    public PulseSendPack(String pulse) {
        this.body = pulse.getBytes(UTF8);
    }

    @Override
    public int type() {
        return PackConstants.PULSE_PACK_TYPE;
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
