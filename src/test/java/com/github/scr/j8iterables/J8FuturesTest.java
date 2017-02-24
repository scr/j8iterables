package com.github.scr.j8iterables;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.testng.annotations.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 1/27/17.
 */
@Test
public class J8FuturesTest {
    @Test
    public void testCoverageTrickForUtilityClass() throws Exception {
        assertThat(new J8Futures(), notNullValue());
    }

    @Test
    public void testAsCompletableFutureSuccess() throws Exception {
        SettableFuture<Integer> settableFuture = SettableFuture.create();
        CompletableFuture<Integer> completableFuture = J8Futures.asCompletableFuture(settableFuture);
        assertThat("completableFuture is done too soon", !completableFuture.isDone());
        settableFuture.set(123);
        assertThat("completableFuture isn't done yet", completableFuture.isDone());
        assertThat("completableFuture has exception", !completableFuture.isCompletedExceptionally());
        assertThat("completableFuture was cancelled", !completableFuture.isCancelled());
        assertThat(completableFuture.get(), is(123));
    }

    @Test(expectedExceptions = CancellationException.class)
    public void testAsCompletableFutureCancelled() throws Exception {
        SettableFuture<Integer> settableFuture = SettableFuture.create();
        CompletableFuture<Integer> completableFuture = J8Futures.asCompletableFuture(settableFuture);
        assertThat("completableFuture is done too soon", !completableFuture.isDone());
        settableFuture.cancel(true);
        assertThat("completableFuture isn't done yet", completableFuture.isDone());
        assertThat("completableFuture wasn't cancelled", completableFuture.isCancelled());
        assertThat("completableFuture isn't exceptional", completableFuture.isCompletedExceptionally());

        // Should cause exception
        completableFuture.get();
    }

    @Test
    public void testAsListenableFutureSuccess() throws Exception {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        ListenableFuture<Integer> listenableFuture = J8Futures.asListenableFuture(completableFuture);
        assertThat("listenableFuture is done too soon", !listenableFuture.isDone());
        completableFuture.complete(123);
        assertThat("listenableFuture isn't done yet", listenableFuture.isDone());
        assertThat("listenableFuture was cancelled", !listenableFuture.isCancelled());
        assertThat(listenableFuture.get(), is(123));
    }

    @Test(expectedExceptions = CancellationException.class)
    public void testAsListenableFutureCanceled() throws Exception {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        ListenableFuture<Integer> listenableFuture = J8Futures.asListenableFuture(completableFuture);
        assertThat("listenableFuture is done too soon", !listenableFuture.isDone());
        completableFuture.cancel(true);
        assertThat("listenableFuture isn't done yet", listenableFuture.isDone());
        assertThat("listenableFuture wasn't cancelled", listenableFuture.isCancelled());

        // Should cause exception
        listenableFuture.get();
    }

    @Test(expectedExceptions = ExecutionException.class)
    public void testAsListenableFutureExceptional() throws Exception {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        ListenableFuture<Integer> listenableFuture = J8Futures.asListenableFuture(completableFuture);
        assertThat("listenableFuture is done too soon", !listenableFuture.isDone());
        completableFuture.completeExceptionally(new RuntimeException("Testing"));
        assertThat("listenableFuture isn't done yet", listenableFuture.isDone());

        // Should cause exception
        listenableFuture.get();
    }

    @Test
    public void testOverBackSuccess() throws Exception {
        SettableFuture<Integer> settableFuture = SettableFuture.create();
        CompletableFuture<Integer> completableFuture = J8Futures.asCompletableFuture(settableFuture);
        ListenableFuture<Integer> listenableFuture = J8Futures.asListenableFuture(completableFuture);
        assertThat("listenableFuture is done too soon", !listenableFuture.isDone());
        completableFuture.complete(123);
        assertThat("listenableFuture isn't done yet", listenableFuture.isDone());
        assertThat("listenableFuture was cancelled", !listenableFuture.isCancelled());
        assertThat(listenableFuture.get(), is(123));
    }

    @Test(expectedExceptions = CancellationException.class)
    public void testOverBackCanceled() throws Exception {
        SettableFuture<Integer> settableFuture = SettableFuture.create();
        CompletableFuture<Integer> completableFuture = J8Futures.asCompletableFuture(settableFuture);
        ListenableFuture<Integer> listenableFuture = J8Futures.asListenableFuture(completableFuture);
        assertThat("listenableFuture is done too soon", !listenableFuture.isDone());
        completableFuture.cancel(true);
        assertThat("listenableFuture isn't done yet", listenableFuture.isDone());
        assertThat("listenableFuture wasn't cancelled", listenableFuture.isCancelled());

        // Should cause exception
        listenableFuture.get();
    }

    @Test(expectedExceptions = ExecutionException.class)
    public void testOverBackExceptional() throws Exception {
        SettableFuture<Integer> settableFuture = SettableFuture.create();
        CompletableFuture<Integer> completableFuture = J8Futures.asCompletableFuture(settableFuture);
        ListenableFuture<Integer> listenableFuture = J8Futures.asListenableFuture(completableFuture);
        assertThat("listenableFuture is done too soon", !listenableFuture.isDone());
        completableFuture.completeExceptionally(new RuntimeException("Testing"));
        assertThat("listenableFuture isn't done yet", listenableFuture.isDone());

        // Should cause exception
        listenableFuture.get();
    }
}