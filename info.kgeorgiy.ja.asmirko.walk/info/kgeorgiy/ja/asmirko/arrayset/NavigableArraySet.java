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
        int closestPos = find(t);
        if (closestPos == -1 || closestPos == 0) {
            return null;
        } else if (closestPos > 0) {
            return data.get(closestPos - 1);
        } else {
            return data.get(Math.abs(closestPos) - 2);
        }
    }

    @Override
    public T floor(T t) {
        int closestPos = find(t);
        if (closestPos >= 0 && data.get(closestPos).equals(t)) {
            return t;
        }
        if (closestPos == -1) {
            return null;
        } else if (closestPos >= 0) {
            return data.get(closestPos);
        } else {
            return data.get(Math.abs(closestPos) - 2);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T ceiling(T t) {
        int closestPos = find(t);
        if (closestPos >= 0) {
            T foundItem = data.get(closestPos);
            if (comparator != null && comparator.compare(foundItem, t) == 0) {
                return t;
            } else {
                Comparable<? super T> tCmp = (Comparable<? super T>) t;
                if (tCmp.compareTo(foundItem) == 0) {
                    return t;
                }
            }
        }
        if (closestPos == -data.size() || closestPos == data.size() - 1) {
            return null;
        } else if (closestPos > 0) {
            return data.get(closestPos + 1);
        } else {
            return data.get(Math.abs(closestPos));
        }
    }

    @Override
    public T higher(T t) {
        int closestPos = find(t);
        if (closestPos == -data.size() || closestPos == data.size() - 1) {
            return null;
        } else if (closestPos > 0) {
            return data.get(closestPos + 1);
        } else {
            return data.get(Math.abs(closestPos));
        }
    }

    @Override
    public T pollFirst() {
        if (isEmpty())
            return null;
        return data.remove(0);
    }

    @Override
    public T pollLast() {
        if (isEmpty())
            return null;
        return data.remove(data.size() - 1);
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return null;
    }

    @Override
    public Iterator<T> descendingIterator() {
        return null;
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
}
