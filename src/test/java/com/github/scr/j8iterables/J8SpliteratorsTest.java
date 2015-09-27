package com.github.scr.j8iterables;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 9/26/15.
 */
@Test
public class J8SpliteratorsTest {
    @Test
    public void testConstructor() throws Exception {
        assertThat(new J8Spliterators(), notNullValue());
    }

    @Test
    public void testDoubleSpliterator() throws Exception {
        assertThat(StreamSupport.doubleStream(J8Spliterators.mapToDouble(
                        Arrays.asList(1d, 2d, 3d).spliterator(), Double::doubleValue), false).sum(),
                is(6d));
    }

    @Test
    public void testIntSpliterator() throws Exception {
        assertThat(StreamSupport.intStream(J8Spliterators.mapToInt(
                        Arrays.asList(1d, 2d, 3d).spliterator(), Double::intValue), false).sum(),
                is(6));
    }

    @Test
    public void testLongSpliterator() throws Exception {
        assertThat(StreamSupport.longStream(J8Spliterators.mapToLong(
                        Arrays.asList(1d, 2d, 3d).spliterator(), Double::longValue), false).sum(),
                is(6L));
    }
}