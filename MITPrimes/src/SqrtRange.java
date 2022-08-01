import java.math.BigInteger;
public class SqrtRange {

    public static final BigInteger I_0 = BigInteger.ZERO;
    public static final BigInteger ONE = BigInteger.ONE;

    /**
     * Find range of integer solutions for sqrt() using Herons (or "Babylonian") method
     * Finding an initial guess for square root and calling the below method
     *
     * @param N the number to find a square root
     * @return [lower, upper] int values of sqrt(N)
     */
    public static BigInteger[] findSqrtForAnySizeN(BigInteger N) {
        // check if N is positive
        int sign = N.signum();
        if (sign>0) {
            int bits = N.bitLength();
            if (bits >= 1024) {
                // how many shifts currently -- we can deal with the shifts by dividing
                // the exponent by 2
                int shiftsRight = bits - 1023;
                // if the shifts is odd, we have to make it even so we can easily find the sqrt
                if ((shiftsRight & 1) == 1) shiftsRight++;
                double sqrt = Math.sqrt(N.shiftRight(shiftsRight).doubleValue());
                BigInteger initialGuess = doubleToBigInteger(sqrt, shiftsRight>>1);

                return findSqrtForAnySizeN(N, initialGuess);
            } else if (bits >= 127) {
                BigInteger initialGuess = doubleToBigInteger(Math.sqrt(N.doubleValue()), 0);
                return findSqrtForAnySizeN(N, initialGuess);
            } else if (bits >= 107) {
                BigInteger initialGuess = BigInteger.valueOf((long) Math.sqrt(N.doubleValue()));
                return findSqrtForAnySizeN(N, initialGuess);
            } else if (bits >= 64) {
                long sqrt = (long) Math.sqrt(N.doubleValue());
                BigInteger sqrt_big = BigInteger.valueOf(sqrt);
                int cmp = sqrt_big.multiply(sqrt_big).compareTo(N);
                return createOutput(cmp, sqrt_big);
            } else {
                long N_long = N.longValue();
                long sqrt = (long) Math.sqrt(N_long);
                long inline_cmp = sqrt*sqrt - N_long;
                BigInteger sqrt_big = BigInteger.valueOf(sqrt);
                return createOutput(inline_cmp, sqrt_big);
            }
        }
        // if the value is 0, then sqrt(0) = 0
        if (sign==0) return new BigInteger[] {I_0, I_0};

        // if the value is negative no sqrt
        throw new IllegalArgumentException("No square roots for negative number over the real domain");
    }

    /**
     * Creates the range output of sqrt(n) based on the given sqrt guess
     *
     * @param cmp how the value compared to the real square root
     * @param sqrt the guess of the true sqrt
     * @return [lower, upper] int values of sqrt(n)
     */
    public static BigInteger[] createOutput(long cmp, BigInteger sqrt) {
        if (cmp < 0) return new BigInteger[] {sqrt, sqrt.add(ONE)};
        if (cmp > 0) return new BigInteger[] {sqrt.subtract(ONE), sqrt};
        return new BigInteger[] {sqrt, sqrt};
    }

    /**
     * Simple Babylonian Method to find Square Roots
     * Threshold of sqrt difference among guesses is 1
     *
     * @param n the number to find the approx sqrt of
     * @param guess the initial guess of the sqrt of n
     * @return [lower, upper] int values of sqrt(n)
     */
    public static BigInteger[] findSqrtForAnySizeN(BigInteger n, BigInteger guess) {
        // directly from Wikipedia
        guess = n.divide(guess).add(guess).shiftRight(1);

        BigInteger previous_guess;
        do {
            previous_guess = guess;
            guess = n.divide(guess).add(guess).shiftRight(1);
        } while (guess.subtract(previous_guess).abs().bitLength()>1);

        // compare guess^2 to n to see how it compares to the actual sqrt of n
        int cmp = guess.multiply(guess).compareTo(n);

        return createOutput(cmp, guess);
    }

    /**
     * Convert a double to a BigInteger from double, with minimal precision loss
     * @param d the input double to convert
     * @param additional_exp an additional power of 2 to multiply the BigInteger by
     * @return BigInteger
     */
    public static BigInteger doubleToBigInteger(double d, int additional_exp) {
        long d_bits = Double.doubleToRawLongBits(d);
        // x = s*m*2^e (with base b=2 in IEEE 754) from Wikipedia
        // bit 63 represents the sign
        boolean isNegative = (d_bits & 0x8000000000000000L) != 0;
        // Bits 62-52 represent the exponent
        // As stated in Wikipedia The exponent is fixed by subtracting the fixed bias value
        // this bias value for doubles is 1023
        int bias = 1023;
        // we used the unsigned bit operation as we want only the exponent value
        int p = 52;
        int e = (int) ((d_bits & 0x7ff0000000000000L) >>> p) - bias;
        // Bits 51-0 represent the significand
        long mantissa = d_bits & 0x000fffffffffffffL;
        // m = 1 + (mantissa/2^p) with p=52 for doubles
        // => x = s*(1+(mantissa/2^p))*2^e = s * [2^e + mantissa * 2^(e-p)]
        BigInteger result = ONE.shiftLeft(e + additional_exp).add(BigInteger.valueOf(mantissa).shiftLeft(e + additional_exp -p));
        return (isNegative) ? result.negate() : result;
    }
}
