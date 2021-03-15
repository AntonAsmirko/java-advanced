package info.kgeorgiy.ja.asmirko;

import info.kgeorgiy.ja.asmirko.arrayset.ArraySet;
import info.kgeorgiy.ja.asmirko.arrayset.NavigableArraySet;
import info.kgeorgiy.java.advanced.arrayset.NavigableSetTest;
import info.kgeorgiy.java.advanced.arrayset.SortedSetTest;
import info.kgeorgiy.java.advanced.student.StudentQueryTest;
import info.kgeorgiy.java.advanced.walk.Tester;
import info.kgeorgiy.java.advanced.walk.WalkTest;

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;

public class CustomTester extends Tester {
    public static void main(String[] args) {
        NavigableArraySet<Integer> navigable = new NavigableArraySet<>(List.of(-2029479006,
                146719081,
                224429096,
                449365419,
                1208036781,
                1519891101
        ), Comparator.naturalOrder());
        var tmp = navigable.headSet(-2029479007);
        new CustomTester()
                .add("Walk", WalkTest.class)
                .add("SortedSet", SortedSetTest.class)
                .add("NavigableSet", NavigableSetTest.class)
                .add("StudentDB", StudentQueryTest.class)
                .run(args);
    }
}
