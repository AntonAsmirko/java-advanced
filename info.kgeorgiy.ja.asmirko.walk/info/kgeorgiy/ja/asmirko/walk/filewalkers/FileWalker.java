package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Iterator;

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
        Iterator<String> hashes = Files.lines(inputPath)
                .map(Path::of)
                .peek(Path::normalize)
                .map(path -> {
                    long hash = 0L;
                    try {
                        hash = hashSumAlgorithm.hashOfFile(path);
                    } catch (IOException e) {
                        if(log) e.printStackTrace();
                    }
                    return String.format("%016x %s", hash, path.toString());
                }).iterator();
        try (var out = Files.newBufferedWriter(outputPath, encoding, StandardOpenOption.WRITE)) {
            while (hashes.hasNext()) {
                out.write(hashes.next());
            }
        }
    }

    public void setLog(Boolean log){
        this.log = log;
    }
}
