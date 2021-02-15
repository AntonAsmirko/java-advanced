package info.kgeorgiy.ja.asmirko.walk.hashsum;

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
}