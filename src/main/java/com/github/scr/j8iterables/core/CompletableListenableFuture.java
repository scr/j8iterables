package com.github.scr.j8iterables.core;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * For converting between Java8' {@link CompletableFuture} and guava's {@link ListenableFuture}.
 *
 * @author scr on 1/27/17.
 */
public class CompletableListenableFuture<T> extends CompletableFuture<T> implements FutureCallback<T> {
    @SuppressWarnings("WeakerAccess")
    public final ListenableFuture<T> delegateListenableFuture;

    public CompletableListenableFuture(@NotNull ListenableFuture<T> delegateListenableFuture) {
        this.delegateListenableFuture = Objects.requireNonNull(delegateListenableFuture);
        Futures.addCallback(delegateListenableFuture, this);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean ret = delegateListenableFuture.cancel(mayInterruptIfRunning);
        super.cancel(mayInterruptIfRunning);
        return ret;
    }

    @Override
    public boolean complete(T value) {
        Preconditions.checkState(delegateListenableFuture instanceof SettableFuture);
        boolean ret = ((SettableFuture<T>) delegateListenableFuture).set(value);
        super.complete(value);
        return ret;
    }

    @Override
    public boolean completeExceptionally(Throwable ex) {
        Preconditions.checkState(delegateListenableFuture instanceof SettableFuture);
        boolean ret = ((SettableFuture<T>) delegateListenableFuture).setException(ex);
        super.completeExceptionally(ex);
        return ret;
    }

    @Override
    public void onSuccess(T t) {
        complete(t);
    }

    @Override
    public void onFailure(Throwable throwable) {
        completeExceptionally(throwable);
    }
}
