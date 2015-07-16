package com.github.scr.j8iterables.core;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 7/16/15.
 */
public class EndsTest {
    @Test
    public void testHashCode() throws Exception {
        assertThat(Ends.of(1, 2).hashCode(), is(Ends.of(1, 2).hashCode()));
        assertThat(Ends.of(1, 2).hashCode(), not(Ends.of(2, 2).hashCode()));
    }

    @Test
    public void testEquals() throws Exception {
        Ends<?> ends = Ends.of(1, 2);
        assertThat("ends equals itself", ends.equals(ends));
        assertThat("ends equals equivalent", ends.equals(Ends.of(1, 2)));
    }
}