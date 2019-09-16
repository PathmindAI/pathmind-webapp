package io.skymind.pathmind.services.training;

public interface ExecutionProvider {
    public String execute(JobSpec job, ExecutionEnvironment env);
}
