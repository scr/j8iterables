package com.github.scr.j8iterables.core;

import javax.annotation.Nonnull;

/**
 * Encapsulate the first and last elements of a list.
 *
 * @author scr
 */
@SuppressWarnings("WeakerAccess")
public class Ends<T> {
    private final T first;
    private final T last;

    /**
     * Construct a new Ends from {@code first} and {@code last}.
     *
     * @param first the first end
     * @param last  the last end
     */
    public Ends(T first, T last) {
        this.first = first;
        this.last = last;
    }

    /**
     * Get the first end.
     *
     * @return the first end
     */
    @Nonnull
    public T getFirst() {
        return first;
    }

    /**
     * Get the last end.
     *
     * @return the last end
     */
    @Nonnull
    public T getLast() {
        return last;
    }

    /**
     * Determine whether first and last are unique from each other.
     *
     * @return true when first != last
     */
    public boolean areUnique() {
        return first != last;
    }

    /**
     * Return a new Ends from {@code first} and {@code last}.
     *
     * @param first the first end
     * @param last  the last end
     * @param <T>   the type of first and last
     * @return a new Ends object
     */
    @Nonnull
    public static <T> Ends<T> of(T first, T last) {
        return new Ends<>(first, last);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ends<?> ends = (Ends<?>) o;
        return first.equals(ends.first) && last.equals(ends.last);
    }

    @Override
    public int hashCode() {
        return 31 * first.hashCode() + last.hashCode();
    }

    @Override
    @Nonnull
    public String toString() {
        return "Ends{" +
                "first=" + first +
                ", last=" + last +
                '}';
    }
}
