package com.smart.ext.parse;

import com.smart.ext.head.Headers;
import com.smart.ext.iface.PackConstants;
import com.smart.ext.pack.StringSendPack;
import com.smart.ext.protocol.SmartReaderProtocol;
import com.smart.socket.core.pojo.OriginalData;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SmartParseData {
    protected final byte[] bodyArrays;
    protected final int type;
    protected final Headers header;

    public SmartParseData(OriginalData data) {
        byte[] bytes = data.getHeadBytes();
        if (bytes.length == SmartReaderProtocol.HEADER_LENGTH) {
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            byte[] bodyOriginal = data.getBodyBytes();
            final int bodyLength = bodyOriginal.length;

            //第一个4位为预留数据的总长度
            int length = buffer.getInt();
            //第二个预留4位为类型
            type = buffer.getInt();
            //第三个预留4位为自定义header的长度
            final int headerLength = buffer.getInt();
            if (headerLength > 0) {
                //第四个可能是header
                byte[] headerByte = Arrays.copyOf(bodyOriginal, headerLength);
                header = Headers.parseHeaders(headerByte);

                bodyArrays = Arrays.copyOfRange(bodyOriginal, headerLength, bodyLength);
            } else {
                header = null;
                bodyArrays = bodyOriginal;
            }
            //最后的才是数据
        } else {
            type = -bytes.length;
            bodyArrays = data.getBodyBytes();
            header = null;
        }
    }

    public int getType() {
        return type;
    }

    public boolean hasHeaders() {
        return header != null && header.size() > 0;
    }

    public Headers getHeaders() {
        return header;
    }

    public String getHeader(String key) {
        if (header != null) {
            return header.getValues(key);
        }
        return null;
    }

    public byte[] getBodyBytes() {
        return bodyArrays;
    }

    public boolean isString() {
        return type == PackConstants.STRING_PACK_TYPE;
    }

    public boolean isPulse() {
        return type == PackConstants.PULSE_PACK_TYPE;
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
        return new String(bodyArrays, StringSendPack.UTF8);
    }

    public int asInt() {
        return ByteBuffer.wrap(bodyArrays).getInt();
    }

    public long asLong() {
        return ByteBuffer.wrap(bodyArrays).getLong();
    }

    public float asFloat() {
        return ByteBuffer.wrap(bodyArrays).getFloat();
    }

    public double asDouble() {
        return ByteBuffer.wrap(bodyArrays).getDouble();
    }

    public char asChar() {
        return ByteBuffer.wrap(bodyArrays).getChar();
    }

}
