package com.github.scr.j8iterables;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 7/13/15.
 */
@Test
public class J8IterablesTest {
    @Test
    public void testCollect() throws Exception {
        Collection<Pair<String, Integer>> collection = Arrays.asList(
                Pair.of("foo", 1),
                Pair.of("foo", 2),
                Pair.of("bar", 5));

        Map<String, Integer> results = J8Iterables.collect(collection,
                Collectors.groupingBy(Pair::getLeft, Collectors.summingInt(Pair::getRight)));

        assertThat(results.get("foo"), is(3));
        assertThat(results.get("bar"), is(5));
    }

    private static void accumulate(HashMap<String, Integer> hashMap, Pair<String, Integer> value) {
        hashMap.compute(value.getKey(), (k, v) -> value.getValue() + ((v == null) ? 0 : v));
    }

    private static void combine(HashMap<String, Integer> hashMap, HashMap<String, Integer> hashMap2) {
        hashMap2.forEach((hm2k, hm2v) -> {
            hashMap.compute(hm2k, (k, v) -> hm2v + ((v == null) ? 0 : v));
        });
    }

    @Test
    public void testCombiningCollect() throws Exception {
        Collection<Pair<String, Integer>> collection = Arrays.asList(
                Pair.of("foo", 1),
                Pair.of("foo", 2),
                Pair.of("bar", 5));
        Collection<Pair<String, Integer>> collection2 = Arrays.asList(
                Pair.of("foo", 7),
                Pair.of("bar", 10),
                Pair.of("bar", 5));

        Map<String, Integer> results = J8Iterables.collect(
                Arrays.asList(collection, collection2),
                HashMap::new,
                J8IterablesTest::accumulate,
                J8IterablesTest::combine);

        assertThat(results.get("foo"), is(10));
        assertThat(results.get("bar"), is(20));
    }

    @Test
    public void testReductionSumOptional() throws Exception {
        assertThat(J8Iterables.reduce(Arrays.asList(1, 2, 3), Integer::sum), is(Optional.of(6)));
    }

    @Test
    public void testReductionSum() throws Exception {
        assertThat(J8Iterables.reduce(Arrays.asList(1, 2, 3), 0, Integer::sum), is(6));
    }

    @Test
    public void testReductionIterables() throws Exception {
        Collection<Integer> collection = Arrays.asList(1, 2, 3);
        Collection<Integer> collection2 = Arrays.asList(4, 5, 6);

        int result = J8Iterables.reduce(
                Arrays.asList(collection, collection2),
                0,
                Integer::sum,
                Integer::sum);

        assertThat(result, is(21));
    }

    @Test
    public void testReductionMax() throws Exception {
        assertThat(J8Iterables.reduce(Arrays.asList(1, 2, 3), 0, Integer::max), is(3));
    }

    @Test
    public void testFromStream() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        int i = 0;
        for (Integer integer : J8Iterables.fromStream(stream)) {
            assertThat(integer, is(++i));
        }
    }

    @Test
    public void testToStream() throws Exception {
        Iterable<Integer> iterable = Arrays.asList(1, 2, 3);
        Stream<Integer> stream = J8Iterables.toStream(iterable);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        stream.forEachOrdered(i -> {
            assertThat(i, is(atomicInteger.addAndGet(1)));
        });
        assertThat(atomicInteger.get(), not(0));
    }

    @Test
    public void testFromStreamForEach() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        Iterable<Integer> iterable = J8Iterables.fromStream(stream);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        iterable.forEach(i -> {
            assertThat(i, is(atomicInteger.addAndGet(1)));
        });
        assertThat(atomicInteger.get(), not(0));
    }

    @Test
    public void testFromStreamToStream() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        Iterable<Integer> iterable = J8Iterables.fromStream(stream);
        Stream<Integer> stream2 = J8Iterables.toStream(iterable);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        stream2.forEachOrdered(i -> {
            assertThat(i, is(atomicInteger.addAndGet(1)));
        });
        assertThat(atomicInteger.get(), not(0));
    }

    @Test
    public void testNonContainerIterableToStream() throws Exception {
        Iterable<Integer> iterable = Iterables.concat(Arrays.asList(1, 2), Collections.singleton(3));
        Stream<Integer> stream = J8Iterables.toStream(iterable);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        iterable.forEach(i -> {
            assertThat(i, is(atomicInteger.addAndGet(1)));
        });
        assertThat(atomicInteger.get(), not(0));
    }
}