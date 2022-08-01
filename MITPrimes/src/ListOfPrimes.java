public class ListOfPrimes implements SieveCallback {

    private int[] array_primes = new int[] {2};
    private int number_of_elements_stored = 1;
    private int array_capacity = 1;

    private static final ListOfPrimes THE_PRIMES_ARRAY = new ListOfPrimes();

    public static ListOfPrimes get() {
        return THE_PRIMES_ARRAY;
    }

    /**
     * Ensures that the array contains all primes <= primeLimit.
     * @param primeLimit the number where all primes less than or equal should be added to array_primes
     */
    public void primeTopLimit(int primeLimit) {
        // we need to check if the largest element is less than primeLimit, and if so make sure to add primes up to that point
        if (array_primes[number_of_elements_stored - 1] < primeLimit) {
            int countUpperBound = (int) PrimeCountUpperBounds.combinedUpperBound(primeLimit);
            getPrimesUpToLimit(countUpperBound, primeLimit);
        }
    }

    /**
     * @param n the index of the prime to return
     * @return nth prime, zero index
     */
    public int getNthPrime(int n) {
        if (number_of_elements_stored <= n) {
            int nextCount = 3 * number_of_elements_stored; // amortized value
            long nthPrimeUpperBound = NthPrimeUpperBounds.combinedUpperBound(nextCount);
            getPrimesUpToLimit(nextCount, nthPrimeUpperBound);
        }
        return array_primes[n];
    }

    /**
     * @param desiredCount wanted number of primes
     * @param limit max prime number in array
     */
    private synchronized void getPrimesUpToLimit(int desiredCount, long limit) {
        if (desiredCount > number_of_elements_stored) {
            // reset values to the new sizes
            reset(desiredCount);
            RangedSieveofEratosthenes segmentedSieve = new RangedSieveofEratosthenes(this);
            segmentedSieve.sieve(limit);
        }
    }

    /**
     * Resets the array with the current parameters
     * @param desiredCount wanted number of primes
     */

    private void reset(int desiredCount) {
        array_primes = new int[desiredCount];
        array_capacity = desiredCount;
        number_of_elements_stored = 0;
    }

    @Override
    public void add(long prime) {
        if (number_of_elements_stored == array_capacity) return;
        array_primes[number_of_elements_stored++] = (int) prime;
    }
}
