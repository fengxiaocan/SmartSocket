package com.smart.socket.server.impl.clientpojo;

import com.smart.socket.common.interfaces.common_interfacies.server.IClient;
import com.smart.socket.common.interfaces.common_interfacies.server.IClientPool;
import com.smart.socket.core.iocore.interfaces.ISendPack;
import com.smart.socket.server.exceptions.CacheException;

public class ClientPoolImpl extends AbsClientPool<String, IClient> implements IClientPool<IClient, String> {

    public ClientPoolImpl(int capacity) {
        super(capacity);
    }

    @Override
    public void cache(IClient client) {
        super.set(client.getUniqueTag(), client);
    }

    @Override
    public IClient findByUniqueTag(String tag) {
        return get(tag);
    }

    public void unCache(IClient iClient) {
        remove(iClient.getUniqueTag());
    }

    public void unCache(String key) {
        remove(key);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public void sendToAll(final ISendPack sendPack) {
        echoRun(new Echo<String, IClient>() {
            @Override
            public void onEcho(String key, IClient value) {
                value.send(sendPack);
            }
        });
    }

    public void serverDown(){
        echoRun(new Echo<String, IClient>(){
            @Override
            public void onEcho(String key, IClient value) {
                value.disconnect();
            }
        });
        removeAll();
    }

    @Override
    void onCacheFull(String key, IClient lastOne) {
        lastOne.disconnect(new CacheException("cache is full,you need remove"));
        unCache(lastOne);
    }

    @Override
    void onCacheDuplicate(String key, IClient oldOne) {
        oldOne.disconnect(new CacheException("there are cached in this server.it need removed before new cache"));
        unCache(oldOne);
    }

    @Override
    public void onCacheEmpty() {
        //do nothing
    }
}
