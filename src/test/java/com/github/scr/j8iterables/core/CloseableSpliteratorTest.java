package com.github.scr.j8iterables.core;

import com.github.scr.j8iterables.TestResource;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Closeable;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 2/24/17.
 */
public class CloseableSpliteratorTest {
    TestResource testResource;
    Spliterator<Integer> testSpliterator;
    CloseableSpliterator<Integer, TestResource> closeableSpliterator;

    @BeforeMethod
    public void setUp() throws Exception {
        testResource = Mockito.spy(TestResource.class);
        testSpliterator = Mockito.spy(testResource.spliterator());
        Mockito.doReturn(null).when(testSpliterator).getComparator();
        closeableSpliterator = new CloseableSpliterator<>(testResource, TestResource::close, testSpliterator);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        testResource = null;
        testSpliterator = null;
    }

    @Test
    public void testTrySplit() throws Exception {
        assertThat(closeableSpliterator.trySplit(), notNullValue());
        Mockito.verify(testSpliterator).trySplit();
    }

    @Test
    public void testHasCharacteristics() throws Exception {
        assertThat("Not ordered", closeableSpliterator.hasCharacteristics(Spliterator.ORDERED));
        Mockito.verify(testSpliterator).hasCharacteristics(Spliterator.ORDERED);
    }

    @Test
    public void testCharacteristics() throws Exception {
        final int noCharacteristics = 0;
        assertThat(closeableSpliterator.characteristics(), not(noCharacteristics));
        Mockito.verify(testSpliterator).characteristics();
    }

    @Test
    public void testEstimateSize() throws Exception {
        assertThat(closeableSpliterator.estimateSize(), is((long) testResource.size()));
        Mockito.verify(testSpliterator).estimateSize();
    }

    @Test
    public void testComparator() throws Exception {
        closeableSpliterator.getComparator();
        Mockito.verify(testSpliterator).getComparator();
    }

    @Test
    public void testGetExactSizeIfKnown() throws Exception {
        assertThat(closeableSpliterator.getExactSizeIfKnown(), is((long) testResource.size()));
        Mockito.verify(testSpliterator).getExactSizeIfKnown();
    }

    @Test
    public void testTryAdvance() throws Exception {
        AtomicInteger atomicInteger = Mockito.mock(AtomicInteger.class);
        closeableSpliterator.tryAdvance(atomicInteger::set);
        Mockito.verify(atomicInteger).set(testResource.get(0));
        Mockito.verify(testSpliterator).tryAdvance(Mockito.any());
    }

    @Test
    public void testForEachRemaining() throws Exception {
        AtomicInteger atomicInteger = Mockito.mock(AtomicInteger.class);
        closeableSpliterator.forEachRemaining(atomicInteger::set);
        InOrder inOrder = Mockito.inOrder(atomicInteger);
        inOrder.verify(atomicInteger).set(testResource.get(0));
        inOrder.verify(atomicInteger).set(testResource.get(1));
        inOrder.verify(atomicInteger).set(testResource.get(2));
        inOrder.verifyNoMoreInteractions();
        Mockito.verify(testSpliterator).forEachRemaining(Mockito.any());
        Mockito.verify(testResource).close();
    }

    @Test
    public void testTryAdvanceClosesWhenDone() throws Exception {
        AtomicInteger atomicInteger = Mockito.mock(AtomicInteger.class);
        assertThat("Done too soon", closeableSpliterator.tryAdvance(atomicInteger::set));
        assertThat("Done too soon", closeableSpliterator.tryAdvance(atomicInteger::set));
        assertThat("Done too soon", closeableSpliterator.tryAdvance(atomicInteger::set));
        InOrder inOrder = Mockito.inOrder(atomicInteger);
        inOrder.verify(atomicInteger).set(testResource.get(0));
        inOrder.verify(atomicInteger).set(testResource.get(1));
        inOrder.verify(atomicInteger).set(testResource.get(2));
        inOrder.verifyNoMoreInteractions();
        Mockito.verify(testResource, Mockito.never()).close();
        assertThat("Not done", !closeableSpliterator.tryAdvance(atomicInteger::set));
        Mockito.verify(testResource).close();
    }

    @Test
    public void testOfCloseableResource() throws Exception {
        Closeable testCloseable = Mockito.mock(Closeable.class);
        CloseableSpliterator<Integer, Closeable> closeableSpliteratorOfCloseable =
                CloseableSpliterator.ofCloseableResource(testCloseable, Spliterators.<Integer>emptySpliterator());
        AtomicInteger atomicInteger = Mockito.mock(AtomicInteger.class);
        Mockito.verifyNoMoreInteractions(atomicInteger);
        assertThat("Unexpected elements", !closeableSpliteratorOfCloseable.tryAdvance(atomicInteger::set));
        Mockito.verify(testCloseable).close();
    }
}