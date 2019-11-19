package io.skymind.pathmind.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Provides methods for accessing Spring beans from classes
 * which are not beans
 */
@Component
public class SpringUtils {

	@Autowired
	private ApplicationContext applicationContext;

	private static SpringUtils instance;

	@PostConstruct
	protected void initialize() {
		instance = this;
	}

	/**
	 * returns bean of given type from current application context
	 */
	public static <T> T getBean(Class<T> beanType) {
		return instance.applicationContext.getBean(beanType);
	}

}
