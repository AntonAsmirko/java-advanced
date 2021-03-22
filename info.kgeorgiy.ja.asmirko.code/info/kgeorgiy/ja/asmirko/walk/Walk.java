package info.kgeorgiy.ja.asmirko.walk;

import info.kgeorgiy.ja.asmirko.walk.filewalkers.FileWalker;
import info.kgeorgiy.ja.asmirko.walk.filewalkers.RecursiveWalker;
import info.kgeorgiy.ja.asmirko.walk.filewalkers.Walker;
import info.kgeorgiy.ja.asmirko.walk.hashsum.PJWHashSum;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Walk {
    public static void main(String[] args) {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            return;
        }

        Path inFile;
        Path outFile;

        try {
            inFile = Path.of(args[0]);
            outFile = Path.of(args[1]);
            Walker fileWalker = new RecursiveWalker(inFile, outFile, new PJWHashSum());
            fileWalker.walk();
        } catch (IOException | IllegalArgumentException e) {
            //if (e instanceof IllegalArgumentException) e.printStackTrace();
        }
    }
}