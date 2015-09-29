package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Utility methods to extend Guava Iterables with Java 8 Stream-like classes such as Collectors.
 *
 * @author scr
 */
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
     * @return The reduced result
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
     * Return the first and last elements or {@link Optional#empty()} if {@code Iterables.isEmpty(iterable)}.
     *
     * @param iterable The iterable to get the ends from
     * @param <T>      The type of element in the iterable
     * @return optional {@link Ends} with the first and last of the iterable
     */
    @NotNull
    public static <T> Optional<Ends<T>> ends(@NotNull Iterable<T> iterable) {
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
    @NotNull
    public static <T> FluentIterable<T> peek(@NotNull Iterable<T> iterable, @NotNull Consumer<? super T> consumer) {
        return fromSupplier(() -> new PeekIterator<>(iterable.iterator(), consumer));
    }

    /**
     * Return a peeking transformer - a UnaryOperator that will send each element to the consumer and return identity.
     *
     * @param consumer The peeking function
     * @param <T>      The type of elements
     * @return a peeking (non-transforming) transformer
     */
    @NotNull
    public static <T> ConsumingIdentity<T> peeker(@NotNull Consumer<T> consumer) {
        return new ConsumingIdentity<>(consumer);
    }

    /**
     * Create a one-time Iterable from a Stream.
     *
     * @param stream The Stream to use in creating an Iterable
     * @param <T>    The type of elements
     * @return Iterable from the given stream
     */
    @NotNull
    public static <T> FluentIterable<T> fromStream(@NotNull Stream<T> stream) {
        return new StreamIterable<>(stream);
    }

    /**
     * Create a {@link Stream} from the given {@link Iterable}.
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

    /**
     * Create an {@link DoubleStream} from the given {@code doubleIterable}.
     *
     * @param doubleIterable The iterable to use in creating a DoubleStream
     * @return DoubleStream from the given iterable
     */
    @NotNull
    public static DoubleStream toStream(@NotNull J8PrimitiveIterable.OfDouble doubleIterable) {
        // TODO(scr): Is it possible to do late-binding (iterable::spliterator)? Need to know characteristics.
        return StreamSupport.doubleStream(doubleIterable.primitiveSpliterator(), false);
    }

    /**
     * Create an {@link IntStream} from the given {@code intIterable}.
     *
     * @param intIterable The iterable to use in creating an IntStream
     * @return IntStream from the given iterable
     */
    @NotNull
    public static IntStream toStream(@NotNull J8PrimitiveIterable.OfInt intIterable) {
        // TODO(scr): Is it possible to do late-binding (iterable::spliterator)? Need to know characteristics.
        return StreamSupport.intStream(intIterable.primitiveSpliterator(), false);
    }

    /**
     * Create a {@link LongStream} from the given {@code iterable}.
     *
     * @param longIterable The iterable to use in creating a LongStream
     * @return LongStream from the given iterable
     */
    @NotNull
    public static LongStream toStream(@NotNull J8PrimitiveIterable.OfLong longIterable) {
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
    public static <T> FluentIterable<T> emptyIterable() {
        return (FluentIterable<T>) EMPTY_ITERABLE;
    }

    /**
     * Create a FluentIterable for elements.
     *
     * Provides a wrapper to help where {@link FluentIterable} falls short - no varargs static constructor for testing.
     *
     * @param elements the elements to iterate over
     * @param <T>      the type of elements
     * @return a FluentIterable for elements
     */
    @SafeVarargs
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
    public static <T> SupplierIterable<T> fromSupplier(@NotNull Supplier<Iterator<? extends T>> supplier) {
        @SuppressWarnings("unchecked")
        Supplier<Iterator<T>> tSupplier = (Supplier<Iterator<T>>) (Supplier) supplier;
        return new SupplierIterable<>(tSupplier);
    }

    public static <T> J8PrimitiveIterable.OfDouble mapToDouble(
            @NotNull Iterable<T> iterable, @NotNull ToDoubleFunction<T> toDoubleFunction) {
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

    public static <T> J8PrimitiveIterable.OfInt mapToInt(
            @NotNull Iterable<T> iterable, @NotNull ToIntFunction<T> toIntFunction) {
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

    public static <T> J8PrimitiveIterable.OfLong mapToLong(
            @NotNull Iterable<T> iterable, @NotNull ToLongFunction<T> toLongFunction) {
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
