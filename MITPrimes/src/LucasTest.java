import java.math.BigInteger;

public class LucasTest {

    public static final BigInteger ZERO = BigInteger.ZERO;
    public static final BigInteger I_1 = BigInteger.ONE;

    private final JacobiSymbol jacobi = new JacobiSymbol();

    /**
     * Strong Lucas probable prime test with parameters P=1,
     * D some value in 5, -7, 9, -11, 13, -15, ...
     * and Q=(1-D)/4
     *
     * @param N input that we are testing prime or not
     * @return true if N is a strong Lucas probable prime, false if N is composite
     */
    public boolean probablePrimeTest(BigInteger N) {
        // As stated on Wikipedia, "one usually verifies that n ... is not a perfect square"
        if (SqrtExact.exactSqrt(N) != null) return false;

        // "We choose a Lucas sequence where the Jacobi Symbol == -1" (Wikipedia)
        BigInteger D = findDInJacobiSymbol(N);

        // δ(N) = delta(N) = N+1
        BigInteger delta = N.add(I_1);

        // Now, factor δ(n)=n−(D/n) into the form d * 2 ^ s where d is odd
        int s = delta.getLowestSetBit();
        BigInteger d = delta.shiftRight(s);

        // compute the Lucas Sequence
        BigInteger U = I_1;
        BigInteger V = I_1;
        for (int i = d.bitLength()-2; i >= 0; i--) {
            // Doubling the subscript from k to 2k directly from Wikipedia
            BigInteger U2 = U.multiply(V).mod(N);
            BigInteger V2 = V.multiply(V).add(D.multiply(U.multiply(U))).mod(N);
            if (V2.testBit(0)) {
                // if the number is odd, then can't divide by 2 so subtract N
                V2 = V2.subtract(N);
            }
            V2 = V2.shiftRight(1);

            if (d.testBit(i)) {
                // "Next, we can increase the subscript by 1 using the recurrences" Wikipedia
                U = U2.add(V2).mod(N);
                if (U.testBit(0)) U = U.subtract(N);
                U = U.shiftRight(1);
                V = V2.add(D.multiply(U2)).mod(N);
                if (V.testBit(0)) V = V.subtract(N);
                V = V.shiftRight(1);
            } else {
                // bit i of delta is 0, no need to increment indices
                U = U2;
                V = V2;
            }
        }
        // A strong Lucas pseudoprime for a given (P, Q) pair is an odd composite number n
        // with GCD(n, D) = 1, satisfying one of the conditions U_d === 0 (mod n)
        // or V_(d*2^r) === 0 (mod n)
        if (U.equals(ZERO) || V.equals(ZERO)) return true;

        // test V_(d*2^r) == 0 (mod N) for 1<=r<s because we already tested 1 above
        for (int r=1; r<s; r++) {
            // Doubling the subscript from k to 2k directly from Wikipedia
            BigInteger U2 = U.multiply(V).mod(N);
            BigInteger V2 = V.multiply(V).add(D.multiply(U.multiply(U))).mod(N);
            if (V2.testBit(0)) {
                V2 = V2.subtract(N);
                V2 = V2.shiftRight(1);
                U = U2;
                V = V2;
            } else {
                V2 = V2.shiftRight(1);
                U = U2;
                V = V2;
                // Test if V_(d*2^r) == 0 (mod N); then N is a Lucas probable prime
                if (V.equals(ZERO)) return true;
            }
        }
        return false;
    }

    /**
     * Find the first D in the sequence alternating sequence of positive and negative odd numbers
     * 5, -7, 9, -11, 13, -15, ...
     * such that Jacobi(D|N) is -1.
     * @param N input
     * @return D
     */
    private BigInteger findDInJacobiSymbol(BigInteger N) {
        int D = 5;
        while (jacobi.jacobiSymbol(BigInteger.valueOf(D), N) != -1) {
            if (D > 0) {
                D = -D - 2;
            }

            else {
                D = -D + 2;
            }
        }
        return BigInteger.valueOf(D);
    }
}
