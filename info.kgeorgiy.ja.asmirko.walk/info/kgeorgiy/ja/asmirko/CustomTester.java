package info.kgeorgiy.ja.asmirko;

import info.kgeorgiy.ja.asmirko.arrayset.ArraySet;
import info.kgeorgiy.java.advanced.arrayset.SortedSetTest;
import info.kgeorgiy.java.advanced.walk.Tester;
import info.kgeorgiy.java.advanced.walk.WalkTest;

import java.util.Comparator;
import java.util.List;

public class CustomTester extends Tester {

    public static void main(String[] args) {
        new CustomTester()
                .add("Walk", WalkTest.class)
                .add("SortedSet", SortedSetTest.class)
                .run(args);
    }
}
