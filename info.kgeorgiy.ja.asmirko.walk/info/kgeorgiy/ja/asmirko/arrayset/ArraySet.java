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
        if (comparator() != null && comparator().compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        Pair<LinkedList<T>, ArraySetIterator<T>> arrAndIt = makeIteratorAndSublist(fromElement);
        LinkedList<T> sublist = arrAndIt.first;
        ArraySetIterator<T> it = arrAndIt.second;
        while (it.hasNext()) {
            T item = it.next();
            if (comparator() != null && comparator().compare(item, toElement) >= 0) {
                break;
            }
            sublist.add(item);
        }
        return new ArraySet<>(sublist, comparator);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        if (isEmpty() || Objects.requireNonNull(comparator()).compare(toElement, data.get(0)) <= 0) {
            return new ArraySet<>(comparator());
        }
        return subSet(data.get(0), toElement);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if (isEmpty()) {
            return new ArraySet<>(comparator());
        }
        Pair<LinkedList<T>, ArraySetIterator<T>> arrAndIt = makeIteratorAndSublist(fromElement);
        LinkedList<T> sublist = arrAndIt.first;
        ArraySetIterator<T> it = arrAndIt.second;
        T last = data.get(size() - 1);
        if (comparator() != null && comparator().compare(last, fromElement) < 0){
            return new  ArraySet<>(comparator);
        }
        while (it.hasNext()) {
            sublist.add(it.next());
        }
        return new ArraySet<>(sublist, comparator);
    }

    private Pair<LinkedList<T>, ArraySetIterator<T>> makeIteratorAndSublist(T fromElement){
        LinkedList<T> sublist = new LinkedList<>();
        int startPos = find(fromElement);
        if (startPos < 0){
            startPos = startPos * -1 - 1;
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

    private static class Pair<F, S>{
        private final F first;
        private final S second;

        public Pair(F first, S second){
            this.first = first;
            this.second = second;
        }
    }
}