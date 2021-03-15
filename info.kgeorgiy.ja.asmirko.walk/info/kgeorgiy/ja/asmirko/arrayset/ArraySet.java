package info.kgeorgiy.ja.asmirko.arrayset;

import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements SortedSet<T> {

    protected final T[] data;
    protected final int l, r;
    protected final Comparator<? super T> comparator;

    public ArraySet() {
        this(null, null);
    }

    public ArraySet(Collection<T> collection) {
        this(collection, null);
    }

    public ArraySet(Comparator<? super T> comparator) {
        this(null, comparator);
    }

    @SuppressWarnings("unchecked")
    public ArraySet(Collection<T> collection, Comparator<? super T> comparator) {
        this.comparator = comparator;
        TreeSet<T> set;
        if (collection != null) {
            set = new TreeSet<>(comparator);
            set.addAll(collection);
        } else {
            set = new TreeSet<>();
        }
        data = (T[])(new ArrayList<>(set)).toArray();
        this.l = 0;
        this.r = collection != null ? collection.size() : 0;
    }

    protected ArraySet(T[] array, Comparator<? super T> comparator, int l, int r){
        this.data = array;
        this.l = l;
        this.r = r;
        this.comparator = comparator;
    }

    private Pair<T, Integer> lowerInternal(T t) {
        int closestPos = find(t);
        if (closestPos == -1 || closestPos == 0) {
            return null;
        } else if (closestPos > 0) {
            return new Pair<T, Integer>(data[l + closestPos - 1], closestPos - 1);
        } else {
            return new Pair<T, Integer>(data[l + Math.abs(closestPos) - 2], Math.abs(closestPos) - 2);
        }
    }

    protected T lower(T t) {
        Pair<T, Integer> result = lowerInternal(t);
        if (result == null) {
            return null;
        }
        return result.getFirst();
    }

    private Pair<T, Integer> floorInternal(T t) {
        int closestPos = find(t);
        if (closestPos >= 0 && data[l + closestPos].equals(t)) {
            return new Pair<>(data[l + closestPos], closestPos);
        }
        if (closestPos == -1) {
            return null;
        } else if (closestPos >= 0) {
            return new Pair<>(data[l + closestPos], closestPos);
        } else {
            return new Pair<>(data[l + Math.abs(closestPos) - 2], Math.abs(closestPos) - 2);
        }
    }

    protected T floor(T t) {
        Pair<T, Integer> result = floorInternal(t);
        if (result == null) {
            return null;
        }
        return result.getFirst();
    }

    private int segmentLength(){ return this.r - this.l; }

    private Pair<T, Integer> ceilingInternal(T t) {
        int closestPos = find(t);
        if (closestPos >= 0 && data[l + closestPos].equals(t)) {
            return new Pair<>(data[l + closestPos], closestPos);
        }
        if (closestPos < -segmentLength()) {
            return null;
        } else if (closestPos >= 0) {
            return new Pair<>(data[l + closestPos], closestPos);
        } else {
            return new Pair<>(data[l + Math.abs(closestPos) - 1], Math.abs(closestPos) - 1);
        }
    }

    protected T ceiling(T t) {
        Pair<T, Integer> result = ceilingInternal(t);
        if (result == null) {
            return null;
        }
        return result.getFirst();
    }

    private Pair<T, Integer> higherInternal(T t) {
        int closestPos = find(t);
        if (closestPos < -segmentLength() || closestPos == segmentLength() - 1) {
            return null;
        } else if (closestPos >= 0) {
            return new Pair<>(data[l + closestPos + 1], closestPos + 1);
        } else {
            return new Pair<>(data[l + Math.abs(closestPos) - 1], Math.abs(closestPos) - 1);
        }
    }

    protected T higher(T t) {
        Pair<T, Integer> result = higherInternal(t);
        if (result == null) {
            return null;
        }
        return result.getFirst();
    }

    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    protected int find(Object e) {
        return Arrays.binarySearch(data, l, r, (T) e, comparator);
    }

    @Override
    public boolean contains(Object e) {
        return find(e) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArraySetIterator<T>(this.data, this.l, this.r);
    }

    @Override
    public int size() {
        return segmentLength();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false, true);
    }

    public SortedSet<T> subSet(T fromElement, Boolean fromInclusive,
                               T toElement, Boolean toInclusive, Boolean willBeExceptionThrown)
            throws IllegalArgumentException {
        if (comparator() != null && comparator().compare(fromElement, toElement) > 0) {
            if (!willBeExceptionThrown) {
                return new ArraySet<T>(comparator());
            } else {
                throw new IllegalArgumentException();
            }
        }
        Pair<T, Integer> lBound, rBound;

        if (fromInclusive) {
            lBound = ceilingInternal(fromElement);
        } else {
            lBound = higherInternal(fromElement);
        }
        if (toInclusive) {
            rBound = floorInternal(toElement);
        } else {
            rBound = lowerInternal(toElement);
        }
        if (lBound == null || rBound == null || lBound.second > rBound.second) {
            return new ArraySet<>(comparator());
        }
        return new ArraySet<>(data, comparator(), lBound.second, rBound.second);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false, true);
    }

    public SortedSet<T> headSet(T toElement, Boolean toInclusive, Boolean willBeExceptionThrown) {
        if (isEmpty() || Objects.requireNonNull(comparator()).compare(toElement, data[l]) <= 0 && !toInclusive) {
            return new ArraySet<>(comparator());
        }
        return subSet(data[l], true, toElement, toInclusive, willBeExceptionThrown);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    public SortedSet<T> tailSet(T fromElement, Boolean fromInclusive) {
        if (isEmpty()) {
            return new ArraySet<>(comparator());
        }
        Pair<T, Integer> lBound;

        if (fromInclusive) {
            lBound = ceilingInternal(fromElement);
        } else {
            lBound = higherInternal(fromElement);
        }

        if (lBound == null) {
            return new ArraySet<>(comparator());
        }

        return new ArraySet<>(data, comparator(), lBound.second, segmentLength());
    }

    @Override
    public T first() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return data[l];
    }

    @Override
    public T last() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return data[l + size() - 1];
    }

    private static class ArraySetIterator<T> implements Iterator<T> {

        private final T[] data;
        private int posBefore;
        private final int r;

        public ArraySetIterator(T[] data, int l, int r) {
            this.data = data;
            this.r = r;
            this.posBefore = l;
        }

        @Override
        public boolean hasNext() {
            return posBefore < r;
        }

        @Override
        public T next() {
            return data[posBefore++];
        }
    }

    protected static class Pair<F, S> {
        private final F first;
        private final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }
    }
}