import java.math.BigInteger;

public class MillerRabinTest {

    public static final BigInteger ONE = BigInteger.ONE;
    private BigInteger N, NMinusOne, d;
    private int s;

    /**
     * Set correct class parameters and then run a Miller-Rabin test of N with base a.
     * @param N argument for the Miller-Rabin Base Test
     * @param a the base
     * @return true if N is probable prime, false if N is composite
     */
    public boolean basePrimeTest(BigInteger N, BigInteger a) {
        this.N = N;
        NMinusOne = N.subtract(ONE);
        // As stated on Wikipedia: N-1 = 2^s * D where D odd
        s = NMinusOne.getLowestSetBit();
        d = NMinusOne.shiftRight(s); // shifts right s bits to divide by 2 ^ s

        return basePrimeTest(a);
    }

    /**
     * Miller-Rabin test of N with base a.
     * @param a the base
     * @return true if N is either strong pseudoprime or real prime, false if N is composite
     */
    private boolean basePrimeTest(BigInteger a) {
        BigInteger test = a.modPow(d, N);
        if (test.equals(ONE) || test.equals(NMinusOne)) return true;

        // finished case r = 0, start with 1
        for (int r = 1; r < s; r++) {
            test = test.multiply(test).mod(N);
            if (test.equals(NMinusOne)) return true;
            if (test.equals(ONE)) return false;
        }

        return false;
    }
}