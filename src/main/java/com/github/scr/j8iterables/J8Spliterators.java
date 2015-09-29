package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.ToDoubleSpliterator;
import com.github.scr.j8iterables.core.ToIntSpliterator;
import com.github.scr.j8iterables.core.ToLongSpliterator;
import com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Utility methods to extend Spliterator.
 *
 * @author scr
 */
public class J8Spliterators {
    @VisibleForTesting
    J8Spliterators() {
    }

    public static <T> Spliterator.OfInt mapToInt(
            @NotNull Spliterator<T> iterator, @NotNull ToIntFunction<T> toIntFunction) {
        return new ToIntSpliterator<>(iterator, toIntFunction);
    }

    public static <T> Spliterator.OfLong mapToLong(
            @NotNull Spliterator<T> iterator, @NotNull ToLongFunction<T> toLongFunction) {
        return new ToLongSpliterator<>(iterator, toLongFunction);
    }

    public static <T> Spliterator.OfDouble mapToDouble(
            @NotNull Spliterator<T> iterator, @NotNull ToDoubleFunction<T> toDoubleFunction) {
        return new ToDoubleSpliterator<>(iterator, toDoubleFunction);
    }
}
