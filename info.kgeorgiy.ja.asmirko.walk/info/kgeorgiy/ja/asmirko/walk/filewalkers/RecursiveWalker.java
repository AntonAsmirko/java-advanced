package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
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
        RecursiveFileVisitor visitor = new RecursiveFileVisitor(hashSumAlgorithm);
        BufferedWriter out = Files.newBufferedWriter(outputPath, encoding);
        visitor.setOut(out);
        for (Iterator<String> it = Files.lines(inputPath, encoding).iterator(); it.hasNext(); ) {
            String line = it.next();
            try {
                Path startPoint = Paths.get(line);
                startPoint.normalize();
                Files.walkFileTree(startPoint, visitor);
            } catch (IOException | InvalidPathException  e) {
                out.write(String.format("%016x %s", 0L, line));
                out.newLine();
            }
        }
        out.close();
        visitor.close();
    }
}
