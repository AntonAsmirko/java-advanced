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
        NavigableArraySet<Integer> navigable = new NavigableArraySet<>(List.of(-1998717303, 21089083, -237823223, -1647293387, 1449588870, 1143605045, -1831513293, -1981584842, -103215478), (o1, o2) -> 0);
        var tmp = navigable.floor(-1998717304);
        //-759072671
        new CustomTester()
                .add("Walk", WalkTest.class)
                .add("SortedSet", SortedSetTest.class)
                .add("NavigableSet", NavigableSetTest.class)
                .add("StudentDB", StudentQueryTest.class)
                .run(args);
    }
}
