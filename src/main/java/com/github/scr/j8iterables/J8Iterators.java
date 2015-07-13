package com.github.scr.j8iterables;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility methods to extend Iterators with Java 8 Stream.
 * <p/>
 * Created by scr on 7/13/15.
 */
public class J8Iterators {
    /**
     * Create a Stream from an iterator.
     *
     * @param iterator The iterator to create a Stream from
     * @param <T>      The type of element
     * @return A Stream object from given iterator.
     */
    public static <T> Stream<T> toStream(Iterator<T> iterator) {
        return StreamSupport.stream(((Iterable<T>) () -> iterator).spliterator(), false);
    }
}
