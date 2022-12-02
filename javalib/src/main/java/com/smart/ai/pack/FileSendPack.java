package com.smart.ai.pack;

import com.smart.ai.iface.PackConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class FileSendPack extends SmartPack {
    private final byte[] paramsBody;
    private final byte[] body;
    private final int params;

    public FileSendPack(File file) throws IOException {
        String json = getJson(file.getName(), file.length());
        paramsBody = json.getBytes();
        this.params = paramsBody.length;
        body = fileToArray(file);
    }

    public FileSendPack(File file, String jsonParams) throws IOException {
        paramsBody = jsonParams.getBytes();
        this.params = paramsBody.length;
        body = fileToArray(file);
    }

    public static String getJson(String fileName, long length) {
        StringBuilder builder = new StringBuilder("{\"name\":\"");
        builder.append(fileName);
        builder.append("\",\"length\":\"");
        builder.append(length);
        builder.append("\"}");
        return builder.toString();
    }

    private byte[] fileToArray(File file) throws IOException {
        final byte[] body;
        ByteArrayOutputStream bao = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            bao = new ByteArrayOutputStream();
            byte[] array = new byte[2048];
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
    public int params() {
        return params;
    }

    @Override
    public int bodyLength() {
        return body.length+paramsBody.length;
    }

    @Override
    public void addBody(ByteBuffer buffer) {
        buffer.put(paramsBody);
        buffer.put(body);
    }
}
