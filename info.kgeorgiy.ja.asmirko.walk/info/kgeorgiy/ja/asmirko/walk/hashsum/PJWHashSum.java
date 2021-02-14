package hashsum;

public class PJWHashSum implements HashSumAlgorithm{
    @Override
    public long computeHashSum(long prevHash, CharSequence str, int buffSize){
        long BitsInLong        = (long)(8 * 8);
        long ThreeQuarters     = (long)((BitsInLong  * 3) / 4);
        long OneEighth         = (long)(BitsInLong / 8);
        long HighBits          = ~((~0L) >> OneEighth);
        long test              = 0;

        for(int i = 0; i < buffSize; i++){
            prevHash = (prevHash << OneEighth) + str.charAt(i);

            if((test = prevHash & HighBits)  != 0){
                    prevHash = (( prevHash ^ (test >> ThreeQuarters)) & (~HighBits));
                }
        }
        return prevHash;
    }

    // @Override
    // public long computeHashSum(long prevHash, CharSequence str, int buffSize){
    //     long hash = prevHash;
    //     int bits = 8 * 8;
    //     long high = 0;
    //     str.chars()
    //         .forEach(ch -> {
    //             hash = hash << bits / 8 + ch;

    //         });
    //     return 0L;
    // }
}