package com.github.scr.j8iterables.core;

import com.github.scr.j8iterables.J8Iterables;
import org.testng.annotations.Test;

import java.util.Collections;

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

    @Test
    public void testUnique() throws Exception {
        String foo = "foo";
        String bar = "bar";
        assertThat("same ends are not unique", !Ends.of(foo, foo).areUnique());
        assertThat("different ends are unique", Ends.of(foo, bar).areUnique());
        assertThat("ends from single iterable are not unique",
                !J8Iterables.ends(Collections.singleton(foo)).get().areUnique());
    }
}