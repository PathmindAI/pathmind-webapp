package io.skymind.pathmind.shared.constants;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum RunStatus {
	NotStarted(0, "Not Started"),
	Starting(1, "Starting Cluster"),
	Running(2, "Running"),
	Completed(3, "Completed"),
	Error(4, "Error"),
	Killed(5, "Stopped"),
	Restarting(6, "Restarting") ,
	Stopping(7, "Stopping");

	private int id;
	private String name;

	private static final Map<Integer,RunStatus> STATUS_BY_ID;
	private static final EnumSet<RunStatus> RUNNING_STATES = EnumSet.of(Starting, Running, Restarting);
	private static final EnumSet<RunStatus> FINISHED_STATES = EnumSet.of(Completed, Error, Killed);

	RunStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int getValue() {
		return id;
	}

	static {
		Map<Integer,RunStatus> map = new ConcurrentHashMap<>();
		for (RunStatus instance : RunStatus.values()) {
			map.put(instance.getValue(), instance);
		}
		STATUS_BY_ID = Collections.unmodifiableMap(map);
	}

	public static RunStatus getEnumFromValue(int value) {
		return STATUS_BY_ID.get(value);
	}

	public static boolean isRunning(RunStatus status){
		return RUNNING_STATES.contains(status);
	}

	public static boolean isFinished(RunStatus status){
		return FINISHED_STATES.contains(status);
	}
}