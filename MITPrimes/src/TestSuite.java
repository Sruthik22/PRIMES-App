import java.math.BigInteger;
import java.util.*;

public class TestSuite {
    static int MAX = 0;
    static ArrayList<String> arrayList;

    static void findCombinations(String str, int index, String out) {
        if (index == str.length())
            arrayList.add(out);

        for (int i = index; i < str.length(); i++)
            findCombinations(str, i + 1, out +
                    "[" + str.substring(index, i+1) + "]" );
    }

    public static int brute_force_prime_fac(long n) {
        int count = 0;
        while (n%2==0) {
            count++;
            n /= 2;
        }
        for (int i = 3; i <= Math.sqrt(n); i+= 2) {
            while (n%i == 0) {
                count++;
                n /= i;
            }
        }

        if (n > 2) count++;

        return count;
    }

    public static void test_1() throws Exception {
        int trials = 1000;
        for (int i = 0; i < trials; i++) {
            int number = (int) (Math.random() * 17 + 1);
            String str = generateRandomDigits(number);
            System.out.println(str);
            arrayList = new ArrayList<>();
            findCombinations(str, 0, "");
            for (String partitions : arrayList) {
                StringBuilder sb = new StringBuilder();
                int cur = 0;
                for (char c: partitions.toCharArray()) {
                    if (c == '[') continue;
                    else if (c == ']' && !sb.toString().equals("")) {
                        long l = Long.parseLong(sb.toString());
                        int factors = brute_force_prime_fac(l);
                        cur += factors;
                        sb = new StringBuilder();
                    }
                    else {
                        sb.append(c);
                    }
                }

                MAX = Math.max(MAX, cur);
            }

            P1.solver(str, false);

            if (MAX != P1.dp[str.length()]) {
                System.out.println("ERROR: Values do not match for input: " + str);
                return;
            }

            MAX = 0;
        }
    }

    public static void test_2() throws Exception {
        int trials = 1;
        for (int i = 0; i < trials; i++) {
            String str = "123489721561234";
            System.out.println(str);
            BigInteger product = BigInteger.valueOf(676257548736L);
            arrayList = new ArrayList<>();
            findCombinations(str, 0, "");
            HashSet<BigInteger> all_combos = new HashSet<>();
            for (String partitions : arrayList) {
                StringBuilder sb = new StringBuilder();
                long cur = 1;
                for (char c: partitions.toCharArray()) {
                    if (c == '[') continue;
                    else if (c == ']' && !sb.toString().equals("")) {
                        long l = Long.parseLong(sb.toString());
                        cur *= l;
                        sb = new StringBuilder();
                    }
                    else {
                        sb.append(c);
                    }
                }

                all_combos.add(BigInteger.valueOf(cur));
            }

            P2.solver(str, product);

            if (all_combos.contains(product) && P2.dp[str.length()].containsKey(product)) {

            }

            else if (!all_combos.contains(product) && !P2.dp[str.length()].containsKey(product)) {

            }

            else {
                System.out.println("ERROR: Values do not match for input: " + str + " " + product);
                return;
            }
        }
    }

    static void test_evaluate() {
        PollardRho pollardRho = new PollardRho();
        int trials = 1000;
        for (int i = 0; i < trials; i++) {
            int number = (int) (Math.random() * 17 + 1);
            String str = generateRandomDigits(number);
            BigInteger str_big = new BigInteger(str);

            HashMap<BigInteger, Integer> factors = pollardRho.prime_factorize(str_big);
            if (!P2.evaluate(factors).equals(str_big)) {
                System.out.println("ERROR: Values do not match for input: " + str);
                return;
            }
        }
    }

    public static void main (String[] args) throws Exception {
        //test_1();
        //test_2();
        //test_evaluate();
    }

    public static String generateRandomDigits(int n) {
        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < n; i++) {
            sb.append(random.nextInt(9) + 1);
        }

        return sb.toString();
    }
}