package com.smart.socket.client.impl.client;

import com.smart.socket.core.iocore.interfaces.IPulseSender;
import com.smart.socket.client.impl.exceptions.DogDeadException;
import com.smart.socket.client.sdk.client.OkSocketOptions;
import com.smart.socket.client.sdk.client.bean.IPulse;
import com.smart.socket.client.sdk.client.connection.IConnectionManager;
import com.smart.socket.common.interfaces.basic.AbsLoopThread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuhao on 2017/5/18.
 */

public class PulseManager implements IPulse {
    /**
     * 数据包发送器
     */
    private volatile IConnectionManager mManager;
    /**
     * 心跳数据包
     */
    private IPulseSender mSender;
    /**
     * 连接参数
     */
    private volatile OkSocketOptions mOkOptions;
    /**
     * 当前频率
     */
    private volatile long mCurrentFrequency;
    /**
     * 当前的线程模式
     */
    private volatile OkSocketOptions.IOThreadMode mCurrentThreadMode;
    /**
     * 是否死掉
     */
    private volatile boolean isDead = false;
    /**
     * 允许遗漏的次数
     */
    private volatile AtomicInteger mLoseTimes = new AtomicInteger(-1);

    private PulseThread mPulseThread = new PulseThread();

    PulseManager(IConnectionManager manager, OkSocketOptions okOptions) {
        mManager = manager;
        mOkOptions = okOptions;
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
    }

    public synchronized IPulse setPulseSender(IPulseSender sender) {
        if (sender != null) {
            mSender = sender;
        }
        return this;
    }

    public IPulseSender getPulseSender() {
        return mSender;
    }

    @Override
    public synchronized void pulse() {
        privateDead();
        updateFrequency();
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX) {
            if (mPulseThread.isShutdown()) {
                mPulseThread.start();
            }
        }
    }

    @Override
    public synchronized void trigger() {
        if (isDead) {
            return;
        }
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX && mManager != null && mSender != null) {
            mManager.send(mSender);
        }
    }

    public synchronized void dead() {
        mLoseTimes.set(0);
        isDead = true;
        privateDead();
    }

    private synchronized void updateFrequency() {
        if (mCurrentThreadMode != OkSocketOptions.IOThreadMode.SIMPLEX) {
            mCurrentFrequency = mOkOptions.getPulseFrequency();
            mCurrentFrequency = mCurrentFrequency < 1000 ? 1000 : mCurrentFrequency;//间隔最小为一秒
        } else {
            privateDead();
        }
    }

    @Override
    public synchronized void feed() {
        mLoseTimes.set(-1);
    }

    private void privateDead() {
        if (mPulseThread != null) {
            mPulseThread.shutdown();
        }
    }

    public int getLoseTimes() {
        return mLoseTimes.get();
    }

    protected synchronized void setOkOptions(OkSocketOptions okOptions) {
        mOkOptions = okOptions;
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
        updateFrequency();
    }

    private class PulseThread extends AbsLoopThread {

        @Override
        protected void runInLoopThread() throws Exception {
            if (isDead) {
                shutdown();
                return;
            }
            if (mManager != null && mSender != null) {
                if (mOkOptions.getPulseFeedLoseTimes() != -1 && mLoseTimes.incrementAndGet() >= mOkOptions.getPulseFeedLoseTimes()) {
                    mManager.disconnect(new DogDeadException("you need feed dog on time,otherwise he will die"));
                } else {
                    mManager.send(mSender);
                }
            }

            //not safety sleep.
            Thread.sleep(mCurrentFrequency);
        }

        @Override
        protected void loopFinish(Exception e) {
        }
    }


}
