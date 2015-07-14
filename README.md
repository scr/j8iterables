# j8iterables
Extending Guava Iterables for Java8.

## Inspiration
`Stream` provides interesting composibility constructs that encourage smaller functions that focus on very straightforward tasks.  However, there are two features that are sorely lacking:

1. While it implements all of the Iterable interface methods, it does not declare Iterable, meaning that you cannot use it in a "enhanced for" loop.  While that may seem like mere syntactic sugar, it also means that in order to refer to references in the scope, you end up creating a "capturing lambda" which does not perform as well as a non-capturing one (some sources say 6x slower).  More important than this may be the distress it places on developers who have to figure out what to do to get things to be "effectively final" such as using `int[] counter = {0};` to reference `counter[0]` inside the lambda or `AtomicInteger`.
2. Streams are not replayable - this may be by design to declare a contract of something that is consumable, but it means that much of the goodness of the architecture is lost even when using things that should be replayable such as `Collection::stream`.

Guava's `FluentIterable` class provides very similar composibility constructs and makes using `Iterable` quite enjoyable.  However, it seems to lack in some of the finishing composibility such as "collect", and "reduce".

Finally, going between the two constructs (Stream and Iterable) isn't incredibly easy though it can be.

## Usage: Maven dependency
```
<dependency>
  <groupId>com.github.scr</groupId>
  <artifactId>j8iterables</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Usage: Examples

### Enhanced for loop on stream
```
Stream<Integer> theStream = Stream.of(1, 2, 3);
int i = 0;
for (Integer integer : J8Iterables.fromStream(theStream)) {
    Assert.assertEquals(integer, ++i);
}
```

### Partitioning & reducing as sum
```
Iterable<Integer> input = Arrays.asList(1, 2, 3, 4);
Map<Boolean, Integer> result = J8Iterables.collect(
    Collections.partitioningBy(i -> (i % 2) == 0, 
        Collections.reducing(Integer::sum)));
Assert.assertEquals(result.get(false), 4);
Assert.assertEquals(result.get(true), 6);
```

### Peek, using FluentIterable return
```
Iterable<Integer> input = Arrays.asList(1, 2, 3, 4);
List<Integer> collectingList = new ArrayList<>();
List<Integer> result = J8Iterables.peek(input, collectingList::add)
    .toList();
Assert.assertEquals(result, collectingList);
```
