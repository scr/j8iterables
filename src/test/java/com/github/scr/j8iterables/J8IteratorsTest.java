package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.Ends;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
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

    @DataProvider(name = "endIterators")
    public static Iterator<Object[]> endIterators() {
        return Iterators.transform(Arrays.asList(J8IterablesTest.endIterables()).iterator(),
                objects -> new Object[]{objects[0], ((Iterable) objects[1]).iterator(), objects[2]});
    }

    @Test(dataProvider = "endIterators")
    public <T> void testGetEnds(String desc, Iterator<T> iterator, Optional<Ends<T>> expectedEnds) throws Exception {
        Optional<Ends<T>> ends = J8Iterators.ends(iterator);
        assertThat(desc, ends, is(expectedEnds));
        if (ends.isPresent() && expectedEnds.isPresent()) {
            assertThat(ends.get().getFirst(), is(expectedEnds.get().getFirst()));
            assertThat(ends.get().getLast(), is(expectedEnds.get().getLast()));
        }
    }

    @Test
    public void testPeek() throws Exception {
        List<Integer> input = Arrays.asList(1, 2, 3);
        List<Integer> peekedOutput = new ArrayList<>();
        List<Integer> output = new ArrayList<>();

        Iterator<Integer> inputIterator = input.iterator();
        Iterator<Integer> peekingIterator = J8Iterators.peek(inputIterator, e -> peekedOutput.add(e + 1));
        peekingIterator.forEachRemaining(output::add);

        assertThat(peekedOutput, is(Arrays.asList(2, 3, 4)));
        assertThat(output, is(Arrays.asList(1, 2, 3)));
    }
}