package com.smart.socket.server.action;


import com.smart.socket.core.iocore.interfaces.ISendPack;
import com.smart.socket.core.iocore.interfaces.IStateSender;
import com.smart.socket.core.pojo.OriginalData;

import java.io.Serializable;


import static com.smart.socket.server.action.IAction.Client.ACTION_READ_COMPLETE;
import static com.smart.socket.server.action.IAction.Client.ACTION_READ_THREAD_SHUTDOWN;
import static com.smart.socket.server.action.IAction.Client.ACTION_READ_THREAD_START;
import static com.smart.socket.server.action.IAction.Client.ACTION_WRITE_COMPLETE;
import static com.smart.socket.server.action.IAction.Client.ACTION_WRITE_THREAD_SHUTDOWN;
import static com.smart.socket.server.action.IAction.Client.ACTION_WRITE_THREAD_START;

public class ClientActionDispatcher implements IStateSender {

    private ClientActionListener mActionListener;

    public ClientActionDispatcher(ClientActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Override
    public void sendBroadcast(final String action, final Serializable serializable) {
        if (mActionListener == null) {
            return;
        }
        dispatch(action, serializable);
    }

    @Override
    public void sendBroadcast(String action) {
        sendBroadcast(action, null);
    }

    private void dispatch(String action, Serializable serializable) {
        switch (action) {
            case ACTION_READ_THREAD_START: {
                try {
                    mActionListener.onClientReadReady();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_READ_THREAD_SHUTDOWN: {
                try {
                    Exception exception = (Exception) serializable;
                    mActionListener.onClientReadDead(exception);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_WRITE_THREAD_START: {
                try {
                    mActionListener.onClientWriteReady();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_WRITE_THREAD_SHUTDOWN: {
                try {
                    Exception exception = (Exception) serializable;
                    mActionListener.onClientWriteDead(exception);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_READ_COMPLETE: {
                try {
                    OriginalData data = (OriginalData) serializable;
                    mActionListener.onClientRead(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_WRITE_COMPLETE: {
                try {
                    ISendPack data = (ISendPack) serializable;
                    mActionListener.onClientWrite(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public interface ClientActionListener {
        void onClientReadReady();

        void onClientWriteReady();

        void onClientReadDead(Exception e);

        void onClientWriteDead(Exception e);

        void onClientRead(OriginalData originalData);

        void onClientWrite(ISendPack sendable);
    }
}
