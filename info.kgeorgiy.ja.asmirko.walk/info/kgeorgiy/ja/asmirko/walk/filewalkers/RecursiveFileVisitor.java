package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveFileVisitor implements FileVisitor<Path>, Closeable {

    private HashSumAlgorithm hashSumAlgorithm;
    private BufferedWriter out;

    public RecursiveFileVisitor(HashSumAlgorithm hashSumAlgorithm) {
        this.hashSumAlgorithm = hashSumAlgorithm;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        long hash = 0L;
        try {
            hash = hashSumAlgorithm.hashOfFile(file);
        } catch (IOException | InvalidPathException ignored) {
        } finally {
            out.write(String.format("%016x %s", hash, file));
            out.newLine();
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        if (exc instanceof NoSuchFileException) {
            out.write(String.format("%016x %s", 0L, file));
            out.newLine();
            return FileVisitResult.CONTINUE;
        } else {
            throw exc;
        }
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public void close() throws IOException {
        hashSumAlgorithm = null;
        out.close();
    }

    public void setOut(BufferedWriter bw) {
        out = bw;
    }
}
