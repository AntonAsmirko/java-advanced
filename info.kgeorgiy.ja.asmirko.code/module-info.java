module info.kgeorgiy.ja.asmirko.code {
    requires info.kgeorgiy.java.advanced.arrayset;
    requires info.kgeorgiy.java.advanced.walk;
    requires info.kgeorgiy.java.advanced.student;
    requires info.kgeorgiy.java.advanced.implementor;
    requires java.compiler;
    requires info.kgeorgiy.java.advanced.concurrent;

    exports info.kgeorgiy.ja.asmirko.arrayset;
    exports info.kgeorgiy.ja.asmirko.walk;
    exports info.kgeorgiy.ja.asmirko.students;
    exports info.kgeorgiy.ja.asmirko.implementor;
    exports info.kgeorgiy.ja.asmirko.concurrent;

    opens info.kgeorgiy.ja.asmirko.walk to junit;
}