public class RangedSieveofEratosthenes {

    private SieveCallback clientCallback;

    public RangedSieveofEratosthenes(SieveCallback clientCallback) {
        this.clientCallback = clientCallback;
    }

    /**
     * Generate primes under a specified limit.
     * @param limit the upper bound of testing whether prime or not
     */
    public void sieve(long limit) {
        clientCallback.add(2);

        int sqrt = (int) Math.sqrt(limit);
        int segmentSize = (int) Math.min(limit, 132768);

        // classic sieve of Eratosthenes
        boolean[] isComposite = new boolean[sqrt+1];
        for (int i=2; i*i <= sqrt; i++) {
            if (!isComposite[i]) {
                for (int j = i*i; j <= sqrt; j+=i) {
                    isComposite[j] = true;
                }
            }
        }

        // faster than array list
        int arraySize = (int) PrimeCountUpperBounds.combinedUpperBound(sqrt);
        // 2 * small primes used for sieving
        long[] primes_times_2 = new long[arraySize];
        // composites that are multiples to remove
        long[] composites_to_remove = new long[arraySize];
        int primesCount = 0;

        // loop through each range
        long s = 3;
        long n = 3;
        for (long low=0; low <= limit; low += segmentSize) {
            boolean[] sieve_composite = new boolean[segmentSize];
            // [low, high]
            long high = Math.min(low + segmentSize - 1, limit);

            // new primes to sieve in this range
            for (; s*s <= high; s+=2) {
                if (!isComposite[(int) s]) {
                    primes_times_2[primesCount] = s<<1;
                    composites_to_remove[primesCount] = s*s - low;
                    primesCount++;
                }
            }

            // sieve out the range
            for (int i=0; i<primesCount; i++) {
                long j = composites_to_remove[i];
                long k = primes_times_2[i];
                for (; j<segmentSize; j+=k) {
                    sieve_composite[(int)j] = true;
                }
                composites_to_remove[i] = j-segmentSize;
            }

            // add primes to array
            for (; n<=high; n+=2) {
                if (!sieve_composite[(int) (n - low)]) {
                    clientCallback.add(n);
                }
            }
        }
    }
}
