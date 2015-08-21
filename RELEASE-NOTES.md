* 1.6.0
   * Add `of(E... elements)` method to help create `FluentIterable` for testing, etc.
* 1.5.0
   * Improve `J8Iterables.peek` by making a `PeekIterator` class instead of faking Guava's transform to return identity.
   * Provide `J8Iterators.peek` as well as the `J8Iterables` version.
* 1.4.0
   * Added `J8Iterables.emptyIterable()` method to get an empty FluentIterable.
* 1.3.0
   * Added ends() - facility to get both the first and last in an Iterable or Iterator.
* 1.2.1
   * Update some javadoc.
* 1.2.0
   * Move inner classes into core package.
   * Create ConsumingIdentity for peek operation on Iterables.
   * Added peeker and peek methods to enable peeking without transformation.
* 1.0.1
   * Return FluentIterable from the fromStream method for more stream-like functionality right out of the gate.
* 1.0.0
   * Initial version