package com.github.scr.j8iterables.core;

import java.util.Iterator;
import java.util.ListIterator;

/**
 * An {@link Iterator} that walks a {@link ListIterator} backwards.
 *
 * @author scr
 */
public class PreviousListIterator<E> implements Iterator<E> {
    private final ListIterator<E> delegateListIterator;

    public PreviousListIterator(ListIterator<E> delegateListIterator) {
        this.delegateListIterator = delegateListIterator;
    }

    @Override
    public boolean hasNext() {
        return delegateListIterator.hasPrevious();
    }

    @Override
    public E next() {
        return delegateListIterator.previous();
    }

    @Override
    public void remove() {
        delegateListIterator.remove();
    }
}
