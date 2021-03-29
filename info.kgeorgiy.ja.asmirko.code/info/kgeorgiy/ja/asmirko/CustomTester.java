package info.kgeorgiy.ja.asmirko;

import info.kgeorgiy.ja.asmirko.arrayset.NavigableArraySet;
import info.kgeorgiy.ja.asmirko.students.StudentDB;
import info.kgeorgiy.java.advanced.arrayset.AdvancedSetTest;
import info.kgeorgiy.java.advanced.arrayset.NavigableSetTest;
import info.kgeorgiy.java.advanced.arrayset.SortedSetTest;
import info.kgeorgiy.java.advanced.implementor.ClassJarImplementorTest;
import info.kgeorgiy.java.advanced.implementor.InterfaceImplementorTest;
import info.kgeorgiy.java.advanced.implementor.InterfaceJarImplementorTest;
import info.kgeorgiy.java.advanced.student.StudentQueryTest;
import info.kgeorgiy.java.advanced.walk.Tester;
import info.kgeorgiy.java.advanced.walk.WalkTest;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static info.kgeorgiy.java.advanced.student.GroupName.M3238;
import static info.kgeorgiy.java.advanced.student.GroupName.M3239;

public class CustomTester extends Tester {
    public static void main(String[] args) {
        StudentDB studentDB = new StudentDB();
        var res = studentDB.findStudentsByLastName(List.of(new Student(2, "?????????", "?????????", M3239), new Student(0, "?????????", "?????????", M3239), new Student(1, "?????", "???????????", M3238)), "?????????");
        new CustomTester()
                .add("Walk", WalkTest.class)
                .add("SortedSet", SortedSetTest.class)
                .add("NavigableSet", NavigableSetTest.class)
                .add("StudentDB", StudentQueryTest.class)
                .add("Implementor", InterfaceImplementorTest.class)
                .add("JarImplementor", InterfaceJarImplementorTest.class)
                .run(args);
    }
}
