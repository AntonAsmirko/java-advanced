module info.kgeorgiy.ja.asmirko.walk {
    requires info.kgeorgiy.java.advanced.arrayset;
    requires info.kgeorgiy.java.advanced.walk;
    requires info.kgeorgiy.java.advanced.student;

    exports info.kgeorgiy.ja.asmirko.arrayset;
    exports info.kgeorgiy.ja.asmirko.walk;
    exports info.kgeorgiy.ja.asmirko.students;

    opens info.kgeorgiy.ja.asmirko.walk to junit;
}