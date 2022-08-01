import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class P2 {

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

    static HashMap<BigInteger, Integer>[] dp;
    static HashMap<HashMap<BigInteger, Integer>, Integer>[] dp2;
    static boolean worked = false;

    static class CPMath {

        static BigInteger multiply(BigInteger a, BigInteger b) {
            b = a.multiply(b);
            return b;
        }

        static BigInteger power(BigInteger a, int b) {
            BigInteger r = BigInteger.ONE;

            while (b > 0) {
                if (b % 2 == 1) {
                    r = multiply(r, a);
                }

                a = multiply(a, a);
                b /= 2;
            }

            return r;
        }
    }

    public static void solver(String s, BigInteger product) throws Exception {
        HashSet<BigInteger> factors = printDivisors(product);

        dp = new HashMap[s.length() + 1];

        for (int i = 0; i <= s.length(); i++) {
            dp[i] = new HashMap<>();
        }

        dp[0].put(BigInteger.valueOf(1), 0);

        for (int i = 0; i <= s.length(); i++) {
            for (int j = i - 1; j >= 0; j--) {
                String num = s.substring(j + 1 - 1, i);

                BigInteger val = new BigInteger(num);

                if (factors.contains(val)) {
                    for (Map.Entry<BigInteger, Integer> entry: dp[j].entrySet()) {

                        BigInteger k = entry.getKey();

                        BigInteger inter = val.multiply(k);
                        if (factors.contains(inter)) {
                            dp[i].put(inter, j);
                        }
                    }
                }
            }
        }
    }

    public static void solver2(String s, BigInteger product) throws Exception {
        PollardRho pollardRho = new PollardRho();
        HashMap<BigInteger, Integer> factors = pollardRho.prime_factorize(product);

        dp2 = new HashMap[s.length() + 1];

        for (int i = 0; i <= s.length(); i++) {
            dp2[i] = new HashMap<>();
        }

        dp2[0].put(new HashMap<>(), 0);

        for (int i = 0; i <= s.length(); i++) {
            for (int j = i - 1; j >= 0; j--) {
                String num = s.substring(j + 1 - 1, i);

                BigInteger val = new BigInteger(num);
                HashMap<BigInteger, Integer> val_factors = pollardRho.prime_factorize(val);

                if (isDivisor(val_factors, factors)) {
                    for (Map.Entry<HashMap<BigInteger, Integer>, Integer> entry: dp2[j].entrySet()) {
                        HashMap<BigInteger, Integer> k = entry.getKey();

                        HashMap<BigInteger, Integer> result = multiplyAndCheck(val_factors, k, factors);

                        if (result != null) {
                            dp2[i].put(result, j);
                            if (i == s.length()) {
                                // check that this equals the product
                                if (isDivisor(factors, result)) {
                                    worked = true;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static boolean isDivisor(HashMap<BigInteger, Integer> toTest, HashMap<BigInteger, Integer> product) {
        for (Map.Entry<BigInteger,Integer> entry : toTest.entrySet()) {
            BigInteger bigInteger = entry.getKey();
            int exponent_to_test = entry.getValue();
            if (product.containsKey(bigInteger)) {
                int exponent_product = product.get(bigInteger);
                if (exponent_to_test > exponent_product) return false;
            }

            else {
                return false;
            }
        }

        return true;
    }

    static HashMap<BigInteger, Integer> multiplyAndCheck(HashMap<BigInteger, Integer> hashMap1, HashMap<BigInteger, Integer> hashMap2, HashMap<BigInteger, Integer> product) {
        HashMap<BigInteger, Integer> result = new HashMap<>();

        HashSet<BigInteger> already_done = new HashSet<>();

        for (Map.Entry<BigInteger,Integer> entry : hashMap1.entrySet()) {
            BigInteger bigInteger = entry.getKey();
            int exponent_to_test = entry.getValue();

            int total_exponent_in_product = exponent_to_test;

            if (hashMap2.containsKey(bigInteger)) {
                int exponent_product = hashMap2.get(bigInteger);
                total_exponent_in_product+=exponent_product;
                already_done.add(bigInteger);
            }

            if (product.get(bigInteger) < total_exponent_in_product) return null;
            result.put(bigInteger, total_exponent_in_product);
        }

        for (Map.Entry<BigInteger,Integer> entry : hashMap2.entrySet()) {
            BigInteger bigInteger = entry.getKey();
            if (already_done.contains(bigInteger)) continue;
            int exponent_to_test = entry.getValue();

            int total_exponent_in_product = exponent_to_test;

            if (hashMap1.containsKey(bigInteger)) {
                int exponent_product = hashMap1.get(bigInteger);
                total_exponent_in_product+=exponent_product;
            }

            if (product.get(bigInteger) < total_exponent_in_product) return null;
            result.put(bigInteger, total_exponent_in_product);
        }

        return result;
    }

    static HashMap<BigInteger, Integer>[] evaluateAll(int upper_index) {
        HashMap<BigInteger, Integer>[] result = new HashMap[upper_index + 1];

        for (int i = 0; i <= upper_index; i++) {
            result[i] = new HashMap<>();
            for (Map.Entry<HashMap<BigInteger, Integer>, Integer> entry: dp2[i].entrySet()) {
                HashMap<BigInteger, Integer> factorization_of_number = entry.getKey();
                int key_to_back = entry.getValue();
                result[i].put(evaluate(factorization_of_number), key_to_back);
            }
        }

        return result;
    }

    static BigInteger evaluate(HashMap<BigInteger, Integer> number) {
        BigInteger result = BigInteger.ONE;

        for (Map.Entry<BigInteger, Integer> entry: number.entrySet()) {
            BigInteger prime = entry.getKey();
            int key_to_back = entry.getValue();
            result = result.multiply(CPMath.power(prime, key_to_back));
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        sc = new InputReader(System.in);
        pw = new PrintWriter(System.out);

        String s = sc.next();

        if (s.contains("0")) {
            throw new Exception("The program input should be a string of non-zero digits");
        }

        if (!P1.onlyDigits(s)) {
            throw new Exception("The program input should not contain any characters other than digits");
        }

        boolean verbose = true;

        BigInteger product = new BigInteger(sc.next());

        if (product.compareTo(BigInteger.ONE) < 0 || product.compareTo(new BigInteger(s)) > 0) {
            throw new Exception("Does not satisfy requirements of input");
        }

        solver2(s, product);

        if (worked) {
            if (verbose) System.out.println("A partition does work. Going to find it!");
            int index = s.length();

            HashMap<BigInteger, Integer>[] dp_2 = evaluateAll(index);

            BigInteger value = product;

            ArrayList<String> res = new ArrayList<>();

            while (index != 0) {
                String sub = s.substring(dp_2[index].get(value), index);
                res.add(sub);
                index = dp_2[index].get(value);

                BigInteger val = new BigInteger(sub);

                value = value.divide(val);
            }

            Collections.reverse(res);

            for (int i = 0; i < res.size(); i++) {
                pw.print(res.get(i));
                if (i != res.size() - 1) pw.print(", ");
            }
        }

        else {
            pw.println("No solution.");
        }

        /*solver(s, product);

        if ((!s.isEmpty() && dp[s.length()].containsKey(product)) || (s.isEmpty() && product.equals(BigInteger.ZERO))) {
            int index = s.length();
            BigInteger value = product;

            ArrayList<String> res = new ArrayList<>();

            while (index != 0) {
                String sub = s.substring(dp[index].get(value), index);
                res.add(sub);
                index = dp[index].get(value);

                BigInteger val = new BigInteger(sub);

                value = value.divide(val);
            }

            Collections.reverse(res);

            for (int i = 0; i < res.size(); i++) {
                pw.print(res.get(i));
                if (i != res.size() - 1) pw.print(", ");
            }
        }

        else {
            pw.println("No solution.");
        }*/

        pw.close();
    }

    static HashSet<BigInteger> printDivisors(BigInteger n) {
        HashSet<BigInteger> hashSet = new HashSet<>();
        for (BigInteger i=BigInteger.ONE;;) {
            if (n.mod(i).equals(BigInteger.ZERO)) {

                BigInteger div = n.divide(i);

                if (div.equals(i)) hashSet.add(i);

                else {
                    hashSet.add(i);
                    hashSet.add(div);
                }
            }
            i = i.add(BigInteger.ONE);
            if (i.equals(n.sqrt())) break;
        }

        return hashSet;
    }
}