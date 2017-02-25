package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.CloseableSpliterator;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utilities for Streams.
 *
 * @author scr on 2/24/17.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public class J8Streams {
    /**
     * Inspired by
     * <a href="http://doc.akka.io/docs/akka/2.4/scala/stream/stages-overview.html#unfoldResource">akka-stream</a>,
     * obtain a stream from a resource that can be closed when the stream has been consumed.
     *
     * @param resourceSupplier          Supplier of the resource
     * @param resourceSpliteratorMapper Maps the resource to a spliterator
     * @param resourceCloser            Closes the resource
     * @param characteristics           spliterator characteristics
     * @param parallel                  Whether parallel execution is possible
     * @param <T>                       Type of the iteration elements
     * @param <R>                       Type of the resource
     * @return A Stream of T
     */
    @Nonnull
    public static <T, R> Stream<T> unfoldResource(
            Supplier<R> resourceSupplier,
            Function<R, Spliterator<T>> resourceSpliteratorMapper,
            Consumer<R> resourceCloser,
            int characteristics, boolean parallel) {

        return StreamSupport.stream(() -> {
            R resource = resourceSupplier.get();
            Spliterator<T> spliterator = resourceSpliteratorMapper.apply(resource);
            return new CloseableSpliterator<>(resource, resourceCloser, spliterator);
        }, characteristics, parallel);
    }

    /**
     * Inspired by
     * <a href="http://doc.akka.io/docs/akka/2.4/scala/stream/stages-overview.html#unfoldResource">akka-stream</a>,
     * obtain a stream from a {@link Closeable} resource that can be closed when the stream has been consumed.
     *
     * @param resourceSupplier          Supplier of the resource
     * @param resourceSpliteratorMapper Maps the resource to a spliterator
     * @param characteristics           spliterator characteristics
     * @param parallel                  Whether parallel execution is possible
     * @param <T>                       Type of the iteration elements
     * @param <R>                       Type of the resource
     * @return A Stream of T
     * @apiNote If {@link Closeable} throws an {@link java.io.IOException}, an {@link java.io.UncheckedIOException} will be thrown.
     */
    @Nonnull
    public static <T, R extends Closeable> Stream<T> unfoldResource(
            Supplier<R> resourceSupplier,
            Function<R, Spliterator<T>> resourceSpliteratorMapper,
            int characteristics, boolean parallel) {

        return StreamSupport.stream(() -> {
            R resource = resourceSupplier.get();
            Spliterator<T> spliterator = resourceSpliteratorMapper.apply(resource);
            return CloseableSpliterator.ofCloseableResource(resource, spliterator);
        }, characteristics, parallel);
    }
}
