package io.skymind.pathmind.data.utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FakeDataUtils
{
	private static final Random RANDOM = new Random();

	private FakeDataUtils() {
	}

	public static int getRandomInt(int bound) {
		return RANDOM.nextInt(bound);
	}

	public static long getRandomLong(long bound) {
		return RANDOM.nextLong() % bound;
	}
}
