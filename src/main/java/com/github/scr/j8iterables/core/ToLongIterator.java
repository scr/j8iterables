package com.github.scr.j8iterables.core;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.PrimitiveIterator.OfLong;
import java.util.function.ToLongFunction;

/**
 * Map an {@link Iterator} to {@link PrimitiveIterator.OfLong}.
 *
 * @author scr
 */
public class ToLongIterator<T> implements OfLong {
    private final Iterator<T> backingIterator;
    private final ToLongFunction<T> toLongFunction;

    public ToLongIterator(@NotNull Iterator<T> backingIterator, @NotNull ToLongFunction<T> toLongFunction) {
        this.backingIterator = backingIterator;
        this.toLongFunction = toLongFunction;
    }

    @Override
    public long nextLong() {
        return toLongFunction.applyAsLong(backingIterator.next());
    }

    @Override
    public boolean hasNext() {
        return backingIterator.hasNext();
    }
}
