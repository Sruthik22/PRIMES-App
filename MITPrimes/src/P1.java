import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class P1 {

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

    static int[] dp;
    static int[] parent;

    public static void solver(String s, boolean verbose) throws Exception {
        if (s.contains("0")) {
            throw new Exception("The program input should be a string of non-zero digits");
        }

        if (!onlyDigits(s)) {
            throw new Exception("The program input should not contain any characters other than digits");
        }

        dp = new int[s.length() + 1];
        parent = new int[s.length() + 1];

        for (int i = 1; i <= s.length(); i++) {
            for (int j = i - 1; j >= 0; j--) {
                String num = s.substring(j, i);
                BigInteger val = new BigInteger(num);
                PollardRho factorAlgorithm = new PollardRho();

                primeFactor = factorAlgorithm.prime_factorize(val);

                if (verbose) System.out.println("N = " + val + " = " + primeFactor);

                int num_primes = getNumberOfPrimes();

                if (verbose) System.out.println("Number of Primes is " + num_primes);

                if (dp[i] < num_primes + dp[j]) {
                    dp[i] = num_primes + dp[j];
                    parent[i] = j;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        sc = new InputReader(System.in);
        pw = new PrintWriter(System.out);

        boolean verbose = false;

        String s = sc.next();

        solver(s, verbose);

        int result = dp[s.length()];

        int index = s.length();

        ArrayList<String> res = new ArrayList<>();

        while (index != 0) {
            String sub = s.substring(parent[index], index);
            res.add(sub);
            index = parent[index];
        }

        Collections.reverse(res);

        for (int i = 0; i < res.size(); i++) {
            pw.print(res.get(i));
            if (i != res.size() - 1) pw.print(", ");
        }

        pw.println(" " + result);
        pw.close();
    }

    public static int getNumberOfPrimes() {
        int sum = 0;
        for (int i : primeFactor.values()) {
            sum += i;
        }

        return sum;
    }

    static boolean onlyDigits(String str)
    {
        for (char c: str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}