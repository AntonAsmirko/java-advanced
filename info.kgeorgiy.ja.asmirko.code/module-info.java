module info.kgeorgiy.ja.asmirko.code {
    requires info.kgeorgiy.java.advanced.arrayset;
    requires info.kgeorgiy.java.advanced.walk;
    requires info.kgeorgiy.java.advanced.student;
    requires info.kgeorgiy.java.advanced.implementor;

    exports info.kgeorgiy.ja.asmirko.arrayset;
    exports info.kgeorgiy.ja.asmirko.walk;
    exports info.kgeorgiy.ja.asmirko.students;

    opens info.kgeorgiy.ja.asmirko.walk to junit;
}