package com.smart.socket.core.iocore;

import com.smart.socket.core.exceptions.WriteException;
import com.smart.socket.core.iocore.interfaces.IIOCoreOptions;
import com.smart.socket.core.iocore.interfaces.IOAction;
import com.smart.socket.core.iocore.interfaces.IPulseSender;
import com.smart.socket.core.iocore.interfaces.ISendPack;
import com.smart.socket.core.iocore.interfaces.IStateSender;
import com.smart.socket.core.iocore.interfaces.IWriter;
import com.smart.socket.core.utils.BytesUtils;
import com.smart.socket.core.utils.SLog;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xuhao on 2017/5/31.
 */

public class WriterImpl implements IWriter<IIOCoreOptions> {

    private volatile IIOCoreOptions mOkOptions;

    private IStateSender mStateSender;

    private OutputStream mOutputStream;

    private LinkedBlockingQueue<ISendPack> mQueue = new LinkedBlockingQueue<>();

    @Override
    public void initialize(OutputStream outputStream, IStateSender stateSender) {
        mStateSender = stateSender;
        mOutputStream = outputStream;
    }

    @Override
    public boolean write() throws RuntimeException {
        ISendPack sendable = null;
        try {
            sendable = mQueue.take();
        } catch (InterruptedException e) {
            //ignore;
        }

        if (sendable != null) {
            try {
                byte[] sendBytes = sendable.parse();
                int packageSize = mOkOptions.getWritePackageBytes();
                int remainingCount = sendBytes.length;
                ByteBuffer writeBuf = ByteBuffer.allocate(packageSize);
                writeBuf.order(mOkOptions.getWriteByteOrder());
                int index = 0;
                while (remainingCount > 0) {
                    int realWriteLength = Math.min(packageSize, remainingCount);
                    writeBuf.clear();
                    writeBuf.rewind();
                    writeBuf.put(sendBytes, index, realWriteLength);
                    writeBuf.flip();

                    byte[] writeArr = new byte[realWriteLength];
                    writeBuf.get(writeArr);
                    mOutputStream.write(writeArr);
                    mOutputStream.flush();

                    if (SLog.isDebug()) {
                        byte[] forLogBytes = Arrays.copyOfRange(sendBytes, index, index + realWriteLength);
                        SLog.i("write bytes: " + BytesUtils.toHexStringForLog(forLogBytes));
                        SLog.i("bytes write length:" + realWriteLength);
                    }

                    index += realWriteLength;
                    remainingCount -= realWriteLength;
                }
                if (sendable instanceof IPulseSender) {
                    mStateSender.sendBroadcast(IOAction.ACTION_PULSE_REQUEST, sendable);
                } else {
                    mStateSender.sendBroadcast(IOAction.ACTION_WRITE_COMPLETE, sendable);
                }
            } catch (Exception e) {
                WriteException writeException = new WriteException(e);
                throw writeException;
            }
            return true;
        }
        return false;
    }

    @Override
    public void setOption(IIOCoreOptions option) {
        mOkOptions = option;
    }

    @Override
    public void offer(ISendPack sender) {
        mQueue.offer(sender);
    }

    @Override
    public void close() {
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }


}
