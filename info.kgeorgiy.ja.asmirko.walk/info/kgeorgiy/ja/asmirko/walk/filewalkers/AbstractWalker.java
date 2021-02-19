package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public abstract class AbstractWalker implements Walker {

    protected Path inputPath;
    protected Path outputPath;
    protected HashSumAlgorithm hashSumAlgorithm;
    protected Charset encoding = StandardCharsets.UTF_8;
    protected boolean log = false;

    public AbstractWalker(Path inputPath, Path outputPath, HashSumAlgorithm hashSumAlgorithm) throws IllegalArgumentException, IOException {
        if (Files.notExists(inputPath)) {
            throw new IllegalArgumentException("Input file does not exist");
        }
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.hashSumAlgorithm = hashSumAlgorithm;
        if (Files.notExists(outputPath)) {
            Files.createFile(outputPath);
        }
    }
}
