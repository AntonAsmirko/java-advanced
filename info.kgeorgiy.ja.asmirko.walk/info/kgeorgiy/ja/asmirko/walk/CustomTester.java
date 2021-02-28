package info.kgeorgiy.ja.asmirko.walk;

import info.kgeorgiy.java.advanced.arrayset.SortedSetTest;
import info.kgeorgiy.java.advanced.walk.Tester;

public class CustomTester extends Tester {

    public static void main(String[] args) {
        new CustomTester()
                .add("ArraySet", SortedSetTest.class)
                .run(args);
    }
}
