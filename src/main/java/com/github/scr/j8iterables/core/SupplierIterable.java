package com.github.scr.j8iterables.core;

import com.google.common.collect.FluentIterable;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Iterable that gets its {@link Iterator} from a {@link Supplier}.
 *
 * @author scr
 */
public class SupplierIterable<E> extends FluentIterable<E> {
    private final Supplier<Iterator<E>> SUPPLIER;

    public SupplierIterable(Supplier<Iterator<E>> supplier) {
        SUPPLIER = supplier;
    }

    @Override
    @Nonnull
    public Iterator<E> iterator() {
        return SUPPLIER.get();
    }
}
