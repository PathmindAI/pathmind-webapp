package io.skymind.pathmind.data;

import java.util.*;

// TODO -> Remove as this is just a placeholder for fake data for now.
public class MockData
{
	private String name;
	private String value;

	public static final List<MockData> FAKE_DATA = Arrays.asList(
			new MockData("Alice", "aaaaaaaa"),
			new MockData("Bob", "bbbbbbbb"),
			new MockData("John", "cccccccc"),
			new MockData("Fred", "dddddddd"),
			new MockData("Ivy", "eeeeeeee"),
			new MockData("Ted", "ffffffff"),
			new MockData("Jane", "gggggggg"),
			new MockData("Ellie", "hhhhhhhh"));

	public MockData(String name, String value) {
		this.name = name;
		this.value = value;
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
