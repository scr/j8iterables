package com.github.scr.j8iterables.core;

import javax.annotation.Nullable;
import java.util.Spliterator;
import java.util.function.LongConsumer;
import java.util.function.ToLongFunction;

/**
 * Map a {@link Spliterator} to {@link Spliterator.OfLong}.
 *
 * @author scr
 */
public class ToLongSpliterator<T> implements Spliterator.OfLong {
    private final Spliterator<T> backingSpliterator;
    private final ToLongFunction<T> toLongFunction;

    public ToLongSpliterator(Spliterator<T> backingSpliterator, ToLongFunction<T> toLongFunction) {
        this.backingSpliterator = backingSpliterator;
        this.toLongFunction = toLongFunction;
    }

    @Override
    @Nullable
    public OfLong trySplit() {
        Spliterator<T> newSpliterator = backingSpliterator.trySplit();
        if (newSpliterator == null) {
            return null;
        }
        return new ToLongSpliterator<>(newSpliterator, toLongFunction);
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
    public boolean tryAdvance(LongConsumer action) {
        return backingSpliterator.tryAdvance(t -> action.accept(toLongFunction.applyAsLong(t)));
    }
}
