package com.github.scr.j8iterables.core;

import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;

/**
 * Map a {@link Spliterator} to {@link Spliterator.OfInt}.
 *
 * @author scr
 */
public class ToIntSpliterator<T> implements Spliterator.OfInt {
    private final Spliterator<T> backingSpliterator;
    private final ToIntFunction<T> toIntFunction;

    public ToIntSpliterator(Spliterator<T> backingSpliterator, ToIntFunction<T> toIntFunction) {
        this.backingSpliterator = backingSpliterator;
        this.toIntFunction = toIntFunction;
    }

    @Override
    public OfInt trySplit() {
        Spliterator<T> newSpliterator = backingSpliterator.trySplit();
        if (newSpliterator == null) {
            return null;
        }
        return new ToIntSpliterator<>(newSpliterator, toIntFunction);
    }

    @Override
    public long estimateSize() {
        return backingSpliterator.estimateSize();
    }

    @Override
    public int characteristics() {
        return backingSpliterator.characteristics();
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        return backingSpliterator.tryAdvance(t -> action.accept(toIntFunction.applyAsInt(t)));
    }
}
