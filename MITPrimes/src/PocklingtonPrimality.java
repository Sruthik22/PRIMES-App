import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class PocklingtonPrimality {

    static class CPMath {

        static BigInteger multiply(BigInteger a, BigInteger b) {
            b = a.multiply(b);
            return b.mod(mod);
        }

        static BigInteger power(BigInteger a, BigInteger b) {
            BigInteger r = BigInteger.ONE;

            while (b.compareTo(BigInteger.ZERO) > 0) {
                if (b.getLowestSetBit() == 0) {
                    r = multiply(r, a);
                }

                a = multiply(a, a);
                b = b.shiftRight(1);
            }

            return r;
        }
    }

    static BigInteger mod;

    public static boolean test(BigInteger N) {
        PollardRho pollardRho = new PollardRho();
        HashMap<BigInteger, Integer> factorization = pollardRho.prime_factorize(N.subtract(BigInteger.ONE));
        BigInteger checked = BigInteger.ONE;

        for (BigInteger prime_factor: factorization.keySet()) {
            boolean result = checkFactor(prime_factor, N);
            if (!result) return false;
            checked = checked.multiply(prime_factor);
            if (checked.multiply(checked).compareTo(N) > 0) return true;
        }

        return true;
    }

    private static boolean checkFactor(BigInteger p, BigInteger N) {
        int a = candidateTo(N);
        return CPMath.power(BigInteger.valueOf(a), N.subtract(BigInteger.ONE).divide(p)).gcd(N).equals(BigInteger.ONE);
    }
    private static int candidateTo(BigInteger N) {
        int a = 2;
        mod = N;
        while (!CPMath.power(BigInteger.valueOf(a), N.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
            a++;
        }
        return a;
    }
}