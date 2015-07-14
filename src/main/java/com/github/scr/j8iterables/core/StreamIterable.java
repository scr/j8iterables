package com.github.scr.j8iterables.core;

import com.google.common.collect.FluentIterable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Proxy class that converts a stream, which implements all of Iterable's methods to an Iterable.
 *
 * @param <T> The type of elements
 */
public class StreamIterable<T> extends FluentIterable<T> {
    @NotNull
    private final Stream<T> STREAM;

    public StreamIterable(@NotNull Stream<T> stream) {
        STREAM = stream;
    }

    @NotNull
    public Iterator<T> iterator() {
        return STREAM.iterator();
    }

    public void forEach(@NotNull Consumer<? super T> action) {
        STREAM.forEach(action);
    }

    @NotNull
    public Spliterator<T> spliterator() {
        return STREAM.spliterator();
    }
}
