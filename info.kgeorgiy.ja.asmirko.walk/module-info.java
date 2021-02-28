module info.kgeorgiy.ja.asmirko.walk {
    requires transitive info.kgeorgiy.java.advanced.walk;
    requires info.kgeorgiy.java.advanced.arrayset;

    exports info.kgeorgiy.ja.asmirko.walk;

    opens info.kgeorgiy.ja.asmirko.walk to junit;
}