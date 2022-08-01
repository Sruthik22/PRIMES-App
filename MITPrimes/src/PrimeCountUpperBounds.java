/**
 * Bounds for the prime counting function pi(x) = number of primes in (0, x].
 * @author Tilman Neumann
 */
public class PrimeCountUpperBounds {

    private PrimeCountUpperBounds() {
        // static class
    }

    /**
     * Computes an upper bound for the prime counting function pi(x) := number of primes in (0, x]
     * @param x
     * @return
     */
    public static long combinedUpperBound(long x) {
        if (x<2) return 0;
        if (x<200) return Rosser_Schoenfeld(x); // works for x > 1
        if (x<24103) return Dusart2010_eq6_6(x); // works for x <= 24103, x >= 60184
        if (x<60184) return Axler_3_5d(x); // works for x = 5.43 ... 230387 and x > 2634562561
        if (x<120000) return Dusart2010_eq6_6(x); // works for x <= 24103, x >= 60184
        if (x<230387) return Axler_3_5d(x); // works for x = 5.43 ... 230387 and x > 2634562561
        if (x<420000) return Dusart2010_eq6_6(x); // works for x <= 24103, x >= 60184

        // Clearly best for 420000 < x <= all x tested so far (x <= 411200000000).
        // From that data I estimated that Theorem 1.3 would be better for approximately x > 2470000000000.
        if (x<2470000000000L) return Axler_3_5c(x); // works for x >= 9.25

        // Axler Theorem 1.3 is the best in the long run
        return Axler_1_3(x);
    }

    /**
     * Axler, https://arxiv.org/pdf/1409.1780.pdf, Theorem 1.3:
     * pi(x) < x / [ln(x) - 1 - 1/ln(x) - 3.35/ln^2(x) - 12.65/ln^3(x) - 71.7/ln^4(x) - 466.1275/ln^5(x) - 3489.8225/ln^6(x)], for x > e^3.804.
     *
     * Axler comments: "improvement of Theorem 1.1 for every sufficiently large x". This is true, but the improvement is quite small.
     *
     * From data up to x ~ 4.112 * 10^11 I estimated that Theorem 1.3 is better than Corollar 3.5c for approximately x > 2.470.000.000.000.
     */
    public static long Axler_1_3(long x) {
        double lnx = Math.log(x);
        double lnxPow2 = lnx * lnx;
        double lnxPow3 = lnxPow2 * lnx;
        double lnxPow4 = lnxPow3 * lnx;
        double lnxPow5 = lnxPow4 * lnx;
        double lnxPow6 = lnxPow5 * lnx;
        double den = lnx - 1 - 1/lnx - 3.35/lnxPow2 - 12.65/lnxPow3 - 71.7/lnxPow4 - 466.1275/lnxPow5 - 3489.8225/lnxPow6;
        return (long) Math.ceil(x/den);
    }

    /**
     * Axler, https://arxiv.org/pdf/1409.1780.pdf, Corollary 3.5c:
     * pi(x) < x / [ln(x) - 1 - 1/ln(x) - 3.83/ln^2(x)] for x >= 9.25
     *
     * Best stable algorithm for all x tested so far! (x <= 50673847669)
     */
    public static long Axler_3_5c(long x) {
        double lnx = Math.log(x);
        double lnxPow2 = lnx * lnx;
        double den = lnx - 1 - 1/lnx - 3.83/lnxPow2;
        return (long) Math.ceil(x/den);
    }

    /**
     * Axler, https://arxiv.org/pdf/1409.1780.pdf, Corollary 3.5d:
     * pi(x) < x / [ln(x) - 1 - 1.17/ln(x)]<br/>
     *
     * Works for x >= 2.634.800.823 and then it is the best bound for x < 6.200.000.000 approximately.
     */
    public static long Axler_3_5d(long x) {
        double lnx = Math.log(x);
        double den = lnx - 1 - 1.17/lnx;
        return (long) Math.ceil(x/den);
    }

    /**
     * Dusart 2010 theorem 6.9, eq. (6.6), holds for any x >= 60184.
     * @see https://arxiv.org/PS_cache/arxiv/pdf/1002/1002.0442v1.pdf
     */
    public static long Dusart2010_eq6_6(long x) {
        return (long) Math.ceil(x / (Math.log(x) - 1.1));
    }

    /**
     * Rosser, Schoenfeld: pi(x) < 1.25506*x / ln(x) for x > 1.
     */
    public static long Rosser_Schoenfeld(long x) {
        return (long) Math.ceil(1.25506*x / Math.log(x));
    }
}
