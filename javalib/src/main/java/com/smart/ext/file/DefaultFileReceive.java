package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DefaultFileReceive implements FileReceive {
    private File dir;
    private FileOutputStream fos;

    public DefaultFileReceive(File dir) {
        this.dir = dir;
    }

    @Override
    public void begin(Headers headers) throws IOException {
        String name = headers.getValues("name");
        fos = new FileOutputStream(new File(dir, name));
    }

    @Override
    public void receive(byte[] array, int len) throws IOException {
        fos.write(array, 0, len);
    }

    @Override
    public void finish() {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
    }
}
