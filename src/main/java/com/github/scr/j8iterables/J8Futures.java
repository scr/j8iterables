package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.CompletableListenableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * For converting between Java8' {@link CompletableFuture} and guava's {@link ListenableFuture}.
 *
 * @author scr on 1/27/17.
 */
@SuppressWarnings("WeakerAccess")
public class J8Futures {
    /**
     * Convert a {@link ListenableFuture} to a {@link CompletableFuture}.
     *
     * @param listenableFuture The Guava listenable future to convert
     * @param <T>              The value type of the future
     * @return a Java8 completable future
     */
    public static <T> CompletableFuture<T> asCompletableFuture(ListenableFuture<T> listenableFuture) {
        return new CompletableListenableFuture<T>(listenableFuture);
    }

    /**
     * Convert a {@link CompletableFuture} to a {@link ListenableFuture}.
     *
     * @param completableFuture The Java8 completable future to convert
     * @param <T>               The value type of the future
     * @return a Guava listenable future
     */
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
