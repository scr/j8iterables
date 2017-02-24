package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.Ends;
import com.github.scr.j8iterables.core.PeekIterator;
import com.github.scr.j8iterables.core.PreviousListIterator;
import com.github.scr.j8iterables.core.ToDoubleIterator;
import com.github.scr.j8iterables.core.ToIntIterator;
import com.github.scr.j8iterables.core.ToLongIterator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterators;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility methods to extend Iterators with Java 8 Stream.
 *
 * @author scr
 */
@SuppressWarnings("WeakerAccess")
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
    @Nonnull
    public static <T> Stream<T> toStream(Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
    }

    /**
     * Return the first and last elements or {@link Optional#empty()} if {@code Iterators.isEmpty(iterator)}.
     *
     * @param iterator The iterator to get the ends from
     * @param <T>      The type of element in the iterator
     * @return optional {@link Ends} with the first and last of the iterable
     */
    @Nonnull
    public static <T> Optional<Ends<T>> ends(Iterator<T> iterator) {
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
    @Nonnull
    public static <T> Iterator<T> peek(Iterator<T> iterator, Consumer<? super T> consumer) {
        return new PeekIterator<>(iterator, consumer);
    }

    /**
     * Iterate a {@link ListIterator} in reverse order.
     *
     * @param listIterator the list iterator
     * @param <T>          the type of elements
     * @return an iterator that walks the listIterator backwards
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T> PreviousListIterator<T> reverse(ListIterator<? extends T> listIterator) {
        return new PreviousListIterator<>((ListIterator<T>) listIterator);
    }

    @Nonnull
    public static <T> PrimitiveIterator.OfInt mapToInt(
            Iterator<T> iterator, ToIntFunction<T> toIntFunction) {
        return new ToIntIterator<>(iterator, toIntFunction);
    }

    @Nonnull
    public static <T> PrimitiveIterator.OfLong mapToLong(
            Iterator<T> iterator, ToLongFunction<T> toLongFunction) {
        return new ToLongIterator<>(iterator, toLongFunction);
    }

    @Nonnull
    public static <T> PrimitiveIterator.OfDouble mapToDouble(
            Iterator<T> iterator, ToDoubleFunction<T> toDoubleFunction) {
        return new ToDoubleIterator<>(iterator, toDoubleFunction);
    }
}
