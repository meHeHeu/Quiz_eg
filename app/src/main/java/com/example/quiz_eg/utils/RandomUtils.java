package com.example.quiz_eg.utils;

import java.util.List;
import java.util.Random;

public final class RandomUtils {

	private static Random random;

	static {
		random = new Random();
	}

	/**
	 * Fisher–Yates shuffle for lists.
	 *
	 * @param list - a list to shuffle
	 */
	public static <T> void ShuffleList(List<T> list) {
		for (int i = list.size() - 1; i > 0; --i) {
			int j = random.nextInt(i + 1);

			T objA = list.get(i);
			T objB = list.get(j);
			list.set(i, objB);
			list.set(j, objA);
		}
	}

	/**
	 * Fisher–Yates shuffle for arrays.
	 *
	 * @param array - an array to shuffle
	 */
	public static <T> void ShuffleArray(T[] array) {
		for (int i = array.length - 1; i > 0; --i) {
			int j = random.nextInt(i + 1);

			T temp = array[j];
			array[j] = array[i];
			array[i] = temp;
		}
	}

	private RandomUtils() {
	}
}
