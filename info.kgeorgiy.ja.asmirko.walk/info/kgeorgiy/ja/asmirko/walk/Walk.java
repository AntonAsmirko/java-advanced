package info.kgeorgiy.ja.asmirko.walk;

import info.kgeorgiy.ja.asmirko.walk.exceptions.SmoothExitException;
import info.kgeorgiy.ja.asmirko.walk.hashsum.HashSumAlgorithm;
import info.kgeorgiy.ja.asmirko.walk.hashsum.PJWHashSum;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class Walk {

    private final static String DEFAULT_ENCODING = "UTF-8";

    private static byte[] inByteBuffer = new byte[1024 * 2];
    private static HashSumAlgorithm hashSumAlgorithm = new PJWHashSum();

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Expected 2 input files");
                return;
            }

            Path inFile = Path.of(args[0]);
            Path outFile = Path.of(args[1]);

            try{
                BufferedWriter writer = Files.newBufferedWriter(outFile);
                writer.write("");
                writer.flush();
            } catch (IOException e){

            }

            try (var out = Files.newBufferedWriter(outFile, Charset.forName(DEFAULT_ENCODING), StandardOpenOption.WRITE)) {
                handleFiles(out, inFile);
            } catch (IOException e) {
                if (Files.notExists(outFile)) {
                    try {
                        Files.write(outFile,
                                new LinkedList<CharBuffer>(),
                                StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND);
                        var out = Files.newBufferedWriter(outFile, Charset.forName(DEFAULT_ENCODING), StandardOpenOption.WRITE);
                        handleFiles(out, inFile);
                    } catch (IOException ex) {
                        System.out.println("Unable to create new output file");
                        return;
                    }
                } else {
                    System.out.println("Some error occurred while attempt to crete output stream from second file");
                    return;
                }
            }
        } catch (SmoothExitException smoothExitException){

        }
    }

    private static Long hashOfFile(Path file) throws IOException {
        long hashSum = 0L;
        int charsRead;
        var in = Files.newInputStream(file);
        while ((charsRead = in.read(inByteBuffer)) != -1) {
            hashSum = hashSumAlgorithm.computeHashSum(hashSum, inByteBuffer, charsRead);
        }
        return hashSum;
    }

    private static void handleFiles(BufferedWriter out, Path inFile) throws IOException, SmoothExitException {
        if (Files.notExists(inFile)) {
            exitOnError(out);
        }
        var paths = Files.lines(inFile)
                .map(Path::of)
                .peek(Path::normalize).iterator();
        var sb = new StringBuilder();
        while (paths.hasNext()){
            var curPath = paths.next();
            Long hash = pathToHash(curPath, out);
            sb.append(String.format("%016x", hash))
                    .append(" ")
                    .append(curPath.toString());
            out.write(sb.toString());
            sb.delete(0, sb.length());
            out.newLine();
            out.flush();
        }
    }

    private static long pathToHash(Path p, BufferedWriter out) throws SmoothExitException{
        long hash = 0;
        try {
            hash = hashOfFile(p);
        } catch (IOException e) {
            e.printStackTrace();
            exitOnError(out);
        }
        return hash;
    }

    private static void exitOnError(BufferedWriter out) throws SmoothExitException {
        try {
            out.write("0000000000000000 ");
            out.flush();
        } catch (Exception e) {

        }
        throw new SmoothExitException();
    }
}