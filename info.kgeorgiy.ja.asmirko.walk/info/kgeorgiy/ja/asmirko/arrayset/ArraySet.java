package info.kgeorgiy.ja.asmirko.arrayset;

import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements SortedSet<T> {

    protected final ArrayList<T> data;
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

    public ArraySet(Collection<T> collection, Comparator<? super T> comparator) {
        this.comparator = comparator;
        TreeSet<T> set;
        if (collection != null) {
            set = new TreeSet<>(comparator);
            set.addAll(collection);
        } else {
            set = new TreeSet<>();
        }
        data = new ArrayList<>(set);
    }


    private Pair<T, Integer> lowerInternal(T t) {
        int closestPos = find(t);
        if (closestPos == -1 || closestPos == 0) {
            return null;
        } else if (closestPos > 0) {
            return new Pair<T, Integer>(data.get(closestPos - 1), closestPos - 1);
        } else {
            return new Pair<T, Integer>(data.get(Math.abs(closestPos) - 2), Math.abs(closestPos) - 2);
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
        if (closestPos >= 0 && data.get(closestPos).equals(t)) {
            return new Pair<>(data.get(closestPos), closestPos);
        }
        if (closestPos == -1) {
            return null;
        } else if (closestPos >= 0) {
            return new Pair<>(data.get(closestPos), closestPos);
        } else {
            return new Pair<>(data.get(Math.abs(closestPos) - 2), Math.abs(closestPos) - 2);
        }
    }

    protected T floor(T t) {
        Pair<T, Integer> result = floorInternal(t);
        if (result == null) {
            return null;
        }
        return result.getFirst();
    }

    private Pair<T, Integer> ceilingInternal(T t) {
        int closestPos = find(t);
        if (closestPos >= 0 && data.get(closestPos).equals(t)) {
            return new Pair<>(data.get(closestPos), closestPos);
        }
        if (closestPos < -data.size()) {
            return null;
        } else if (closestPos >= 0) {
            return new Pair<>(data.get(closestPos), closestPos);
        } else {
            return new Pair<>(data.get(Math.abs(closestPos) - 1), Math.abs(closestPos) - 1);
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
        if (closestPos < -data.size() || closestPos == data.size() - 1) {
            return null;
        } else if (closestPos >= 0) {
            return new Pair<>(data.get(closestPos + 1), closestPos + 1);
        } else {
            return new Pair<>(data.get(Math.abs(closestPos) - 1), Math.abs(closestPos) - 1);
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
        return Collections.binarySearch(data, (T) e, comparator);
    }

    @Override
    public boolean contains(Object e) {
        return find(e) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArraySetIterator<>(this.data);
    }

    @Override
    public int size() {
        return data.size();
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
                return new ArraySet<T>(this.comparator);
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
        return new ArraySet<>(data.subList(lBound.second, rBound.second + 1), comparator());
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false, true);
    }

    public SortedSet<T> headSet(T toElement, Boolean toInclusive, Boolean willBeExceptionThrown) {
        if (isEmpty() || Objects.requireNonNull(comparator()).compare(toElement, data.get(0)) <= 0 && !toInclusive) {
            return new ArraySet<>(comparator());
        }
        return subSet(data.get(0), true, toElement, toInclusive, willBeExceptionThrown);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    public SortedSet<T> tailSet(T fromElement, Boolean fromInclusive) {
        if (isEmpty()) {
            return new ArraySet<>(comparator());
        }
        Pair<LinkedList<T>, ArraySetIterator<T>> arrAndIt = makeIteratorAndSublist(fromElement, fromInclusive);
        LinkedList<T> sublist = arrAndIt.first;
        ArraySetIterator<T> it = arrAndIt.second;
        T last = data.get(size() - 1);
        if (comparator() != null && comparator().compare(last, fromElement) < 0) {
            return new ArraySet<>(comparator);
        }
        while (it.hasNext()) {
            sublist.add(it.next());
        }
        return new ArraySet<>(sublist, comparator);
    }

    private Pair<LinkedList<T>, ArraySetIterator<T>> makeIteratorAndSublist(T fromElement, Boolean fromInclusice) {
        LinkedList<T> sublist = new LinkedList<>();
        int startPos = find(fromElement);
        if (startPos < 0) {
            startPos = startPos * -1 - 1;
        } else if (!fromInclusice) {
            startPos = startPos + 1;
        }
        ArraySetIterator<T> it = new ArraySetIterator<>(data, startPos);
        return new Pair<>(sublist, it);
    }

    @Override
    public T first() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return data.get(0);
    }

    @Override
    public T last() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return data.get(size() - 1);
    }

    private static class ArraySetIterator<T> implements Iterator<T> {

        private final ArrayList<T> data;
        private int posBefore;

        public ArraySetIterator(ArrayList<T> data) {
            this(data, 0);
        }

        private ArraySetIterator(ArrayList<T> data, int posBefore) {
            this.data = data;
            this.posBefore = posBefore;
        }

        @Override
        public boolean hasNext() {
            return posBefore < data.size();
        }

        @Override
        public T next() {
            return data.get(posBefore++);
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