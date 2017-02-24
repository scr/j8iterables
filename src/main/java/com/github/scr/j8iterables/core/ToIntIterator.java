package com.github.scr.j8iterables.core;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.function.ToIntFunction;

/**
 * Map an {@link Iterator} to {@link PrimitiveIterator.OfInt}.
 *
 * @author scr
 */
public class ToIntIterator<T> implements PrimitiveIterator.OfInt {
    private final Iterator<T> backingIterator;
    private final ToIntFunction<T> toIntFunction;

    public ToIntIterator(Iterator<T> backingIterator, ToIntFunction<T> toIntFunction) {
        this.backingIterator = backingIterator;
        this.toIntFunction = toIntFunction;
    }

    @Override
    public int nextInt() {
        return toIntFunction.applyAsInt(backingIterator.next());
    }

    @Override
    public boolean hasNext() {
        return backingIterator.hasNext();
    }
}
