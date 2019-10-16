package io.skymind.pathmind.data.utils;

import io.skymind.pathmind.data.Run;

import java.time.Duration;
import java.time.LocalDateTime;

public class RunUtils
{
	private RunUtils() {
	}

	public static long getElapsedTime(Run run)
	{
		if(run == null || run.getStartedAt() == null)
			return 0;

		return run.getStoppedAt() == null ?
				Duration.between(run.getStartedAt(), LocalDateTime.now()).toSeconds() :
				Duration.between(run.getStartedAt(), run.getStoppedAt()).toSeconds();
	}
}
