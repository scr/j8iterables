package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.ToDoubleSpliterator;
import com.github.scr.j8iterables.core.ToIntSpliterator;
import com.github.scr.j8iterables.core.ToLongSpliterator;
import com.google.common.annotations.VisibleForTesting;

import javax.annotation.Nonnull;
import java.util.Spliterator;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Utility methods to extend Spliterator.
 *
 * @author scr
 */
@SuppressWarnings("WeakerAccess")
public class J8Spliterators {
    @VisibleForTesting
    J8Spliterators() {
    }

    @Nonnull
    public static <T> Spliterator.OfInt mapToInt(
            Spliterator<T> iterator, ToIntFunction<T> toIntFunction) {
        return new ToIntSpliterator<>(iterator, toIntFunction);
    }

    @Nonnull
    public static <T> Spliterator.OfLong mapToLong(
            Spliterator<T> iterator, ToLongFunction<T> toLongFunction) {
        return new ToLongSpliterator<>(iterator, toLongFunction);
    }

    @Nonnull
    public static <T> Spliterator.OfDouble mapToDouble(
            Spliterator<T> iterator, ToDoubleFunction<T> toDoubleFunction) {
        return new ToDoubleSpliterator<>(iterator, toDoubleFunction);
    }
}
