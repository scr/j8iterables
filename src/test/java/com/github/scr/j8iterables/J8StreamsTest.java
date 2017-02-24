package com.github.scr.j8iterables;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 2/24/17.
 */
public class J8StreamsTest {
    @Test
    public void testCoverageTrickForUtilityClass() throws Exception {
        assertThat(new J8Streams(), notNullValue());
    }


    @Test
    public void testCloseIsCalled() throws Exception {
        TestResource testResource = Mockito.spy(TestResource.class);
        Stream<Integer> testStream = J8Streams.unfoldResource(
                () -> testResource, Iterable::spliterator, TestResource::close, 0, false);
        assertThat(testStream.count(), is(3L));
        Mockito.verify(testResource).close();
    }
}