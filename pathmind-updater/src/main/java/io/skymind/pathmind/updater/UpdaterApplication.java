package io.skymind.pathmind.updater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
@PropertySource({"application.properties", "shared.properties"})
public class UpdaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpdaterApplication.class, args);
	}
}
