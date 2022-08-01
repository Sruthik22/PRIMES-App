import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class P1_test {

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public float nextFloat() {
            return Float.parseFloat(next());
        }

        public double nextDouble() {
            return Float.parseFloat(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static InputReader sc;
    static PrintWriter pw;

    static HashMap<BigInteger, Integer> primeFactor;

    public static void main(String[] args) {
        sc = new InputReader(System.in);
        pw = new PrintWriter(System.out);

        BigInteger val = new BigInteger("192062179178278031562375733145642031");

        System.out.println(val);

        PollardRho factorAlgorithm = new PollardRho();

        primeFactor = factorAlgorithm.prime_factorize(val);

        System.out.println("N = " + val + " = " + primeFactor);

        pw.close();
    }
}