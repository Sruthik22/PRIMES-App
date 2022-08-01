/**
 * Bounds for the n.th prime p(n).
 * @author Tilman Neumann
 */
public class NthPrimeUpperBounds {

    private static final int[] SMALL_BOUNDS = new int[] {0, 2, 3, 5, 7, 11};

    private NthPrimeUpperBounds() {
        // static class
    }

    /**
     * Computes an upper bound for p(n) given n.
     * @param n
     * @return
     */
    // Correctness confirmed for all n <= 3.67 * 10^9.
    public static long combinedUpperBound(long n) {
        if (n < 6) return SMALL_BOUNDS[(int)n];
        if (n < 8601) return RobinJacobsen(n);
        if (n < 34420) return Robin1983(n); // works for n >= 8601, not n >= 7022 as commented; best then for n < 34420
        if (n < 39017) return RobinJacobsen(n);
        if (n < 178974) return Dusart1999(n);
        if (n < 688383) return Dusart2010p7(n);
        if (n < 8009824) return Dusart2010p8(n);
        return Axler2013(n);
    }

    /**
     * Rosser/Schoenfeld 1961 for n >= 6
     */
    public static long RosserSchoenfeld01(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + lnlnN));
    }

    /**
     * Rosser/Schoenfeld 1961 for n >= 20
     */
    public static long RosserSchoenfeld02(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + lnlnN - 0.5));
    }

    /**
     * Robin 1983 modified by D.Jacobsen for n = 6...39016 _only_
     */
    public static long RobinJacobsen(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + 0.6000 * lnlnN));
    }

    /**
     * Robin 1983 "for n >= 7022":
     * My tests say it works for n >= 8601.
     */
    public static long Robin1983(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + lnlnN - 0.9385));
    }

    /**
     * Dusart 1999 page 14 for n >= 39017
     */
    public static long Dusart1999(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + lnlnN - 0.9484));
    }

    /**
     * Dusart 2010 page 7, Lemma 6.5: Holds for n >= 178974
     * @see https://arxiv.org/PS_cache/arxiv/pdf/1002/1002.0442v1.pdf
     */
    public static long Dusart2010p7(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + lnlnN - 1 + ((lnlnN-1.95)/lnN)));
    }

    /**
     * Dusart 2010 page 8, Proposition 6.6: Holds for n >= 688383
     * @see https://arxiv.org/PS_cache/arxiv/pdf/1002/1002.0442v1.pdf
     */
    public static long Dusart2010p8(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + lnlnN - 1 + ((lnlnN-2)/lnN)));
    }

    /**
     * Axler 2013 page viii Korollar G for n >= 8009824
     */
    public static long Axler2013(long n) {
        double lnN = Math.log(n);
        double lnlnN = Math.log(lnN);
        return (long) Math.ceil(n * (lnN + lnlnN - 1 + (lnlnN-2)/lnN - (lnlnN*lnlnN - 6*lnlnN + 10.273)/(2*lnN*lnN)));
    }
}
