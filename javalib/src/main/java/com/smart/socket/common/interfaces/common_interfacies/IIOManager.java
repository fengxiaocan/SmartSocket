package com.smart.socket.common.interfaces.common_interfacies;


import com.smart.socket.core.iocore.interfaces.IIOCoreOptions;
import com.smart.socket.core.iocore.interfaces.ISendPack;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IIOManager<E extends IIOCoreOptions> {
    void startEngine();

    void setOkOptions(E options);

    void send(ISendPack sendable);

    void close();

    void close(Exception e);

}
