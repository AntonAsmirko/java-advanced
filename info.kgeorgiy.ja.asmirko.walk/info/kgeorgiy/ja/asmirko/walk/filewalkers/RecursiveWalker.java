package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;

public class RecursiveWalker extends AbstractWalker {
    public RecursiveWalker(Path inputPath, Path outputPath, HashSumAlgorithm hashSumAlgorithm) throws IllegalArgumentException, IOException {
        super(inputPath, outputPath, hashSumAlgorithm);
    }

    @Override
    public void walk() throws IOException, InvalidPathException {
        BufferedReader in = Files.newBufferedReader(inputPath, encoding);
        BufferedWriter out = Files.newBufferedWriter(outputPath, encoding);
        RecursiveFileVisitor visitor = new RecursiveFileVisitor(hashSumAlgorithm);
        visitor.setOut(out);
        String file;
        while ((file = in.readLine()) != null) {
            try {
                Path startPoint = Paths.get(file);
                startPoint.normalize();
                Files.walkFileTree(startPoint, visitor);
            } catch (IOException | InvalidPathException  e) {
                out.write(String.format("%016x %s", 0L, file));
                out.newLine();
            }
        }
        in.close();
        out.close();
        visitor.close();
    }
}
