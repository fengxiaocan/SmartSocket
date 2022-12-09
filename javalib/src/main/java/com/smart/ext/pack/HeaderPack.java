package com.smart.ext.pack;

import com.smart.ext.head.Headers;

import java.util.Map;

class HeaderPack {
    protected Headers header;

    private Headers getHeader() {
        if (header == null) {
            header = new Headers();
        }
        return header;
    }

    public void setHeader(Headers header) {
        this.header = header;
    }

    public void addHeader(String key, String value) {
        getHeader().addHeader(key, value);
    }


    public void addHeader(String key, int value) {
        getHeader().addHeader(key, value);
    }

    public void addHeader(String key, long value) {
        getHeader().addHeader(key, value);
    }

    public void addHeader(String key, boolean value) {
        getHeader().addHeader(key, value);
    }

    public void addHeader(String key, double value) {
        getHeader().addHeader(key, value);
    }

    public void addHeader(String key, float value) {
        getHeader().addHeader(key, value);
    }

    public void addHeaders(Headers header) {
        getHeader().addHeaders(header);
    }

    public void addHeaders(Map<String, String> header) {
        getHeader().addHeaders(header);
    }

    public String remove(String key) {
        if (header != null) {
            return header.remove(key);
        }
        return null;
    }

    public boolean containsKey(String key) {
        if (header != null) {
            return header.containsKey(key);
        }
        return false;
    }

    public boolean containsValue(String value) {
        if (header != null) {
            return header.containsValue(value);
        }
        return false;
    }

    public boolean isEmpty() {
        if (header != null) {
            return header.isEmpty();
        }
        return true;
    }

    public int size() {
        if (header != null) {
            return header.size();
        }
        return 0;
    }

    public void clear() {
        if (header != null) {
            header.clear();
        }
    }

}
