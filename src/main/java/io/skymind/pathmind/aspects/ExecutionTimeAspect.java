package io.skymind.pathmind.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

	private static final long MAX_EXECUTION_TIME_IN_MILLIS = 2000; // 2 seconds is a max execution time for a query
	private static final String SLOW_QUERY_WARN_MSG = "Query %s took %s ms to retrieve values from DB";

	@Around("@annotation(MonitorExecutionTime)")
	public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		Instant start = Instant.now();

		Object proceed = joinPoint.proceed();

		final long differenceInMs = Duration.between(start, Instant.now()).toMillis();

		if(differenceInMs > MAX_EXECUTION_TIME_IN_MILLIS){
			log.warn(String.format(SLOW_QUERY_WARN_MSG, getMethodName(joinPoint), differenceInMs));
		}

		return proceed;
	}

	private String getMethodName(ProceedingJoinPoint joinPoint) {
		return ((MethodSignature) joinPoint.getSignature()).getMethod() + " with args: " + Arrays.toString(joinPoint.getArgs());
	}
}
