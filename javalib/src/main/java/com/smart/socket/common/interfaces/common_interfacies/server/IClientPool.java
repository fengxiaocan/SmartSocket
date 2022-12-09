package com.smart.socket.common.interfaces.common_interfacies.server;


import com.smart.socket.core.iocore.interfaces.ISendPack;

public interface IClientPool<T, K> {

    void cache(T t);

    T findByUniqueTag(K key);

    int size();

    void sendToAll(ISendPack sendPack);
}
