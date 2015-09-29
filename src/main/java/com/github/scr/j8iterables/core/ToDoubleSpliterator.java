package com.github.scr.j8iterables.core;

import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.ToDoubleFunction;

/**
 * Map a {@link Spliterator} to {@link Spliterator.OfDouble}.
 *
 * @author scr
 */
public class ToDoubleSpliterator<T> implements Spliterator.OfDouble {
    private final Spliterator<T> backingSpliterator;
    private final ToDoubleFunction<T> toDoubleFunction;

    public ToDoubleSpliterator(Spliterator<T> backingSpliterator, ToDoubleFunction<T> toDoubleFunction) {
        this.backingSpliterator = backingSpliterator;
        this.toDoubleFunction = toDoubleFunction;
    }

    @Override
    public OfDouble trySplit() {
        Spliterator<T> newSpliterator = backingSpliterator.trySplit();
        if (newSpliterator == null) {
            return null;
        }
        return new ToDoubleSpliterator<>(newSpliterator, toDoubleFunction);
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
    public boolean tryAdvance(DoubleConsumer action) {
        return backingSpliterator.tryAdvance(t -> action.accept(toDoubleFunction.applyAsDouble(t)));
    }
}
