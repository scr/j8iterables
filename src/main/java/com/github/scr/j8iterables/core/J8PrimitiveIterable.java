package com.github.scr.j8iterables.core;

import java.util.PrimitiveIterator;
import java.util.Spliterator;

/**
 * Classes that implement this provide methods to obtain a PrimitiveIterator and Spliterator.OfPrimitive.
 *
 * @author scr
 */
public interface J8PrimitiveIterable<T_ITERATOR, T_SPLITERATOR> {

    T_ITERATOR primitiveIterator();

    T_SPLITERATOR primitiveSpliterator();

    interface OfInt extends J8PrimitiveIterable<PrimitiveIterator.OfInt, Spliterator.OfInt> {
    }

    interface OfLong extends J8PrimitiveIterable<PrimitiveIterator.OfLong, Spliterator.OfLong> {
    }

    interface OfDouble extends J8PrimitiveIterable<PrimitiveIterator.OfDouble, Spliterator.OfDouble> {
    }
}
