package info.kgeorgiy.ja.asmirko.walk.hashsum;

import java.io.IOException;
import java.nio.file.Path;

public interface HashSumAlgorithm {
    long computeHashSum(long prevHash, byte[] str, int buffSize);

    public long hashOfFile(Path file) throws IOException;
}
