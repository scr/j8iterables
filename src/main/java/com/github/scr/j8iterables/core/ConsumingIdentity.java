package com.github.scr.j8iterables.core;

import com.google.common.base.Function;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Utility to enable Stream-like "peek" on an Iterable.
 *
 * @author scr
 */
public class ConsumingIdentity<T> implements Function<T, T>, UnaryOperator<T> {
    @NotNull
    private final Consumer<T> CONSUMER;

    public ConsumingIdentity(@NotNull Consumer<T> consumer) {
        CONSUMER = consumer;
    }

    @Override
    public T apply(T t) {
        CONSUMER.accept(t);
        return t;
    }
}