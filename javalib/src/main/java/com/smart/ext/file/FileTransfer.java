package com.smart.ext.file;

import java.io.IOException;

public interface FileTransfer {
    void begin() throws IOException;

    int read(byte[] array) throws IOException;

    void finish();
}
