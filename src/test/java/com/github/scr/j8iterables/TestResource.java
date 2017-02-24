package com.github.scr.j8iterables;

import java.util.ArrayList;

/**
 * Created by scr on 2/24/17.
 */
public class TestResource extends ArrayList<Integer> implements AutoCloseable {
    public TestResource() {
        super(3);
        add(1);
        add(2);
        add(3);
    }

    @Override
    public void close() {
    }
}
