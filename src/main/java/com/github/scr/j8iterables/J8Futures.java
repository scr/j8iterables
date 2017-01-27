package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.CompletableListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * Created by scr on 1/27/17.
 */
@SuppressWarnings("WeakerAccess")
public class J8Futures {
    public static <T> CompletableFuture<T> asCompletableFuture(ListenableFuture<T> listenableFuture) {
        return new CompletableListenableFuture<T>(listenableFuture);
    }

    public static <T> ListenableFuture<T> asListenableFuture(CompletableFuture<T> completableFuture) {
        if (completableFuture instanceof CompletableListenableFuture) {
            return ((CompletableListenableFuture<T>) completableFuture).delegateListenableFuture;
        }
        SettableFuture<T> ret = SettableFuture.create();
        completableFuture.whenComplete((result, ex) -> {
            if (completableFuture.isCancelled()) {
                ret.cancel(true);
            } else if (ex != null) {
                ret.setException(ex);
            } else {
                ret.set(result);
            }
        });
        return ret;
    }
}
