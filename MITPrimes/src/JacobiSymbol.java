import java.math.BigInteger;

public class JacobiSymbol {

    public static final BigInteger I_0 = BigInteger.ZERO;
    public static final BigInteger I_1 = BigInteger.ONE;

    /**
     * Jacobi symbol J(n|k), with k an odd, positive integer.
     *
     * @param n input
     * @param k modulus, an odd integer positive integer
     * @return J(n|k)
     */
    public int jacobiSymbol(BigInteger n, BigInteger k) {
        if (n.equals(I_0)) return 0;
        n = n.mod(k);

        int t = 1;

        while (!n.equals(I_0)) {

            int lsb = n.getLowestSetBit();

            int divide_4 = lsb / 2;

            n = n.shiftRight(2 * divide_4);

            if ((lsb&1)==1) {
                n = n.shiftRight(1);
                int r = k.intValue() & 7;
                if (r == 3 || r == 5) {
                    t = -t;
                }
            }

            BigInteger placeHolder = n;

            n = k;
            k = placeHolder;

            if ((n.intValue() & 3) == 3 && (k.intValue() & 3) == 3) t = -t;

            n = n.mod(k);
        }

        if (k.equals(I_1)) return t;
        else return 0;
    }
}