package com.telek.hemsipc.protocal3761.netty;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.telek.hemsipc.protocal3761.protocal.Packet;


public class SyncWriteFuture implements Future<Packet> {
    private CountDownLatch latch = new CountDownLatch(1);
    private Packet result;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Packet get() throws InterruptedException {
        latch.wait();
        return result;
    }

    @Override
    public Packet get(long timeout, TimeUnit unit) throws InterruptedException {
        if (latch.await(timeout, unit)) {
            return result;
        }
        return null;
    }

    public void setResult(Packet result) {
        this.result = result;
        latch.countDown();
    }
}
