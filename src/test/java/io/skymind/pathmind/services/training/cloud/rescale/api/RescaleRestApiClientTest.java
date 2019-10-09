package io.skymind.pathmind.services.training.cloud.rescale.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.services.training.cloud.rescale.api.dto.RescaleFile;
import io.skymind.pathmind.utils.ObjectMapperHolder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
public class RescaleRestApiClientTest {

    @Test
    public void testUploadFilesToRescale() throws IOException {
        ObjectMapper mapper = ObjectMapperHolder.getJsonMapper();

        final RescaleRestApiClient client = new RescaleRestApiClient("https://platform.rescale.jp/api/v2",
                "0d0601925a547db44d41007e3cc4386b075c761c",
                mapper,
                WebClient.builder());

        File file = new ClassPathResource("static/Sample.txt").getFile();

        RescaleFile rescaleFile = client.fileUpload(file, "Sample.txt");

        assertNotNull(rescaleFile);
        assertNotNull(rescaleFile.getId());

        byte[] contents = client.fileContents(rescaleFile.getId());

        assertEquals(contents.length, file.length());
    }

}