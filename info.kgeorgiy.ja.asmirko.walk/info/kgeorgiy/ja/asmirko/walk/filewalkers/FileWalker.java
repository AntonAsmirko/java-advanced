package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;

public class FileWalker {

    private Path inputPath;
    private Path outputPath;
    private HashSumAlgorithm hashSumAlgorithm;
    private Charset encoding = StandardCharsets.UTF_8;
    private boolean log = false;

    public FileWalker(Path inputPath, Path outputPath, HashSumAlgorithm hashSumAlgorithm) throws IllegalArgumentException {
        if (Files.notExists(inputPath)) {
            throw new IllegalArgumentException("Input file does not exist");
        }
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.hashSumAlgorithm = hashSumAlgorithm;
    }

    public void walk() throws IOException, InvalidPathException {
        if (Files.notExists(outputPath)) {
            Files.createFile(outputPath);
        }
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