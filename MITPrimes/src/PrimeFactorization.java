import java.math.BigInteger;
import java.util.*;

abstract public class PrimeFactorization {

    public static final BigInteger I_1 = BigInteger.ONE;
    public static final BigInteger I_2 = BigInteger.valueOf(2);

    private BPSWPrimalityTest bpsw = new BPSWPrimalityTest();
    private TrialDiv tdiv = new TrialDiv();

    /**
     * @param N Number to factor.
     * @return The prime factorization of N
     */
    public HashMap<BigInteger, Integer> prime_factorize(BigInteger N) {
        HashMap<BigInteger, Integer> primeFactors = new HashMap<>();
        // Remove all multiples of 2 so that later algorithms only need to find odd factors
        int lowestSetBit = N.getLowestSetBit();
        if (lowestSetBit > 0) {
            primeFactors.put(I_2, lowestSetBit);
            N = N.shiftRight(lowestSetBit);
        }
        if (N.equals(I_1)) {
            // PRIMES document states that "1 is not a prime number."
            return primeFactors;
        }

        int Nbits = N.bitLength();
        if (Nbits > 62) {
            // these experimental results were taken from Java Number Theory Library
            final double e = 10 + (Nbits-45)*0.07407407407;
            int actualTdivLimit = (int) Math.min(1<<20, Math.pow(2, e));

            // find small factors with trial division -- only odd because even factors were taken out above
            N = tdiv.primeDivisorsUnderLimit(N, actualTdivLimit, primeFactors);

            if (N.equals(I_1)) {
                return primeFactors;
            }
        }

        ArrayList<BigInteger> remaining_n = new ArrayList<BigInteger>();
        remaining_n.add(N);
        while (!remaining_n.isEmpty()) {
            N = remaining_n.remove(remaining_n.size()-1);

            if (isPrime(false, N)) {
                primeFactors.putIfAbsent(N, 0);
                primeFactors.put(N, primeFactors.get(N) + 1);
                continue;
            }
            BigInteger singleFactor = findADivisor(N);
            if (singleFactor.compareTo(I_1) > 0 && singleFactor.compareTo(N) < 0) {
                remaining_n.add(singleFactor);
                remaining_n.add(N.divide(singleFactor));
            } else {
                System.out.println("FAILED TO FIND A NONTRIVIAL FACTOR " + N);
                primeFactors.putIfAbsent(N, 0);
                primeFactors.put(N, primeFactors.get(N) + 1);
            }

        }
        return primeFactors;
    }

    public boolean isPrime(boolean flag, BigInteger N) {
        if (flag) {
            return bpsw.isProbablePrime(N);
        }

        else {
            BigInteger bound = new BigInteger("18446744073709551616");
            return bpsw.isProbablePrime(N) && (N.compareTo(bound) < 0 || PocklingtonPrimality.test(N));
        }
    }

    /**
     * Find a factor of N
     * @param N
     * @return a single factor
     */
    abstract public BigInteger findADivisor(BigInteger N);
}