package com.smart.ext.head;

import com.smart.ext.pack.StringSendPack;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Headers {
    private Map<String, String> headMap = new HashMap<>();

    private Headers(Map<String, String> map) {
        this.headMap = map;
    }

    public Headers() {
    }

    public Headers(Headers header) {
        Set<String> strings = header.headMap.keySet();
        for (String key : strings) {
            headMap.put(key, header.headMap.get(key));
        }
    }

    public static Headers parseHeaders(byte[] array) {
        ByteBuffer buffer = ByteBuffer.wrap(array);
        Headers headers = new Headers();
        while (buffer.position() < array.length) {
            int keySize = buffer.getInt();
            byte[] bytes1 = new byte[keySize];
            buffer.get(bytes1);
            String key = new String(bytes1, StringSendPack.UTF8);
            String value = null;
            int valueSize = buffer.getInt();
            if (valueSize > 0) {
                byte[] bytes2 = new byte[valueSize];
                buffer.get(bytes2);
                value = new String(bytes2, StringSendPack.UTF8);
            }
            headers.addHeader(key, value);
        }
        return headers;
    }

    public Headers addHeader(String key, String value) {
        headMap.put(key, value);
        return this;
    }

    public Headers addHeader(String key, int value) {
        headMap.put(key, String.valueOf(value));
        return this;
    }

    public Headers addHeader(String key, long value) {
        headMap.put(key, String.valueOf(value));
        return this;
    }

    public Headers addHeader(String key, boolean value) {
        headMap.put(key, String.valueOf(value));
        return this;
    }

    public Headers addHeader(String key, double value) {
        headMap.put(key, String.valueOf(value));
        return this;
    }

    public Headers addHeader(String key, float value) {
        headMap.put(key, String.valueOf(value));
        return this;
    }

    public Headers addHeaders(Headers header) {
        Set<String> strings = header.headMap.keySet();
        for (String key : strings) {
            headMap.put(key, header.headMap.get(key));
        }
        return this;
    }

    public Headers addHeaders(Map<String, String> header) {
        Set<String> strings = header.keySet();
        for (String key : strings) {
            headMap.put(key, header.get(key));
        }
        return this;
    }

    public Set<String> keySet() {
        return headMap.keySet();
    }

    public Collection<String> getValues() {
        return headMap.values();
    }

    public String getValues(String key) {
        return headMap.get(key);
    }

    public String remove(String key) {
        return headMap.remove(key);
    }

    public boolean containsKey(String key) {
        return headMap.containsKey(key);
    }

    public boolean containsValue(String value) {
        return headMap.containsValue(value);
    }

    public boolean isEmpty() {
        return headMap.isEmpty();
    }

    public int size() {
        return headMap.size();
    }

    public Headers clear() {
        headMap.clear();
        return this;
    }

    public byte[] toArrays() {
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            Set<String> strings = keySet();
            ByteBuffer buffer = ByteBuffer.allocate(4);

            for (String key : strings) {
                byte[] bytes1 = key.getBytes(StringSendPack.UTF8);
                buffer.rewind();
                buffer.putInt(bytes1.length);

                bao.write(buffer.array());
                bao.write(bytes1);

                String values = getValues(key);
                if (values != null) {
                    byte[] bytes2 = values.getBytes(StringSendPack.UTF8);

                    buffer.rewind();
                    buffer.putInt(bytes2.length);
                    bao.write(buffer.array());

                    bao.write(bytes2);
                }
            }
            return bao.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
}
