package info.kgeorgiy.ja.asmirko.arrayset;

import java.util.Iterator;
import java.util.NavigableSet;

public class NavigableArraySet<T> extends ArraySet<T> implements NavigableSet<T> {
    @Override
    public T lower(T t) {
        int closestPos = find(t);
        if (closestPos == -1 || closestPos == 0) {
            return null;
        } else if (closestPos > 0) {
            return data.get(closestPos - 1);
        } else {
            return data.get(Math.abs(closestPos));
        }
    }

    @Override
    public T floor(T t) {
        int closestPos = find(t);
        if (closestPos >= 0 && data.get(closestPos).equals(t)) {
            return t;
        }
        if (closestPos == -1 || closestPos == 0) {
            return null;
        } else if (closestPos > 0) {
            return data.get(closestPos - 1);
        } else {
            return data.get(Math.abs(closestPos));
        }
    }

    @Override
    public T ceiling(T t) {
        int closestPos = find(t);
        if (closestPos >= 0 && data.get(closestPos).equals(t)) {
            return t;
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
        return (NavigableSet<T>) super.subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        return (NavigableSet<T>) super.headSet(toElement, inclusive);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        return (NavigableSet<T>) super.tailSet(fromElement, inclusive);
    }
}
