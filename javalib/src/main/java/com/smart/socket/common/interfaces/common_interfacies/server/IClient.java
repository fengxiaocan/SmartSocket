package com.smart.socket.common.interfaces.common_interfacies.server;

import com.smart.socket.core.protocol.IReaderProtocol;
import com.smart.socket.common.interfaces.common_interfacies.client.IDisConnectable;
import com.smart.socket.common.interfaces.common_interfacies.client.ISender;

import java.io.Serializable;

public interface IClient extends IDisConnectable, ISender<IClient>, Serializable {

    String getHostIp();

    String getHostName();

    String getUniqueTag();

    void setReaderProtocol(IReaderProtocol protocol);

    void addIOCallback(IClientIOCallback clientIOCallback);

    void removeIOCallback(IClientIOCallback clientIOCallback);

    void removeAllIOCallback();

}
