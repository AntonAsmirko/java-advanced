package info.kgeorgiy.ja.asmirko.walk;

import info.kgeorgiy.ja.asmirko.walk.filewalkers.FileWalker;
import info.kgeorgiy.ja.asmirko.walk.hashsum.PJWHashSum;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Walk {
    public static void main(String[] args){
        if (args.length < 2 || args[0] == null || args[1] == null) {
            return;
        }
        Path inFile = Path.of(args[0]);
        Path outFile = Path.of(args[1]);

        FileWalker fileWalker = new FileWalker(inFile, outFile, new PJWHashSum());
        fileWalker.setLog(true);
        try {
            fileWalker.walk();
        } catch (IOException | InvalidPathException e){
            //e.printStackTrace();
        }
    }
}