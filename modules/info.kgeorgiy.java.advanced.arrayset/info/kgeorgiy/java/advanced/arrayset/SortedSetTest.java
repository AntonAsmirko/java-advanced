package info.kgeorgiy.java.advanced.arrayset;

import info.kgeorgiy.java.advanced.base.BaseTest;
import net.java.quickcheck.Generator;
import net.java.quickcheck.collection.Pair;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.java.quickcheck.generator.CombinedGenerators.excludeValues;
import static net.java.quickcheck.generator.CombinedGenerators.lists;
import static net.java.quickcheck.generator.CombinedGeneratorsIterables.someOneOf;
import static net.java.quickcheck.generator.CombinedGeneratorsIterables.somePairs;
import static net.java.quickcheck.generator.PrimitiveGenerators.fixedValues;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;

/**
 * Tests for easy version
 * of <a href="https://www.kgeorgiy.info/courses/java-advanced/homeworks.html#homework-arrayset">ArraySet</a> homework
 * for <a href="https://www.kgeorgiy.info/courses/java-advanced/">Java Advanced</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SortedSetTest extends BaseTest {
    @Test
    public void test01_constructors() {
        final Class<?> token = loadClass();
        Assert.assertTrue(token.getName() + " should implement SortedSet interface", SortedSet.class.isAssignableFrom(token));

        checkConstructor("default constructor", token);
        checkConstructor("constructor out of Collection", token, Collection.class);
        checkConstructor("constructor out of Collection and Comparator", token, Collection.class, Comparator.class);
    }

    @Test
    public void test02_empty() {
        final SortedSet<Integer> set = create(new Object[]{});
        Assert.assertEquals("Empty set size should be zero", 0, set.size());
        Assert.assertTrue("Empty set should be empty", set.isEmpty());
        Assert.assertEquals("toArray for empty set should return empty array", 0, (Object) set.toArray().length);
    }

    @Test
    public void test04_externalOrder() {
        test((elements, comparator, treeSet, set, context) -> assertEq(treeSet, set, context));
    }

    @Test
    public void test07_contains() {
        test((elements, comparator, treeSet, set, context) -> {
            for (final Integer element : elements) {
                Assert.assertTrue("set should contains() element " + element + " " + context, set.contains(element));
            }

            for (final Integer element : someOneOf(excludeValues(integers(), elements))) {
                Assert.assertEquals("contains(" + element + ") " + context, treeSet.contains(element), set.contains(element));
            }
        });
    }

    @Test
    public void test09_containsAll() {
        test((elements, comparator, treeSet, set, context) -> {
            Assert.assertTrue("set should contains() all elements " + " " + context, set.containsAll(elements));

            for (final Integer element : someOneOf(excludeValues(integers(), elements))) {
                final List<Integer> l = new ArrayList<>(elements);
                elements.add(element);
                Assert.assertEquals("containsAll(" + l + ") " + context, treeSet.containsAll(l), set.containsAll(l));
            }
        });
    }

    private static List<Integer> toList(final SortedSet<Integer> set) {
        return new ArrayList<>(set);
    }

    protected static List<Number> toArray(final SortedSet<Integer> set) {
        return List.of(set.toArray(new Number[0]));
    }

    protected static void assertEq(final SortedSet<Integer> expected, final SortedSet<Integer> actual, final String context) {
        Assert.assertEquals("invalid element order " + context, toList(expected), toList(actual));
        Assert.assertEquals("invalid toArray " + context, toArray(actual), toArray(actual));
        Assert.assertEquals("invalid set size " + context, expected.size(), (Object) actual.size());
        Assert.assertEquals("invalid isEmpty " + context, expected.isEmpty(), actual.isEmpty());
        Assert.assertSame("invalid comparator " + context, expected.comparator(), actual.comparator());
    }

    protected static <T, S extends SortedSet<T>> S treeSet(final List<T> elements, final Comparator<T> comparator) {
        @SuppressWarnings("unchecked") final S set = (S) new TreeSet<>(comparator);
        set.addAll(elements);
        return set;
    }

    @SuppressWarnings("unchecked")
    protected static <T, S extends SortedSet<T>> S set(final List<T> elements, final Comparator<T> comparator) {
        return (S) create(new Object[]{elements, comparator}, Collection.class, Comparator.class);
    }

    protected static final Generator<NamedComparator> NAMED_COMPARATORS = fixedValues(
            new NamedComparator("Natural order", Integer::compare),
            new NamedComparator("Reverse order", Comparator.comparingInt(Integer::intValue).reversed()),
            new NamedComparator("Div 100", Comparator.comparingInt(i -> i / 100)),
            new NamedComparator("Even first", Comparator.<Integer>comparingInt(i -> i % 2).thenComparing(Integer::intValue)),
            new NamedComparator("All equal", Comparator.comparingInt(i -> 0))
    );

    protected interface TestCase<T, S extends SortedSet<T>> {
        void test(List<T> elements, Comparator<T> comparator, S model, S tested, String context);
    }

    protected static <S extends SortedSet<Integer>> void test(final TestCase<Integer, S> testCase) {
        somePairs(NAMED_COMPARATORS, lists(integers())).forEach(e -> {
            final List<Integer> elements = e.getSecond();
            final NamedComparator comparator = e.getFirst();
            testCase.test(elements, comparator, treeSet(elements, comparator), set(elements, comparator), "(comparator = " + comparator + ", elements = " + elements + ")");
        });
    }

    private static SortedSet<Integer> create(final Object[] params, final Class<?>... types) {
        try {
            @SuppressWarnings("unchecked") final
            SortedSet<Integer> set = (SortedSet<Integer>) loadClass().getConstructor(types).newInstance(params);
            return set;
        } catch (final Exception e) {
            e.printStackTrace();
            Assert.fail("Instantiation error");
            throw new AssertionError();
        }
    }

    @Test
    public void test11_comparator() {
        test((elements, comparator, treeSet, set, context) ->
                Assert.assertSame("comparator() should return provided comparator", comparator, set.comparator()));
    }

    @Test
    public void test12_headSet() {
        test((elements, comparator, treeSet, set, context) -> {
            for (final Integer element : inAndOut(elements)) {
                assertEq(treeSet.headSet(element), set.headSet(element), "in headSet(" + element + ") " + context);
            }
        });
    }

    protected static Collection<Integer> inAndOut(final List<Integer> elements) {
        return concat(elements, someOneOf(excludeValues(integers(), elements)));
    }

    private static <T> Collection<T> concat(final Iterable<? extends T> items1, final Iterable<? extends T> items2) {
        return Stream.concat(stream(items1), stream(items2)).collect(Collectors.toList());
    }

    private static <T> Stream<T> stream(final Iterable<T> items1) {
        return StreamSupport.stream(items1.spliterator(), false);
    }

    @Test
    public void test14_subSet() {
        test((elements, comparator, treeSet, set, context) -> {
            final Collection<Integer> all = values(elements);
            for (final Pair<Integer, Integer> p : somePairs(fixedValues(all), fixedValues(all))) {
                final Integer from = p.getFirst();
                final Integer to = p.getSecond();
                if (comparator.compare(from, to) <= 0) {
                    assertEq(
                            treeSet.subSet(from, to), set.subSet(from, to),
                            "in subSet(" + from + ", " + to + ") " + context
                    );
                }
            }
        });
    }

    protected static Collection<Integer> values(final List<Integer> elements) {
        return concat(inAndOut(elements), List.of(0, Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Test
    public void test16_first() {
        test((elements, comparator, treeSet, set, context) -> {
            if (elements.isEmpty()) {
                try {
                    set.first();
                    Assert.fail("first" + "() should throw NoSuchElementException for empty set");
                } catch (final NoSuchElementException e) {
                    // ok
                }
            } else {
                Assert.assertEquals("first" + "() (comparator = " + comparator + ", elements = " + elements + ")", treeSet.first(), treeSet.first());
            }
        });
    }
}
