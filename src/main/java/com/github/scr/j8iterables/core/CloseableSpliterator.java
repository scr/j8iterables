package com.github.scr.j8iterables.core;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A {@link Spliterator} that closes a {@link Closeable} resource when it is completed.
 *
 * @author scr on 2/24/17.
 * @apiNote Upon close, any {@link java.io.IOException} is converted to {@link java.io.UncheckedIOException}.
 */
public class CloseableSpliterator<T, R> implements Spliterator<T> {
    private final R resource;
    private final Consumer<R> resourceCloser;
    private final Spliterator<T> backingSpliterator;

    public CloseableSpliterator(R resource, Consumer<R> resourceCloser, Spliterator<T> backingSpliterator) {
        this.resource = resource;
        this.resourceCloser = resourceCloser;
        this.backingSpliterator = backingSpliterator;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        boolean ret = backingSpliterator.tryAdvance(action);
        if (!ret) {
            resourceCloser.accept(resource);
        }
        return ret;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        backingSpliterator.forEachRemaining(action);
        resourceCloser.accept(resource);
    }

    @Override
    @Nullable
    public Spliterator<T> trySplit() {
        return backingSpliterator.trySplit();
    }

    @Override
    public long estimateSize() {
        return backingSpliterator.estimateSize();
    }

    @Override
    public long getExactSizeIfKnown() {
        return backingSpliterator.getExactSizeIfKnown();
    }

    @Override
    public int characteristics() {
        return backingSpliterator.characteristics();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return backingSpliterator.hasCharacteristics(characteristics);
    }

    @Override
    @Nullable
    public Comparator<? super T> getComparator() {
        return backingSpliterator.getComparator();
    }
}
