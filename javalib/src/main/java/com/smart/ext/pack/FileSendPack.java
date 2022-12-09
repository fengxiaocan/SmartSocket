package com.smart.ext.pack;

import com.smart.ext.iface.PackConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class FileSendPack extends SmartPack {
    private final byte[] body;

    public FileSendPack(File file) throws IOException {
        body = fileToArray(file);
    }

    public FileSendPack(byte[] array) {
        body = array;
    }

    private byte[] fileToArray(File file) throws IOException {
        final byte[] body;
        ByteArrayOutputStream bao = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            bao = new ByteArrayOutputStream();
            byte[] array = new byte[8192];
            int len;
            while ((len = fis.read(array)) > 0) {
                bao.write(array, 0, len);
                bao.flush();
            }
            body = bao.toByteArray();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bao != null) {
                try {
                    bao.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return body;
    }

    @Override
    public int type() {
        return PackConstants.FILE_PACK_TYPE;
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
