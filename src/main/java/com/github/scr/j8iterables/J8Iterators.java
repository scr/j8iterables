package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.Ends;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility methods to extend Iterators with Java 8 Stream.
 *
 * @author scr
 */
public class J8Iterators {
    @VisibleForTesting
    J8Iterators() {
    }

    /**
     * Create a Stream from an iterator.
     *
     * @param iterator The iterator to create a Stream from
     * @param <T>      The type of element
     * @return A Stream object from given iterator.
     */
    @NotNull
    public static <T> Stream<T> toStream(@NotNull Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
    }

    /**
     * Return the first and last elements or {@link Optional#empty()} if {@code Iterators.isEmpty(iterator)}.
     *
     * @param iterator The iterable to get the ends from
     * @param <T>      The type of element in the iterable
     * @return optional {@link Ends} with the first and last of the iterable
     */
    @NotNull
    public static <T> Optional<Ends<T>> ends(@NotNull Iterator<T> iterator) {
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        T first = iterator.next();
        return Optional.of(new Ends<>(first, Iterators.getLast(iterator, first)));
    }
}
