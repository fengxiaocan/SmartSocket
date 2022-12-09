package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DefaultFileTransfer implements FileTransfer {
    private File file;
    private FileInputStream fis;

    public DefaultFileTransfer(File file) {
        this.file = file;
    }

    public Headers getHeader(){
        Headers headers = new Headers();
        headers.addHeader("name", file.getName());
        return headers;
    }

    @Override
    public void begin() throws IOException {
        fis = new FileInputStream(file);
    }

    @Override
    public int read(byte[] array) throws IOException {
        return fis.read(array);
    }

    @Override
    public void finish() {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
            }
        }
    }
}
