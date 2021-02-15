package info.kgeorgiy.ja.asmirko.walk.hashsum;

public interface HashSumAlgorithm {
    public long computeHashSum(long prevHash, byte[] str, int buffSize);
}
