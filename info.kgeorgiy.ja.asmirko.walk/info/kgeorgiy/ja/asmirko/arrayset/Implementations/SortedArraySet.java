package info.kgeorgiy.ja.asmirko.arrayset.Implementations;

import java.util.*;

public class SortedArraySet<T> extends AbstractSet<T> implements SortedSet<T> {

    private ArrayList<T> data = new ArrayList<>();
    private int size = 0;
    private Comparator<? super T> comparator;

    public SortedArraySet(){}

    public SortedArraySet(Collection<T> collection){

    }

    public SortedArraySet(Collection<T> collection, Comparator<? super T> comparator){
        this.comparator = comparator;
        data.addAll(collection);
        if(this.comparator != null){
            data.sort(comparator);
        } else {
            
            List<Comparable<T>> dataToSort = (List<Comparable<T>>)data;
        }
    }

    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object e) {
        if (comparator != null) {

        }
        @SuppressWarnings("unchecked")
        ArrayList<Comparable<? super T>> searchPool = (ArrayList<Comparable<? super T>>) data;
        @SuppressWarnings("unchecked")
        T element = (T) e;
        int result = Collections.binarySearch(searchPool, element);
        return result >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return null;
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return null;
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return null;
    }

    @Override
    public T first() {
        return null;
    }

    @Override
    public T last() {
        return null;
    }
}
