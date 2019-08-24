package io.skymind.pathmind;

import io.skymind.pathmind.init.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PathmindApplication implements CommandLineRunner
{
	@Autowired
	private DataLoader dataLoader;

	public static void main(String[] args)
	{
		SpringApplication.run(PathmindApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		dataLoader.initDatabase();
	}
}
