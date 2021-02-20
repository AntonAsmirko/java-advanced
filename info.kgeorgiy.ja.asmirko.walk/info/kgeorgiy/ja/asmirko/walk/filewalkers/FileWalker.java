package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;

public class FileWalker extends AbstractWalker {

    public FileWalker(Path inputPath, Path outputPath, HashSumAlgorithm hashSumAlgorithm) throws IllegalArgumentException, IOException {
        super(inputPath, outputPath, hashSumAlgorithm);
    }

    @Override
    public void walk() throws IOException, InvalidPathException {
        List<String> files = Files.readAllLines(inputPath, encoding);
        BufferedWriter out = Files.newBufferedWriter(outputPath, encoding);
        for (String file : files) {
            long hash = 0L;
            try {
                var path = Paths.get(file);
                path.normalize();
                hash = hashSumAlgorithm.hashOfFile(path);
            } catch (IOException | InvalidPathException e) {
                if (log) e.printStackTrace();
            } finally {
                out.write(String.format("%016x %s", hash, file));
                out.newLine();
            }
        }
        out.close();
    }
}