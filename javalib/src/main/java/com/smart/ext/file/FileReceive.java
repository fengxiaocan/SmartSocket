package com.smart.ext.file;

import com.smart.ext.head.Headers;

import java.io.IOException;

public interface FileReceive {
    void begin(Headers headers) throws IOException;

    void receive(byte[] array, int len) throws IOException;

    void finish();
}
