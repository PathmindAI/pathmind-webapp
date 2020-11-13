package io.skymind.pathmind.api.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
public class AnyLogicUploadController {

    private final String webappDomainUrl;

    public AnyLogicUploadController(@Value("${pm.api.webapp.url}") String webappDomainUrl) {
        this.webappDomainUrl = webappDomainUrl;
    }

    /*
    curl -i -XPOST -H "X-PM-API-TOKEN: 1d83d812-f79a-497c-a437-ec78957d294a" -F 'file=@/Users/malex/Downloads/PathmindAPIUsage.zip' http://localhost:8081/al/upload
     */
    @PostMapping("/al/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {

        log.debug("saving file {}", file.getOriginalFilename());
        try {
            Path tempFile = Files.createTempFile("pm-al-upload", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());
            log.debug("saved file {} to temp location {}", file.getOriginalFilename(), tempFile);

            int experimentId = new Random().nextInt(2000) + 5000; // todo: replace with create experiment

            URI experimentUri = UriComponentsBuilder.fromHttpUrl(webappDomainUrl)
                    .path("experiment").path("/{experimentId}")
                    .buildAndExpand(Map.of("experimentId", experimentId))
                    .toUri();

            return ResponseEntity.status(HttpStatus.CREATED).location(experimentUri).build();
        } catch (Exception e) {
            log.error("failed to get file from AL", e);
            throw new RuntimeException("failed to process zip file", e);
        }

    }

}
