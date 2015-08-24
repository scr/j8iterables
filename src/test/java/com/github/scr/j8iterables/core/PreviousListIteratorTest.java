package com.github.scr.j8iterables.core;

import com.github.scr.j8iterables.J8Iterables;
import com.github.scr.j8iterables.J8Iterators;
import com.google.common.collect.Lists;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 8/24/15.
 */
@Test
public class PreviousListIteratorTest {
    @Test
    public void testRemoveWorks() throws Exception {
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        assertThat(list, is(Arrays.asList(1, 2, 3)));

        Iterator<Integer> reverseIterator = J8Iterators.reverse(list.listIterator(list.size()));
        assertThat("hasNext", reverseIterator.hasNext());
        reverseIterator.next();
        reverseIterator.remove();
        assertThat(list, is(Arrays.asList(1, 2)));
    }
}