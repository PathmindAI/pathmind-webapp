package io.skymind.pathmind.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.skymind.pathmind.services.project.ProjectFileCheckService;
import io.skymind.pathmind.services.project.rest.ModelAnalyzerApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "io.skymind.pathmind")
@PropertySource({"application.properties", "shared.properties"})
@EnableAsync
public class PathmindApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PathmindApiApplication.class, args);
    }

    @Bean
    public ExecutorService checkerExecutorService(@Value("${pathmind.filecheck.poolsize}") int poolSize) {
        return Executors.newFixedThreadPool(poolSize);
    }

    @Bean
    public ProjectFileCheckService projectFileCheckService(ExecutorService executorService, ModelAnalyzerApiClient modelAnalyzerApiClient,
                                                           @Value("${pathmind.convert-models-to-latest-version.url}") String convertModelsToSupportLastestVersionURL) {
        return new ProjectFileCheckService(executorService, modelAnalyzerApiClient, convertModelsToSupportLastestVersionURL);
    }

}
