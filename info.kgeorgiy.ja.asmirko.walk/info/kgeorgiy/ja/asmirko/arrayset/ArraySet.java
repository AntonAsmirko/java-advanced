package info.kgeorgiy.ja.asmirko.arrayset;

import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements SortedSet<T> {

    private final ArrayList<T> data;
    private final Comparator<? super T> comparator;

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

    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private int find(Object e) {
        return Collections.binarySearch(data, (T) e, comparator);
    }

    @Override
    public boolean contains(Object e) {
        return find(e) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArraySetIterator<T>(this.data);
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
    public SortedSet<T> subSet(T fromElement, T toElement) throws IllegalArgumentException {
        if (comparator() != null && comparator().compare(fromElement, toElement) <= 0) {
            throw new IllegalArgumentException();
        }
        LinkedList<T> sublist = new LinkedList<>();
        int startPos = find(fromElement);
        ArraySetIterator<T> it = new ArraySetIterator<>(data, Math.max(startPos - 1, -1));
        while (it.hasNext()) {
            T item = it.next();
            if (item.equals(toElement)) {
                break;
            }
            sublist.add(item);
        }
        return new ArraySet<>(sublist, comparator);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        if (isEmpty()) {
            return new ArraySet<>();
        }
        return subSet(data.get(0), toElement);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if (isEmpty()) {
            return new ArraySet<>();
        }
        LinkedList<T> sublist = new LinkedList<>();
        int startPos = find(fromElement);
        ArraySetIterator<T> it = new ArraySetIterator<>(data, Math.max(startPos - 1, -1));
        while (it.hasNext()) {
            sublist.add(it.next());
        }
        return new ArraySet<>(sublist, comparator);
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
            this(data, -1);
        }

        private ArraySetIterator(ArrayList<T> data, int posBefore) {
            this.data = data;
            this.posBefore = posBefore;
        }

        @Override
        public boolean hasNext() {
            return posBefore < data.size() - 1;
        }

        @Override
        public T next() {
            posBefore++;
            return data.get(posBefore);
        }
    }
}