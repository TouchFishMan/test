package com.telek.hemsipc.netty.future;

import com.telek.hemsipc.protocol.IResponse;

import java.util.concurrent.*;

public class SyncWriteFuture implements Future<IResponse> {
    private CountDownLatch latch = new CountDownLatch(1);
    private IResponse result;

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
    public IResponse get() throws InterruptedException, ExecutionException {
        latch.wait();
        return result;
    }

    @Override
    public IResponse get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return result;
        }
        return null;
    }

    public void setResult(IResponse result) {
        this.result = result;
        latch.countDown();
    }
}
