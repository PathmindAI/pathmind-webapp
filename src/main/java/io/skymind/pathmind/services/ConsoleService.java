package io.skymind.pathmind.services;

import java.util.Random;

public class ConsoleService
{
	// TODO -> Implement.
	public static String getConsoleLogForRun(long runId)
	{
		Random random = new Random();

		int date = random.nextInt(30);
		int hour = random.nextInt(12);

		return 	"Jan " + date++ + " at " + hour++ + ":30pm -> Ran something or other.\n" +
				"Jan " + date++ + " at " + hour++ + ":31pm -> Ran something else.\n" +
				"Jan " + date +   " at " + hour   + ":32pm -> Completed something.";
	}
}
