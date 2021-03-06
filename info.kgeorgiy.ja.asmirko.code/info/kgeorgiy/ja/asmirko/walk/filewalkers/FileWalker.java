package info.kgeorgiy.ja.asmirko.walk.filewalkers;

import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;

import java.io.BufferedReader;
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
        BufferedReader in = Files.newBufferedReader(inputPath);
        BufferedWriter out = Files.newBufferedWriter(outputPath);
        String file;
        while ((file = in.readLine()) != null) {
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
        in.close();
        out.close();
    }
}