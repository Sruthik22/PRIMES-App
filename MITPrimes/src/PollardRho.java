import java.math.BigInteger;
import java.security.SecureRandom;

public class PollardRho extends PrimeFactorization {

	private static final SecureRandom RNG = new SecureRandom();

	BigInteger N;
	BigInteger ZERO = new BigInteger("0");
	BigInteger ONE = new BigInteger("1");

	public BigInteger findADivisor(BigInteger N) {

		this.N = N;
		BigInteger gcd;
		BigInteger x = generate_between_1_n_minus_1();
		BigInteger y = x;

		do {
			BigInteger b = generate_between_1_n_minus_1();

			do {
				x  = function(x, b);
				y = function(function(y, b), b);
				gcd = x.subtract(y).gcd(N);
			} while(gcd.equals(ONE));
		} while (gcd.equals(N));
		return gcd;
	}

	private BigInteger generate_between_1_n_minus_1() {
		int bitLength = N.bitLength();
		BigInteger x = new BigInteger(bitLength, RNG);
		x = addModN(x, ZERO);
		return x;
	}

	private BigInteger function(BigInteger x, BigInteger b) {
		return addModN(x.multiply(x).mod(N), b);
	}

	private BigInteger addModN(BigInteger a, BigInteger b) {
		BigInteger sum = a.add(b);
		if (sum.compareTo(N) < 0) {
			return sum;
		}

		else return sum.subtract(N);
	}
}