package hashsum;

public interface HashSumAlgorithm {
    public long computeHashSum(long prevHash, CharSequence str, int buffSize);
}
