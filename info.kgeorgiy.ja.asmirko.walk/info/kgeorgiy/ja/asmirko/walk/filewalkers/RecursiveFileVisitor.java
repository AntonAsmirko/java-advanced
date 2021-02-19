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
    private final BufferedWriter out;

    public RecursiveFileVisitor(HashSumAlgorithm hashSumAlgorithm, Path outStream, Charset encoding) throws IOException {
        this.hashSumAlgorithm = hashSumAlgorithm;
        out = Files.newBufferedWriter(outStream, encoding);
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
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        throw exc;
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
}
