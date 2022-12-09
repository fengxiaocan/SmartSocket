package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DefaultFileTransferProtocol implements FileTransferProtocol {

    @Override
    public int protocolVersion() {
        return 1;
    }

    @Override
    public void begin() {

    }

    @Override
    public void writeHeaders(OutputStream os, Headers headers) throws IOException {
        if (headers != null && headers.size() > 0) {
            byte[] headerArrays = headers.toArrays();
            //写入长度
            BaseTransfer.writeInt(os, headerArrays.length);
            os.write(headerArrays);
            os.flush();
        } else {
            //写入长度
            BaseTransfer.writeInt(os, 0);
        }
    }

    @Override
    public Headers readHeaders(InputStream is) throws IOException {
        int headerLength = BaseTransfer.readInt(is);
        System.err.println("readHeaders:" + headerLength);
        Headers header = null;
        if (headerLength > 0) {
            byte[] bytes = new byte[headerLength];
            is.read(bytes);
            header = Headers.parseHeaders(bytes);
        }
        return header;
    }

    @Override
    public void finish() {

    }
}
