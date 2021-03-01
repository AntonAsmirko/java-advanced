package info.kgeorgiy.ja.asmirko.walk;

import info.kgeorgiy.ja.asmirko.arrayset.ArraySet;
import info.kgeorgiy.ja.asmirko.walk.Tests.CustomTest;
import info.kgeorgiy.java.advanced.arrayset.SortedSetTest;
import info.kgeorgiy.java.advanced.arrayset.Tester;

import java.util.Comparator;
import java.util.List;

public class CustomTester extends Tester {

    public static void main(String[] args) {
        ArraySet<Integer> tmp = new ArraySet<Integer>(List.of(-1150527011, -907990455, 1479313065, -1858097033, -241732394, -1618266772, 1582437722, 1390312079), Comparator.reverseOrder());
        System.out.println(tmp.headSet(1582437723));
        new CustomTester()
                .add("ArraySet", SortedSetTest.class)
                .run(args);
    }
}
