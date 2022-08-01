import java.math.BigInteger;
import java.util.*;

public class TrialDiv {

    public static final BigInteger ZERO = BigInteger.ZERO;
    public static final BigInteger I_1 = BigInteger.ONE;

    private static ListOfPrimes PRIMES = ListOfPrimes.get();

    /**
     * Uses trial division to find odd prime divisors of N up to a specified limit
     *
     * @param N
     * @param limit
     * @param primeFactors the prime factors that were found
     * @return N / product of all prime divisors under the limit
     */
    public BigInteger primeDivisorsUnderLimit(BigInteger N, int limit, HashMap<BigInteger, Integer> primeFactors) {
        PRIMES.primeTopLimit(limit);
        int i = 1;
        int prime = PRIMES.getNthPrime(i);
        while (prime <= limit) {

            BigInteger prime_BigInteger = BigInteger.valueOf(prime);
            BigInteger[] div = N.divideAndRemainder(prime_BigInteger);
            if (div[1].equals(ZERO)) {
                do {
                    primeFactors.putIfAbsent(prime_BigInteger, 0);
                    primeFactors.put(prime_BigInteger, primeFactors.get(prime_BigInteger) + 1);
                    N = div[0];
                    div = N.divideAndRemainder(prime_BigInteger);
                } while (div[1].equals(ZERO));

                if (N.bitLength() < 63) {
                    long p_squared = prime *(long)prime;
                    if (p_squared > N.longValue()) {

                        // we already did more than half factorization -- that means N is either a prime number or 1

                        if (N.compareTo(I_1)>0) {
                            primeFactors.putIfAbsent(N, 0);
                            primeFactors.put(N, primeFactors.get(N) + 1);
                        }
                        return I_1;
                    }
                }
            }

            prime = PRIMES.getNthPrime(++i);
        }

        return N;
    }
}
