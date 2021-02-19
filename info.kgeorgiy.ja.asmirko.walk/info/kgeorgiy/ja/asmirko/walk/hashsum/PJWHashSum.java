package info.kgeorgiy.ja.asmirko.walk.hashsum;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class PJWHashSum implements HashSumAlgorithm {
    @Override
    public long computeHashSum(long start, final byte[] bytes, final int size) {
        for (int i = 0; i < size; i++) {
            start = (start << 8) + (bytes[i] & 0xff);
            final long high = start & 0xff00_0000_0000_0000L;
            if (high != 0) {
                start ^= high >> 48;
                start &= ~high;
            }
        }
        return start;
    }

    @Override
    public long hashOfFile(Path file) throws IOException {
        byte[] inByteBuffer = new byte[1024 * 2];
        long hashSum = 0L;
        int charsRead;
        var in = Files.newInputStream(file);
        while ((charsRead = in.read(inByteBuffer)) != -1) {
            hashSum = computeHashSum(hashSum, inByteBuffer, charsRead);
        }
        return hashSum;
    }
}