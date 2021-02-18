package info.kgeorgiy.ja.asmirko.walk;

import info.kgeorgiy.java.advanced.walk.Tester;
import info.kgeorgiy.java.advanced.walk.WalkTest;

public class CustomTester extends Tester {

    public static void main(String[] args) {
        new CustomTester()
                .add("WalkTest", WalkTest.class)
                .run(args);
    }
}
