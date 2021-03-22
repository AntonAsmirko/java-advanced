package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import java.io.IOException;
import java.nio.file.InvalidPathException;

public interface Walker {
    public void walk() throws IOException, InvalidPathException;
}
