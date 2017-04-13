package se.kebr.generator;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class NumberGenerator {

	private SecureRandom random = new SecureRandom();

	public String generate() {
		return new BigInteger(40, random).toString(32);
	}

}
