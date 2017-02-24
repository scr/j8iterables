package com.github.scr.j8iterables.core;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * This iterator allows peeking without transformation of elements.
 *
 * @author scr
 * @see com.github.scr.j8iterables.J8Iterables#peek(Iterable, Consumer)
 */
public class PeekIterator<E> implements Iterator<E> {
    private final Iterator<E> backingIterator;
    private final Consumer<? super E> consumer;

    public PeekIterator(Iterator<E> iterator, Consumer<? super E> consumer) {
        this.backingIterator = iterator;
        this.consumer = consumer;
    }

    @Override
    public boolean hasNext() {
        return backingIterator.hasNext();
    }

    @Override
    public E next() {
        E ret = backingIterator.next();
        consumer.accept(ret);
        return ret;
    }

    @Override
    public void remove() {
        backingIterator.remove();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forEachRemaining(Consumer<? super E> action) {
        backingIterator.forEachRemaining(((Consumer<E>) consumer).andThen(action));
    }
}
