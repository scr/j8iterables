package com.github.scr.j8iterables.core;

import org.jetbrains.annotations.NotNull;

/**
 * Encapsulate the first and last elements of a list.
 *
 * @author scr
 */
public class Ends<T> {
    @NotNull
    private final T first;
    @NotNull
    private final T last;

    public Ends(@NotNull T first, @NotNull T last) {
        this.first = first;
        this.last = last;
    }

    @NotNull
    public T getFirst() {
        return first;
    }

    @NotNull
    public T getLast() {
        return last;
    }

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
    public String toString() {
        return "Ends{" +
                "first=" + first +
                ", last=" + last +
                '}';
    }
}
