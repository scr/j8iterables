package com.github.scr.j8iterables;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Spliterator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.*;
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
    public void testDoubleSpliteratorTrySplit() throws Exception {
        Spliterator.OfDouble doubleSpliterator =
                J8Spliterators.mapToDouble(Arrays.asList(1d, 2d, 3d, 4d).spliterator(), Double::doubleValue);
        Spliterator.OfDouble doubleSpliterator2 = doubleSpliterator.trySplit();
        assertThat(doubleSpliterator, not(sameInstance(doubleSpliterator2)));
        assertThat(DoubleStream.concat(StreamSupport.doubleStream(doubleSpliterator, false),
                        StreamSupport.doubleStream(doubleSpliterator2, false)).sum(),
                is(10d));
    }

    @Test
    public void testDoubleSpliteratorTrySplitSmall() throws Exception {
        Spliterator.OfDouble doubleSpliterator =
                J8Spliterators.mapToDouble(Collections.singleton(1d).spliterator(), Double::doubleValue);
        Spliterator.OfDouble doubleSpliterator2 = doubleSpliterator.trySplit();
        assertThat(doubleSpliterator2, nullValue());
    }

    @Test
    public void testIntSpliterator() throws Exception {
        assertThat(StreamSupport.intStream(J8Spliterators.mapToInt(
                        Arrays.asList(1d, 2d, 3d).spliterator(), Double::intValue), false).sum(),
                is(6));
    }

    @Test
    public void testIntSpliteratorTrySplit() throws Exception {
        Spliterator.OfInt IntSpliterator =
                J8Spliterators.mapToInt(Arrays.asList(1d, 2d, 3d, 4d).spliterator(), Double::intValue);
        Spliterator.OfInt IntSpliterator2 = IntSpliterator.trySplit();
        assertThat(IntSpliterator, not(sameInstance(IntSpliterator2)));
        assertThat(IntStream.concat(StreamSupport.intStream(IntSpliterator, false),
                        StreamSupport.intStream(IntSpliterator2, false)).sum(),
                is(10));
    }

    @Test
    public void testIntSpliteratorTrySplitSmall() throws Exception {
        Spliterator.OfInt IntSpliterator =
                J8Spliterators.mapToInt(Collections.singleton(1d).spliterator(), Double::intValue);
        Spliterator.OfInt IntSpliterator2 = IntSpliterator.trySplit();
        assertThat(IntSpliterator2, nullValue());
    }

    @Test
    public void testLongSpliterator() throws Exception {
        assertThat(StreamSupport.longStream(J8Spliterators.mapToLong(
                        Arrays.asList(1d, 2d, 3d).spliterator(), Double::longValue), false).sum(),
                is(6L));
    }

    @Test
    public void testLongSpliteratorTrySplit() throws Exception {
        Spliterator.OfLong LongSpliterator =
                J8Spliterators.mapToLong(Arrays.asList(1d, 2d, 3d, 4d).spliterator(), Double::longValue);
        Spliterator.OfLong LongSpliterator2 = LongSpliterator.trySplit();
        assertThat(LongSpliterator, not(sameInstance(LongSpliterator2)));
        assertThat(LongStream.concat(StreamSupport.longStream(LongSpliterator, false),
                        StreamSupport.longStream(LongSpliterator2, false)).sum(),
                is(10L));
    }

    @Test
    public void testLongSpliteratorTrySplitSmall() throws Exception {
        Spliterator.OfLong LongSpliterator =
                J8Spliterators.mapToLong(Collections.singleton(1d).spliterator(), Double::longValue);
        Spliterator.OfLong LongSpliterator2 = LongSpliterator.trySplit();
        assertThat(LongSpliterator2, nullValue());
    }
}