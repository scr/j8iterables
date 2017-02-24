package com.github.scr.j8iterables;

import com.github.scr.j8iterables.core.CloseableSpliterator;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Spliterators;
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
        assertThat(testStream.count(), is((long) testResource.size()));
        Mockito.verify(testResource).close();
    }

    @Test
    public void testCloseIsCalledCloseableResource() throws Exception {
        Closeable testCloseable = Mockito.mock(Closeable.class);
        CloseableSpliterator.ofCloseableResource(testCloseable, Spliterators.<Integer>emptySpliterator());
        Stream<Integer> testStream = J8Streams.unfoldResource(
                () -> testCloseable, r -> Spliterators.<Integer>emptySpliterator(), 0, false);
        assertThat(testStream.count(), is(0L));
        Mockito.verify(testCloseable).close();
    }

    @Test(expectedExceptions = UncheckedIOException.class)
    public void testIOExceptionThrowsUnchecked() throws Exception {
        Closeable testCloseable = Mockito.mock(Closeable.class);
        Mockito.doThrow(IOException.class).when(testCloseable).close();
        CloseableSpliterator.ofCloseableResource(testCloseable, Spliterators.<Integer>emptySpliterator());
        Stream<Integer> testStream = J8Streams.unfoldResource(
                () -> testCloseable, r -> Spliterators.<Integer>emptySpliterator(), 0, false);
        assertThat(testStream.count(), is(0L));
        Assert.fail("Exception should be thrown");
    }
}