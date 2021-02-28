package info.kgeorgiy.ja.asmirko.arrayset;

import java.util.*;

public class ArraySet {

    public static void main(String[] args) {
        List<Integer> tmp = new ArrayList<>();
        tmp.add(1);
        printCollection(tmp);
    }

    static void printCollection(Collection<? extends Number> c) {
        for (Object e : c) {
            System.out.println(e);
        }
    }

}
