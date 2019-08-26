package io.skymind.pathmind.data;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO -> Implement correctly.
public class ConsoleEntry
{
	private long id;
	private String description;
	private String status;
	private int  score;
	private LocalDate date;

	public ConsoleEntry(long id, String description, String status, int score, LocalDate date) {
		this.id = id;
		this.description = description;
		this.status = status;
		this.score = score;
		this.date = date;
	}

	private static int count = 1;
	// TODO -> Remove. Fake data for now.
	public static List<ConsoleEntry> getFakeData() {
		return Stream.generate(() ->
				new ConsoleEntry(count, "Some kind of action " + count, "status " + count, count++, LocalDate.now()))
				.limit(10)
				.collect(Collectors.toList());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
