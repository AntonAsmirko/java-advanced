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
        NavigableArraySet<Integer> navigable = new NavigableArraySet<>(List.of(-759072671, 1642200940, -36850366, 1382247506, -1809700816, 284771413, 1155133328, -878217284), Comparator.naturalOrder());
        var tmp = navigable.lower(-759072672);
        //-759072671
        new CustomTester()
                .add("Walk", WalkTest.class)
                .add("SortedSet", SortedSetTest.class)
                .add("NavigableSet", NavigableSetTest.class)
                .add("StudentDB", StudentQueryTest.class)
                .run(args);
    }
}
