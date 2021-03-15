package info.kgeorgiy.ja.asmirko.arrayset;

import java.util.*;

public class NavigableArraySet<T> extends ArraySet<T> implements NavigableSet<T> {

    public NavigableArraySet() {
        super();
    }

    public NavigableArraySet(Collection<T> collection) {
        super(collection);
    }

    public NavigableArraySet(T[] array, Comparator<? super T> comparator, int l, int r){
        super(array, comparator, l, r);
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
        return new NavigableArraySet<T>(data, comparator != null ? comparator().reversed() : null, this.l, this.r);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return new DescendingIterator();
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        ArraySet<T> resToCast = (ArraySet<T>) super.subSet(fromElement, fromInclusive, toElement, toInclusive, false);
        return new NavigableArraySet<>(resToCast.data, resToCast.comparator(), resToCast.l, resToCast.r);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        ArraySet<T> resToCast = (ArraySet<T>)super.headSet(toElement, inclusive, false);
        return new NavigableArraySet<>(resToCast.data, resToCast.comparator(), resToCast.l, resToCast.r);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        ArraySet<T> resToCast =(ArraySet<T>) super.tailSet(fromElement, inclusive);
        return new NavigableArraySet<>(resToCast.data, resToCast.comparator(), resToCast.l, resToCast.r);
    }

    private class DescendingIterator implements Iterator<T> {

        private int posAfter = data.length;

        @Override
        public boolean hasNext() {
            return posAfter != -1;
        }

        @Override
        public T next() {
            return data[--posAfter];
        }
    }
}
