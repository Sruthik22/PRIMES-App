/*
 * java-math-library is a Java library focused on number theory, but not necessarily limited to it. It is based on the PSIQS 4.0 factoring project.
 * Copyright (C) 2018 Tilman Neumann (www.tilman-neumann.de)
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>.
 */

/**
 * A deterministic prime test for N < 32 bit using fast trial division.
 */
public class TDivPrimeTest {

    private static final int NUM_PRIMES_FOR_31_BIT_TDIV = 4793;
    private ListOfPrimes SMALL_PRIMES = ListOfPrimes.get();

    private int[] primes;
    private long[] pinv;

    private static TDivPrimeTest instance = new TDivPrimeTest();
    public static TDivPrimeTest getInstance() { return instance; } //Singleton

    private TDivPrimeTest() {
        primes = new int[NUM_PRIMES_FOR_31_BIT_TDIV];
        pinv = new long[NUM_PRIMES_FOR_31_BIT_TDIV];
        for (int i=0; i<NUM_PRIMES_FOR_31_BIT_TDIV; i++) {
            int p = SMALL_PRIMES.getNthPrime(i);
            primes[i] = p;
            pinv[i] = (1L<<32)/p;
        }
    }

    public boolean isPrime(int N) {
        if (N==1) return false;
        if ((N&1)==0) return N==2;

        // if N is odd and composite then the loop runs maximally up to prime = floor(sqrt(N)); unroll the loop.
        int i=1;
        int unrolledLimit = NUM_PRIMES_FOR_31_BIT_TDIV-8;
        for ( ; i<unrolledLimit; i++) {
            if ((1 + (int) ((N*pinv[i])>>32)) * primes[i] == N) return primes[i]==N;
            if ((1 + (int) ((N*pinv[++i])>>32)) * primes[i] == N) return primes[i]==N;
            if ((1 + (int) ((N*pinv[++i])>>32)) * primes[i] == N) return primes[i]==N;
            if ((1 + (int) ((N*pinv[++i])>>32)) * primes[i] == N) return primes[i]==N;
            if ((1 + (int) ((N*pinv[++i])>>32)) * primes[i] == N) return primes[i]==N;
            if ((1 + (int) ((N*pinv[++i])>>32)) * primes[i] == N) return primes[i]==N;
            if ((1 + (int) ((N*pinv[++i])>>32)) * primes[i] == N) return primes[i]==N;
            if ((1 + (int) ((N*pinv[++i])>>32)) * primes[i] == N) return primes[i]==N;
        }
        for ( ; i<NUM_PRIMES_FOR_31_BIT_TDIV; i++) {
            if ((1 + (int) ((N*pinv[i])>>32)) * primes[i] == N) return primes[i]==N;
        }
        // N is prime
        return true;
    }
}
