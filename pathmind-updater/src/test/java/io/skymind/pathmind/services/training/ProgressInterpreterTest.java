package io.skymind.pathmind.services.training;

import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.updater.ProgressInterpreter;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProgressInterpreterTest {
    private static String name = "PPO_PathmindEnvironment_0_num_sgd_iter=30,sgd_minibatch_size=2048,train_batch_size=12000_2020-08-18_22-16-53bpjqaxzi";
    private static String fileContents;

    @BeforeClass
    public static void beforeClass() {
        try {
            fileContents = FileUtils.readFileToString(ResourceUtils.getFile("classpath:progress.csv"), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testInterpreter(){
        final Policy policy = ProgressInterpreter.interpret(Map.entry(name, fileContents));

        final LocalDateTime utcTime = LocalDateTime.parse("2020-08-18_22-16-53", DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss"));
        final LocalDateTime time = ZonedDateTime.ofInstant(utcTime.toInstant(ZoneOffset.UTC), Clock.systemDefaultZone().getZone()).toLocalDateTime();

        assertEquals(time, policy.getStartedAt());
        assertEquals(51, policy.getScores().size());
        assertEquals(0.19725925509919762, policy.getScores().get(16).getMax(), 0);
    }

    @Test
    public void testMetricsRawInterpreter() {
        Policy policy = new Policy();
        ProgressInterpreter.interpretMetricsRaw(Map.entry(name, fileContents), policy, null, 0, 4);

        assertNotNull(policy.getMetricsRaws());
        assertEquals(51, policy.getMetricsRaws().size());                           // total iteration
        assertEquals(1, policy.getMetricsRaws().get(0).getIteration().intValue());  // the current iteration number
        assertEquals(24, policy.getMetricsRaws().get(0).getEpisodeRaw().size());    // total episode for the current iteration
        assertEquals(4, policy.getMetricsRaws().get(0).getEpisodeRaw().get(0).size()); // total index
    }



}