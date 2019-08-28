package io.skymind.pathmind.data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO -> Remove as this is just a placeholder for fake data for now.
public class MockData
{
	private String name;
	private String value;

	public MockData(String name, String value) {
		this.name = name;
		this.value = value;
	}

	// TODO -> Delete fake data
	private static int count = 0;
	public static List<MockData> generateFakeData(int dataCount) {
		count++;
		return Stream.generate(() ->
				new MockData("Name " + count, Integer.toString(count)))
				.limit(dataCount)
				.collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
