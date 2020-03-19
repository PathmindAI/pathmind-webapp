package io.skymind.pathmind.shared.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MockDefaultValues
{
    private static boolean isDebugAccelerate = false;

    // Number of seconds for the backend to check for updates.
    // IMPORTANT NOTICE -> 1 second is great but it really slows down Intellij over time. Setting to 5 for me had very little Intellij performance impact. Set accordingly.
    public static final int DEBUG_ACCELERATE_UPDATE_INTERVAL = 2;

    public static final int NEW_PROJECT_NUMBER_OF_OBSERVATIONS = 70;
    public static final int NEW_PROJECT_NUMBER_OF_POSSIBLE_ACTIONS = 125;

    public static final String NEW_EXPERIMENT_REWARD_FUNCTION = "reward = after[0] - before[0];";

    @Value("${skymind.debug.accelerate}")
    private boolean isDebugAccelerateHack = false;

    // Janky solution to get the value injected through Spring based on the system environment variable. It's done this way
    // because not everything is injected, at least not yet.
    @PostConstruct
    public void init() {
        isDebugAccelerate = isDebugAccelerateHack;
    }

    public static boolean isDebugAccelerate() {
        return isDebugAccelerate;
    }

    public static String getProjectName() {
        LocalDateTime now = LocalDateTime.now();
        return "Project - " + now.format(DateTimeFormatter.ofPattern("MMM dd")) + " - " +  now.format(DateTimeFormatter.ofPattern("HH:mm a"));
    }
}
