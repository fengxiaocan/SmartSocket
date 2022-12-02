package com.smart.ai.parse;

import com.smart.ai.iface.PackConstants;
import com.smart.ai.pack.StringSendPack;
import com.smart.ai.protocol.SmartReaderProtocol;
import com.smart.socket.core.pojo.OriginalData;

import java.nio.ByteBuffer;

public class SmartPackParse {
    protected final OriginalData originalData;
    protected final int type;
    protected final int params;

    public SmartPackParse(OriginalData originalData) {
        this.originalData = originalData;
        byte[] bytes = originalData.getHeadBytes();
        if (bytes.length == SmartReaderProtocol.HEADER_LENGTH) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            int length = buffer.getInt();
            type = buffer.getInt();
            params = buffer.getInt();
        } else {
            type = -bytes.length;
            params = -1;
        }
    }

    public int getType() {
        return type;
    }

    public int getParams() {
        return params;
    }

    public byte[] getBodyBytes() {
        return originalData.getBodyBytes();
    }

    public boolean isString() {
        return type == PackConstants.STRING_PACK_TYPE;
    }

    public boolean isBytes() {
        return type == PackConstants.BYTES_PACK_TYPE;
    }

    public boolean isInt() {
        return type == PackConstants.INT_PACK_TYPE;
    }

    public boolean isLong() {
        return type == PackConstants.LONG_PACK_TYPE;
    }

    public boolean isFloat() {
        return type == PackConstants.FLOAT_PACK_TYPE;
    }

    public boolean isDouble() {
        return type == PackConstants.DOUBLE_PACK_TYPE;
    }

    public boolean isChar() {
        return type == PackConstants.CHAR_PACK_TYPE;
    }

    public boolean isFile() {
        return type == PackConstants.FILE_PACK_TYPE;
    }

    public String asString() {
        return new String(originalData.getBodyBytes(), StringSendPack.UTF8);
    }

    public int asInt() {
        return ByteBuffer.wrap(originalData.getBodyBytes()).getInt();
    }

    public long asLong() {
        return ByteBuffer.wrap(originalData.getBodyBytes()).getLong();
    }

    public float asFloat() {
        return ByteBuffer.wrap(originalData.getBodyBytes()).getFloat();
    }

    public double asDouble() {
        return ByteBuffer.wrap(originalData.getBodyBytes()).getDouble();
    }

    public char asChar() {
        return ByteBuffer.wrap(originalData.getBodyBytes()).getChar();
    }

}
