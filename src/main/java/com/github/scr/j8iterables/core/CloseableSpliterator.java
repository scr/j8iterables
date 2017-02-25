package com.github.scr.j8iterables.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A {@link Spliterator} that closes a resource when it is completed via the {@code resourceCloser} callback.
 *
 * @author scr on 2/24/17.
 * @apiNote Upon close, any {@link java.io.IOException} is converted to {@link java.io.UncheckedIOException}.
 */
@SuppressWarnings("WeakerAccess")
public class CloseableSpliterator<T, R> implements Spliterator<T> {
    private final R resource;
    private final Consumer<R> resourceCloser;
    private final Spliterator<T> backingSpliterator;

    /**
     * Creates a {@link CloseableSpliterator} that invokes the {@link Consumer} with the {@code resource} when complete.
     *
     * @param resource           The resource to close
     * @param resourceCloser     the callback that closes the resource
     * @param backingSpliterator the real {@link Spliterator} to wrap
     */
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

    /**
     * As a convenience for {@link Closeable} resources, creates a {@link CloseableSpliterator} that calls
     * {@link Closeable#close()} on the resource when done.
     *
     * @param resource           The resource to close
     * @param backingSpliterator the real {@link Spliterator} to wrap
     * @param <T>                Type of the elements
     * @param <R>                Type of the resource
     * @return Spliterator that will close {@code resource} when done.
     * @apiNote If {@link Closeable} throws an {@link IOException}, an {@link UncheckedIOException} will be thrown.
     */
    @Nonnull
    public static <T, R extends Closeable> CloseableSpliterator<T, R> ofCloseableResource(
            R resource, Spliterator<T> backingSpliterator) {
        return new CloseableSpliterator<>(resource, r -> {
            try {
                r.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }, backingSpliterator);
    }
}
