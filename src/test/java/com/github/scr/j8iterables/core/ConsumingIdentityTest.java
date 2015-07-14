package com.github.scr.j8iterables.core;

import com.github.scr.j8iterables.core.ConsumingIdentity;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by scr on 7/13/15.
 */
@Test
public class ConsumingIdentityTest {
    @Test
    public void testConsumingIdentity() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Consumer<Integer> consumer = atomicInteger::set;
        ConsumingIdentity<Integer> peeker = new ConsumingIdentity<>(consumer);

        assertThat(peeker.apply(5), is(5));
        assertThat(atomicInteger.get(), is(5));
    }
}