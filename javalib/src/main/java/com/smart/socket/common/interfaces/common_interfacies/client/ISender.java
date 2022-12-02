package com.smart.socket.common.interfaces.common_interfacies.client;

import com.smart.socket.core.iocore.interfaces.ISendPack;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface ISender<T> {
    /**
     * 在当前的连接上发送数据
     *
     * @param sender 具有发送能力的Bean {@link ISendPack}
     * @return T
     */
    T send(ISendPack sender);
}
