package com.github.scr.j8iterables;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 7/13/15.
 */
@Test
public class J8IteratorsTest {
    @Test
    public void testStreamFromIterator() throws Exception {
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        Stream<Integer> stream = J8Iterators.toStream(iterator);
        assertThat(stream.mapToInt(a -> a).sum(), is(6));
    }

    @Test
    public void testCoverageTrickForUtilityClass() throws Exception {
        assertThat(new J8Iterators(), notNullValue());
    }
}