package io.skymind.pathmind.services.training.cloud.aws;

import org.apache.tika.io.IOUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AWSExecutionProviderTest {

    @Test
    public void error_not_on_last_line() throws IOException {
        File file = ResourceUtils.getFile("classpath:rl-lib-errors/error-not-on-last-line.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(file));
        String line = AWSExecutionProvider.findLineWithException(lines);
        assertThat(line).startsWith("ray.memory_monitor.RayOutOfMemoryError: More than 95%");
    }

    @Test
    public void error_is_on_last_line() throws IOException {
        File file = ResourceUtils.getFile("classpath:rl-lib-errors/error-is-on-last-line.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(file));
        String line = AWSExecutionProvider.findLineWithException(lines);
        assertThat(line).startsWith("RuntimeError: java.lang.ClassNotFoundException: carbon_emissions.PathmindEnvironment");
    }

}