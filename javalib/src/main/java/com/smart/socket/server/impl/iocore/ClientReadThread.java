package com.smart.socket.server.impl.iocore;

import com.smart.socket.common.interfaces.basic.AbsLoopThread;
import com.smart.socket.core.iocore.interfaces.IReader;
import com.smart.socket.core.iocore.interfaces.IStateSender;
import com.smart.socket.core.utils.SLog;
import com.smart.socket.server.action.IAction;
import com.smart.socket.server.exceptions.InitiativeDisconnectException;

import java.io.IOException;

/**
 * Created by xuhao on 2017/5/17.
 */

public class ClientReadThread extends AbsLoopThread {
    private IStateSender mClientStateSender;

    private IReader mReader;

    public ClientReadThread(IReader reader, IStateSender clientStateSender) {
        super("server_client_read_thread");
        this.mClientStateSender = clientStateSender;
        this.mReader = reader;
    }

    @Override
    protected void beforeLoop() {
        mClientStateSender.sendBroadcast(IAction.Client.ACTION_READ_THREAD_START);
    }

    @Override
    protected void runInLoopThread() throws IOException {
        mReader.read();
    }

    @Override
    public synchronized void shutdown(Exception e) {
        mReader.close();
        super.shutdown(e);
    }

    @Override
    protected void loopFinish(Exception e) {
        e = e instanceof InitiativeDisconnectException ? null : e;
        if (e != null) {
            SLog.e("duplex read error,thread is dead with exception:" + e.getMessage());
        }
        mClientStateSender.sendBroadcast(IAction.Client.ACTION_READ_THREAD_SHUTDOWN, e);
    }
}
