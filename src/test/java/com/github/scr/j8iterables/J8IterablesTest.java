package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.Ends;
import com.github.scr.j8iterables.core.J8PrimitiveIterable;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
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
        stream.forEach(i -> {
            assertThat(i, is(atomicInteger.addAndGet(1)));
        });
        assertThat(atomicInteger.get(), not(0));
    }

    @Test
    public void testFluentIterableFromStream() throws Exception {
        Stream<Integer> stream = Stream.of(1, 2, 3);
        J8Iterables.fromStream(stream)
                .filter(x -> x == 2)
                .size();
    }

    @Test
    public void testPeeker() throws Exception {
        List<Integer> peekingCollector = new ArrayList<>();
        List<Integer> collectedList = FluentIterable.from(Arrays.asList(1, 2, 3))
                .transform(J8Iterables.peeker(peekingCollector::add))
                .toList();
        assertThat(peekingCollector, is(collectedList));
    }

    @Test
    public void testPeek() throws Exception {
        List<Integer> peekingCollector = new ArrayList<>();
        List<Integer> collectedList = J8Iterables.peek(Arrays.asList(1, 2, 3), peekingCollector::add)
                .toList();
        assertThat(peekingCollector, is(collectedList));
    }

    @Test
    public void testCoverageTrickForUtilityClass() throws Exception {
        assertThat(new J8Iterables(), notNullValue());
    }

    @DataProvider(name = "endIterables")
    public static Object[][] endIterables() {
        return new Object[][]{
                {"empty", Collections.emptyList(), Optional.empty()},
                {"one", Collections.singleton(1), Optional.of(Ends.of(1, 1))},
                {"two", Arrays.asList(1, 2), Optional.of(Ends.of(1, 2))},
                {"three", Arrays.asList(1, 2, 3), Optional.of(Ends.of(1, 3))},
        };
    }

    @Test(dataProvider = "endIterables")
    public <T> void testGetEnds(String desc, Iterable<T> iterable, Optional<Ends<T>> expectedEnds) throws Exception {
        Optional<Ends<T>> ends = J8Iterables.ends(iterable);
        assertThat(desc, ends, is(expectedEnds));
        if (ends.isPresent() && expectedEnds.isPresent()) {
            assertThat(ends.get().getFirst(), is(expectedEnds.get().getFirst()));
            assertThat(ends.get().getLast(), is(expectedEnds.get().getLast()));
        }
    }

    @Test
    public void testEmptyIterable() throws Exception {
        assertThat("emptyIterable is empty", J8Iterables.emptyIterable().isEmpty());
    }

    @Test
    public void testForEachRemaining() throws Exception {
        List<Integer> inputList = Arrays.asList(1, 2, 3);
        List<Integer> outputList = new ArrayList<>();
        List<Integer> outputList2 = new ArrayList<>();
        Iterator<Integer> iterator = J8Iterables.peek(inputList, outputList::add).iterator();
        iterator.forEachRemaining(outputList2::add);
        assertThat(outputList, is(Arrays.asList(1, 2, 3)));
        assertThat(outputList2, is(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testPeekWithRemove() throws Exception {
        List<Integer> inputList = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> outputList = new ArrayList<>();

        // before
        assertThat(inputList, is(Arrays.asList(1, 2, 3)));
        assertThat(outputList, is(Collections.emptyList()));

        Iterator<Integer> iterator = J8Iterables.peek(inputList, outputList::add).iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }

        // after
        assertThat(outputList, is(Arrays.asList(1, 2, 3)));
        assertThat(inputList, is(Collections.emptyList()));
    }

    @Test
    public void testOf() throws Exception {
        assertThat(J8Iterables.of(1, 2, 3).toList(), is(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testReverseList() throws Exception {
        assertThat(J8Iterables.reverse(Arrays.asList(1, 2, 3)).toList(), is(Arrays.asList(3, 2, 1)));
    }

    @Test
    public void testReverseDeque() throws Exception {
        assertThat(J8Iterables.reverse(new ArrayDeque<>(Arrays.asList(1, 2, 3))).toList(), is(Arrays.asList(3, 2, 1)));
    }

    @Test
    public void testReverseSortedSet() throws Exception {
        assertThat(J8Iterables.reverse(new TreeSet<>(Arrays.asList(2, 1, 3))).toList(), is(Arrays.asList(3, 2, 1)));
    }

    @Test
    public void testReverseRegularIterable() throws Exception {
        assertThat(J8Iterables.reverse(J8Iterables.of(1, 2, 3)).toList(), is(Arrays.asList(3, 2, 1)));
    }

    @Test
    public void testMapToDouble() throws Exception {
        J8PrimitiveIterable.OfDouble doubleIterable =
                J8Iterables.mapToDouble(Arrays.asList(1, 2, 3), Integer::doubleValue);
        PrimitiveIterator.OfDouble doubleIterator = doubleIterable.primitiveIterator();
        double sum = 0;
        while (doubleIterator.hasNext()) {
            sum += doubleIterator.nextDouble();
        }
        assertThat(sum, is(6d));
        assertThat(J8Iterables.toStream(doubleIterable).sum(), is(6d));
    }

    @Test
    public void testMapToInt() throws Exception {
        J8PrimitiveIterable.OfInt intIterable = J8Iterables.mapToInt(Arrays.asList(1d, 2d, 3d), Double::intValue);
        PrimitiveIterator.OfInt IntIterator = intIterable.primitiveIterator();
        int sum = 0;
        while (IntIterator.hasNext()) {
            sum += IntIterator.nextInt();
        }
        assertThat(sum, is(6));
        assertThat(J8Iterables.toStream(intIterable).sum(), is(6));
    }

    @Test
    public void testMapToLong() throws Exception {
        J8PrimitiveIterable.OfLong longIterable = J8Iterables.mapToLong(Arrays.asList(1, 2, 3), Integer::longValue);
        PrimitiveIterator.OfLong LongIterator = longIterable.primitiveIterator();
        long sum = 0;
        while (LongIterator.hasNext()) {
            sum += LongIterator.nextLong();
        }
        assertThat(sum, is(6L));
        assertThat(J8Iterables.toStream(longIterable).sum(), is(6L));
    }
}
