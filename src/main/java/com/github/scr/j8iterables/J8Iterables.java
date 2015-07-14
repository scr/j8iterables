package com.github.scr.j8iterables;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility methods to extend Guava Iterables with Java 8 Stream-like classes such as Collectors.
 * <p/>
 * Created by scr on 7/12/15.
 */
public class J8Iterables {
    /**
     * Proxy class that converts a stream, which implements all of Iterable's methods to an Iterable.
     *
     * @param <T> The type of elements
     */
    static class StreamIterable<T> implements Iterable<T> {
        @NotNull
        private final Stream<T> STREAM;

        StreamIterable(@NotNull Stream<T> stream) {
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

    /**
     * Collect iterable of iterables into a mutable container.
     *
     * @param iterables   The iterable of iterables
     * @param supplier    The container supplier
     * @param accumulator The accumulator function
     * @param combiner    The combiner function
     * @param <T>         The type of elements
     * @param <R>         The return type
     * @return Collected result
     * @see Stream#collect(Supplier, BiConsumer, BiConsumer)
     */
    public static <T, R> R collect(@NotNull Iterable<Iterable<T>> iterables,
                                   @NotNull Supplier<R> supplier,
                                   @NotNull BiConsumer<R, ? super T> accumulator,
                                   @NotNull BiConsumer<R, R> combiner) {
        R result = supplier.get();
        for (Iterable<T> iterable : iterables) {
            R innerResult = supplier.get();
            for (T element : iterable) {
                accumulator.accept(innerResult, element);
            }
            combiner.accept(result, innerResult);
        }
        return result;
    }

    /**
     * Collect iterable into a mutable container.
     *
     * @param iterable  The iterable
     * @param collector The collector object
     * @param <T>       The type of elements
     * @param <A>       The type of accumulator
     * @param <R>       The return type
     * @return Collected result
     * @see Stream#collect(Collector)
     */
    public static <T, A, R> R collect(@NotNull Iterable<T> iterable, @NotNull Collector<? super T, A, R> collector) {
        A container = collector.supplier().get();
        BiConsumer<A, ? super T> accumulator = collector.accumulator();
        for (T t : iterable) {
            accumulator.accept(container, t);
        }
        return collector.finisher().apply(container);
    }

    /**
     * Perform a reduction on iterable.
     *
     * @param iterable    The iterable
     * @param accumulator The accumulator function
     * @param <T>         The type of elements
     * @return The reduced result
     * @see Stream#reduce(BinaryOperator)
     */
    public static <T> Optional<T> reduce(@NotNull Iterable<T> iterable, @NotNull BinaryOperator<T> accumulator) {
        boolean foundAny = false;
        T result = null;
        for (T element : iterable) {
            if (!foundAny) {
                foundAny = true;
                result = element;
            } else
                result = accumulator.apply(result, element);
        }
        return foundAny ? Optional.of(result) : Optional.empty();
    }

    /**
     * Perform a reduction on an iterable.
     *
     * @param iterable    The iterable
     * @param identity    The identity to start reduction with
     * @param accumulator The accumulator function
     * @param <T>         The type of elements
     * @return The reduced result
     * @see Stream#reduce(Object, BinaryOperator)
     */
    public static <T> T reduce(@NotNull Iterable<T> iterable,
                               T identity,
                               @NotNull BinaryOperator<T> accumulator) {
        T result = identity;
        for (T element : iterable) {
            result = accumulator.apply(result, element);
        }
        return result;
    }

    /**
     * Perform a reduction on an iterable of iterables.
     *
     * @param iterables   An iterator of iterables.
     * @param identity    The identity to start reduction with
     * @param accumulator The accumulator function
     * @param combiner    The combiner function
     * @param <T>         The type of elements
     * @param <U>         The reduced type
     * @return The resduced result
     * @see Stream#reduce(Object, BiFunction, BinaryOperator)
     */
    public static <T, U> U reduce(@NotNull Iterable<Iterable<T>> iterables,
                                  U identity,
                                  @NotNull BiFunction<U, ? super T, U> accumulator,
                                  @NotNull BinaryOperator<U> combiner) {
        U result = identity;
        for (Iterable<T> iterable : iterables) {
            U innerResult = identity;
            for (T element : iterable) {
                innerResult = accumulator.apply(innerResult, element);
            }
            result = combiner.apply(result, innerResult);
        }
        return result;
    }

    /**
     * Create a one-time Iterable from a Stream.
     *
     * @param stream The Stream to use in creating an Iterable
     * @param <T>    The type of elements
     * @return Iterable from the given stream
     */
    @NotNull
    public static <T> StreamIterable<T> fromStream(@NotNull Stream<T> stream) {
        return new StreamIterable<>(stream);
    }

    /**
     * Create a Stream from the given Iterable.
     *
     * @param iterable The Iterable to use in creating a Stream
     * @param <T>      The type of elements
     * @return Stream from the given iterable
     */
    @NotNull
    public static <T> Stream<T> toStream(@NotNull Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<T>) iterable).stream();
        }
        // TODO(scr): Is it possible to do late-binding (iterable::spliterator)? Need to know characteristics.
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
