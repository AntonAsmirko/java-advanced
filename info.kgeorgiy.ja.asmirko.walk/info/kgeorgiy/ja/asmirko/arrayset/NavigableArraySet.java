package info.kgeorgiy.ja.asmirko.arrayset;

import java.util.*;

public class NavigableArraySet<T> extends ArraySet<T> implements NavigableSet<T> {

    public NavigableArraySet() {
        super();
    }

    public NavigableArraySet(Collection<T> collection) {
        super(collection);
    }

    public NavigableArraySet(Collection<T> collection, Comparator<? super T> comparator) {
        super(collection, comparator);
    }

    public NavigableArraySet(Comparator<? super T> comparator) {
        super(comparator);
    }

    @Override
    public T lower(T t) {
        return super.lower(t);
    }

    @Override
    public T floor(T t) {
        return super.floor(t);
    }

    @Override
    public T ceiling(T t) {
        return super.ceiling(t);
    }

    @Override
    public T higher(T t) {
        return super.higher(t);
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException("pollFirst operation is not supported");
    }

    @Override
    public T pollLast() {
        throw new UnsupportedOperationException("pollLast operation is not supported");
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new NavigableArraySet<>(data, comparator != null ? comparator.reversed() : null);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new DescendingIterator();
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        SortedSet<T> resToCast = super.subSet(fromElement, fromInclusive, toElement, toInclusive, false);
        return new NavigableArraySet<>(resToCast, resToCast.comparator());
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        SortedSet<T> resToCast = super.headSet(toElement, inclusive, false);
        return new NavigableArraySet<>(resToCast, resToCast.comparator());
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        SortedSet<T> resToCast = super.tailSet(fromElement, inclusive);
        return new NavigableArraySet<>(resToCast, resToCast.comparator());
    }

    private class DescendingIterator implements Iterator<T> {

        private int posAfter = data.size();

        @Override
        public boolean hasNext() {
            return posAfter != -1;
        }

        @Override
        public T next() {
            return data.get(--posAfter);
        }
    }
}
