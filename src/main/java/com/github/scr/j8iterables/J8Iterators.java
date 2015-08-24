package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.Ends;
import com.github.scr.j8iterables.core.PeekIterator;
import com.github.scr.j8iterables.core.PreviousListIterator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;
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
     * @param iterator The iterator to get the ends from
     * @param <T>      The type of element in the iterator
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

    /**
     * Peek at the iterable without modifying the result.
     *
     * @param iterator the original iterator
     * @param consumer the consumer to peek at each element
     * @param <T>      the type of element in the iterator
     * @return an iterator which sends every element through consumer before returning to the caller
     */
    public static <T> Iterator<T> peek(@NotNull Iterator<T> iterator, @NotNull Consumer<? super T> consumer) {
        return new PeekIterator<>(iterator, consumer);
    }

    /**
     * Iterate a {@link ListIterator} in reverse order.
     *
     * @param listIterator the list iterator
     * @param <T> the type of elements
     * @return an iterator that walks the listIterator backwards
     */
    @SuppressWarnings("unchecked")
    public static <T> PreviousListIterator<T> reverse(ListIterator<? extends T> listIterator) {
        return new PreviousListIterator<>((ListIterator<T>) listIterator);
    }
}
