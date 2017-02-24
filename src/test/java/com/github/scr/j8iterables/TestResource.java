package com.github.scr.j8iterables;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by scr on 2/24/17.
 */
public class TestResource extends ArrayList<Integer> implements AutoCloseable {
    public TestResource() {
        super(Arrays.asList(1, 2, 3));
    }

    @Override
    public void close() {
    }
}
