package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.ConsumingIdentity;
import com.github.scr.j8iterables.core.Ends;
import com.github.scr.j8iterables.core.J8PrimitiveIterable;
import com.github.scr.j8iterables.core.PeekIterator;
import com.github.scr.j8iterables.core.StreamIterable;
import com.github.scr.j8iterables.core.SupplierIterable;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility methods to extend Guava Iterables with Java 8 Stream-like classes such as Collectors.
 *
 * @author scr
 */
@SuppressWarnings({"WeakerAccess", "Guava"})
public class J8Iterables {
    /**
     * The empty iterable to be shared across calls to {@link #emptyIterable()}.
     */
    public static final FluentIterable EMPTY_ITERABLE = fromSupplier(Collections::emptyIterator);

    @VisibleForTesting
    J8Iterables() {
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
    @Nullable
    public static <T, R> R collect(Iterable<Iterable<T>> iterables,
                                   Supplier<R> supplier,
                                   BiConsumer<R, ? super T> accumulator,
                                   BiConsumer<R, R> combiner) {
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
    @Nullable
    public static <T, A, R> R collect(Iterable<T> iterable, Collector<? super T, A, R> collector) {
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
    @Nullable
    public static <T> Optional<T> reduce(Iterable<T> iterable, BinaryOperator<T> accumulator) {
        boolean foundAny = false;
        T result = null;
        for (T element : iterable) {
            if (!foundAny) {
                foundAny = true;
                result = element;
            } else {
                result = accumulator.apply(result, element);
            }
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
    @Nullable
    public static <T> T reduce(Iterable<T> iterable,
                               @SuppressWarnings("SameParameterValue") @Nullable T identity,
                               BinaryOperator<T> accumulator) {
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
     * @return The reduced result
     * @see Stream#reduce(Object, BiFunction, BinaryOperator)
     */
    @Nullable
    public static <T, U> U reduce(Iterable<Iterable<T>> iterables,
                                  @SuppressWarnings("SameParameterValue") @Nullable U identity,
                                  BiFunction<U, ? super T, U> accumulator,
                                  BinaryOperator<U> combiner) {
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
     * Return the first and last elements or {@link Optional#empty()} if {@code Iterables.isEmpty(iterable)}.
     *
     * @param iterable The iterable to get the ends from
     * @param <T>      The type of element in the iterable
     * @return optional {@link Ends} with the first and last of the iterable
     */
    @Nonnull
    public static <T> Optional<Ends<T>> ends(Iterable<T> iterable) {
        return J8Iterators.ends(iterable.iterator());
    }

    /**
     * Peek at the iterable without modifying the result.
     *
     * @param iterable The iterable to peek at
     * @param consumer The peeking function
     * @param <T>      The type of elements
     * @return an Iterable that, when traversed will invoke the consumer on each element
     * @see Stream#peek(Consumer)
     */
    @Nonnull
    public static <T> FluentIterable<T> peek(Iterable<T> iterable, Consumer<? super T> consumer) {
        return fromSupplier(() -> new PeekIterator<>(iterable.iterator(), consumer));
    }

    /**
     * Return a peeking transformer - a UnaryOperator that will send each element to the consumer and return identity.
     *
     * @param consumer The peeking function
     * @param <T>      The type of elements
     * @return a peeking (non-transforming) transformer
     */
    @Nonnull
    public static <T> ConsumingIdentity<T> peeker(Consumer<T> consumer) {
        return new ConsumingIdentity<>(consumer);
    }

    /**
     * Create a one-time Iterable from a Stream.
     *
     * @param stream The Stream to use in creating an Iterable
     * @param <T>    The type of elements
     * @return Iterable from the given stream
     */
    @Nonnull
    public static <T> FluentIterable<T> fromStream(Stream<T> stream) {
        return new StreamIterable<>(stream);
    }

    /**
     * Create a {@link Stream} from the given {@link Iterable}.
     *
     * @param iterable The Iterable to use in creating a Stream
     * @param <T>      The type of elements
     * @return Stream from the given iterable
     */
    @Nonnull
    public static <T> Stream<T> toStream(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<T>) iterable).stream();
        }
        // TODO(scr): Is it possible to do late-binding (iterable::spliterator)? Need to know characteristics.
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Create an {@link DoubleStream} from the given {@code doubleIterable}.
     *
     * @param doubleIterable The iterable to use in creating a DoubleStream
     * @return DoubleStream from the given iterable
     */
    @Nonnull
    public static DoubleStream toStream(J8PrimitiveIterable.OfDouble doubleIterable) {
        // TODO(scr): Is it possible to do late-binding (iterable::spliterator)? Need to know characteristics.
        return StreamSupport.doubleStream(doubleIterable.primitiveSpliterator(), false);
    }

    /**
     * Create an {@link IntStream} from the given {@code intIterable}.
     *
     * @param intIterable The iterable to use in creating an IntStream
     * @return IntStream from the given iterable
     */
    @Nonnull
    public static IntStream toStream(J8PrimitiveIterable.OfInt intIterable) {
        // TODO(scr): Is it possible to do late-binding (iterable::spliterator)? Need to know characteristics.
        return StreamSupport.intStream(intIterable.primitiveSpliterator(), false);
    }

    /**
     * Create a {@link LongStream} from the given {@code iterable}.
     *
     * @param longIterable The iterable to use in creating a LongStream
     * @return LongStream from the given iterable
     */
    @Nonnull
    public static LongStream toStream(J8PrimitiveIterable.OfLong longIterable) {
        // TODO(scr): Is it possible to do late-binding (iterable::spliterator)? Need to know characteristics.
        return StreamSupport.longStream(longIterable.primitiveSpliterator(), false);
    }

    /**
     * Return a {@link FluentIterable} that is always empty.
     *
     * @param <T> The type of the FluentIterable
     * @return an empty FluentIterable
     */
    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T> FluentIterable<T> emptyIterable() {
        return (FluentIterable<T>) EMPTY_ITERABLE;
    }

    /**
     * Create a FluentIterable for elements.
     * <p>
     * Provides a wrapper to help where {@link FluentIterable} falls short - no varargs static constructor for testing.
     *
     * @param elements the elements to iterate over
     * @param <T>      the type of elements
     * @return a FluentIterable for elements
     */
    @SafeVarargs
    @Nonnull
    public static <T> FluentIterable<T> of(T... elements) {
        return FluentIterable.of(elements);
    }

    /**
     * Reverse the given {@code iterable}.
     *
     * @param iterable an iterable to reverse
     * @param <T>      the type of elements
     * @return an iterable that reverses the navigableSet
     */
    @Nonnull
    public static <T> FluentIterable<T> reverse(Iterable<? extends T> iterable) {
        // If it's already reversable, return it.
        if (iterable instanceof NavigableSet) {
            @SuppressWarnings("unchecked")
            NavigableSet<T> navigableSet = (NavigableSet<T>) iterable;
            return fromSupplier(navigableSet::descendingIterator);
        } else if (iterable instanceof Deque) {
            @SuppressWarnings("unchecked")
            Deque<T> deque = (Deque<T>) iterable;
            return fromSupplier(deque::descendingIterator);
        } else if (iterable instanceof List) {
            @SuppressWarnings("unchecked")
            List<T> list = (List<T>) iterable;
            return fromSupplier(() -> J8Iterators.reverse(list.listIterator(list.size())));
        }
        // Slurp everything into a deque and then reverse its order.
        return fromSupplier(() -> {
            Deque<T> deque = new ArrayDeque<>();
            Iterables.addAll(deque, iterable);
            return deque.descendingIterator();
        });
    }

    /**
     * Create a {@link FluentIterable} from the given {@link Supplier}.
     *
     * @param supplier the supplier
     * @param <T>      the type of elements of the supplied iterable
     * @return an iterable
     */
    @Nonnull
    public static <T> SupplierIterable<T> fromSupplier(Supplier<Iterator<? extends T>> supplier) {
        @SuppressWarnings("unchecked")
        Supplier<Iterator<T>> tSupplier = (Supplier<Iterator<T>>) (Supplier) supplier;
        return new SupplierIterable<>(tSupplier);
    }

    @Nonnull
    public static <T> J8PrimitiveIterable.OfDouble mapToDouble(
            Iterable<T> iterable, ToDoubleFunction<T> toDoubleFunction) {
        return new J8PrimitiveIterable.OfDouble() {
            @Override
            public PrimitiveIterator.OfDouble primitiveIterator() {
                return J8Iterators.mapToDouble(iterable.iterator(), toDoubleFunction);
            }

            @Override
            public Spliterator.OfDouble primitiveSpliterator() {
                return J8Spliterators.mapToDouble(iterable.spliterator(), toDoubleFunction);
            }
        };
    }

    @Nonnull
    public static <T> J8PrimitiveIterable.OfInt mapToInt(
            Iterable<T> iterable, ToIntFunction<T> toIntFunction) {
        return new J8PrimitiveIterable.OfInt() {
            @Override
            public PrimitiveIterator.OfInt primitiveIterator() {
                return J8Iterators.mapToInt(iterable.iterator(), toIntFunction);
            }

            @Override
            public Spliterator.OfInt primitiveSpliterator() {
                return J8Spliterators.mapToInt(iterable.spliterator(), toIntFunction);
            }
        };
    }

    @Nonnull
    public static <T> J8PrimitiveIterable.OfLong mapToLong(
            Iterable<T> iterable, ToLongFunction<T> toLongFunction) {
        return new J8PrimitiveIterable.OfLong() {
            @Override
            public PrimitiveIterator.OfLong primitiveIterator() {
                return J8Iterators.mapToLong(iterable.iterator(), toLongFunction);
            }

            @Override
            public Spliterator.OfLong primitiveSpliterator() {
                return J8Spliterators.mapToLong(iterable.spliterator(), toLongFunction);
            }
        };
    }
}
