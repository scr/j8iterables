package com.github.scr.j8iterables.core;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.ToDoubleFunction;

/**
 * Map an {@link Iterator} to {@link PrimitiveIterator.OfDouble}.
 *
 * @author scr
 */
public class ToDoubleIterator<T> implements PrimitiveIterator.OfDouble {
    private final Iterator<T> backingIterator;
    private final ToDoubleFunction<T> toDoubleFunction;

    public ToDoubleIterator(@NotNull Iterator<T> backingIterator, @NotNull ToDoubleFunction<T> toDoubleFunction) {
        this.backingIterator = backingIterator;
        this.toDoubleFunction = toDoubleFunction;
    }

    @Override
    public double nextDouble() {
        return toDoubleFunction.applyAsDouble(backingIterator.next());
    }

    @Override
    public boolean hasNext() {
        return backingIterator.hasNext();
    }
}
