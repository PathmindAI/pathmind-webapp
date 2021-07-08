package io.skymind.pathmind.webapp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.SpringVaadinSession;
import io.skymind.pathmind.services.training.cloud.aws.api.AWSApiClient;
import io.skymind.pathmind.services.training.versions.AWSFileManager;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import io.skymind.pathmind.shared.services.training.versions.*;
import io.skymind.pathmind.shared.utils.ObjectMapperHolder;
import io.skymind.pathmind.webapp.ActiveSessionsRegistry;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.skymind.pathmind.webapp.security.constants.VaadinSessionInfo.IS_OLD_VERSION;

@RestController
@Slf4j
public class VersionController {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class LibraryVersion {
        private NativeRL nativeRL;
        private Conda conda;
        private PathmindHelper pathmindHelper;
        private AnyLogic anyLogic;

        @Override
        public boolean equals(Object obj) {
            return this.nativeRL == ((LibraryVersion)obj).nativeRL &&
                this.conda == ((LibraryVersion)obj).conda &&
                this.pathmindHelper == ((LibraryVersion)obj).pathmindHelper &&
                this.anyLogic == ((LibraryVersion)obj).anyLogic;
        }

        public List<VersionEnum> toList() {
            return List.of(nativeRL, conda, pathmindHelper, anyLogic);
        }
    }

    public static final String ATTRIBUTE_VAADIN_SPRING_SERVLET = "com.vaadin.flow.server.VaadinSession.springServlet";

    private final ActiveSessionsRegistry activeSessionsRegistry;

    private static final String VERSION_FILE = "version";
    private final AWSApiClient awsApiClient;
    private final ExecutionEnvironmentManager environmentManager;
    private final AWSFileManager fileManager;
    private @Value("${pathmind.aws.s3.bucket.static}") String staticBucketName;

    private ObjectMapper objectMapper = ObjectMapperHolder.getJsonMapper();

    @Autowired
    public VersionController(ActiveSessionsRegistry activeSessionsRegistry,
                             AWSApiClient awsApiClient,
                             ExecutionEnvironmentManager environmentManager) {
        this.activeSessionsRegistry = activeSessionsRegistry;
        this.awsApiClient = awsApiClient;
        this.environmentManager = environmentManager;
        this.fileManager = AWSFileManager.getInstance();
    }

    /**
     * This REST API is used to notify current active users of the application that a newer version
     * of the app is available.  A notification is shown for those active users to tell to sign out
     * and sign in to use the latest version.
     */
    @PostMapping(value = "/api/newVersionAvailable")
    public Response newVersionAvailable() {
        Collection<HttpSession> activeAuthenticatedSessions = activeSessionsRegistry.getActiveAuthenticatedSessions();
        AtomicInteger count = new AtomicInteger(0);
        for (HttpSession activeSession : activeAuthenticatedSessions) {

            SpringVaadinSession springVaadinSession = (SpringVaadinSession) activeSession.getAttribute(ATTRIBUTE_VAADIN_SPRING_SERVLET);
            if (springVaadinSession != null) {
                springVaadinSession.access(() -> {
                    for (UI ui : springVaadinSession.getUIs()) {
                        try {
                            UI.setCurrent(ui);
                            NotificationUtils.showNewVersionAvailableNotification(ui);
                            springVaadinSession.setAttribute(IS_OLD_VERSION, true);
                            count.incrementAndGet();
                        } finally {
                            UI.setCurrent(null);
                        }
                    }
                });
            }
        }

        String message = String.format("Sent a new version available message to %s HTTP sessions and %s Vaadin UIs", activeAuthenticatedSessions.size(), count.get());
        log.info(message);
        return new Response(message);
    }

    @PutMapping(value = "/api/MAlib/{env}")
    public Response modelAnalyzerLibrary(@PathVariable String env) throws IOException {
        //todo will change the path from {env}-ma-static to {env}-static s3 path
        //{env}-model-analyzer-static-files.pathmind.com
        String libS3Path = env + "-model-analyzer-static-files.pathmind.com";

        byte[] versionByte = awsApiClient.fileContents(libS3Path, VERSION_FILE, true);
        LibraryVersion oldLibVersion = versionByte != null ? objectMapper.readValue(versionByte, LibraryVersion.class) : null;

        ExecutionEnvironment execEnv = this.environmentManager.getEnvironment(0);
        LibraryVersion libVersion = LibraryVersion.builder()
            .anyLogic(execEnv.getAnylogicVersion())
            .conda(execEnv.getCondaVersion())
            .nativeRL(execEnv.getNativerlVersion())
            .pathmindHelper(execEnv.getPathmindHelperVersion())
            .build();

        if (oldLibVersion == null || !oldLibVersion.equals(libVersion)) {
            SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
            String backupPath = libS3Path + "/backup_" + format.format(new Date());
            if (awsApiClient.fileExists(libS3Path, VERSION_FILE)) {
                awsApiClient.copyFile(libS3Path, VERSION_FILE, backupPath, VERSION_FILE);
                log.info("Made a backup of {} to {}", (libS3Path + "/" + VERSION_FILE), (backupPath + "/" + VERSION_FILE));
            }

            libVersion.toList().parallelStream().forEach(v -> {
                Pair<String, String> fileSrcAndDesc = fileManager.libFilePaths(v);
                if (awsApiClient.fileExists(libS3Path, fileSrcAndDesc.getRight())) {
                    awsApiClient.copyFile(libS3Path, fileSrcAndDesc.getRight(), backupPath, fileSrcAndDesc.getRight());
                    log.info("Made a backup of {} to {}", (libS3Path + "/" + fileSrcAndDesc.getRight()), (backupPath + "/" + fileSrcAndDesc.getRight()));
                }
                awsApiClient.copyFile(staticBucketName, fileSrcAndDesc.getLeft(), libS3Path, fileSrcAndDesc.getRight());
                log.info("{} is copied to {}", (staticBucketName + "/" + fileSrcAndDesc.getLeft()), (libS3Path + "/" + fileSrcAndDesc.getRight()));
            });

            awsApiClient.fileUpload(libS3Path, VERSION_FILE, objectMapper.writeValueAsBytes(libVersion));
        }

        return new Response(String.format("The MA libraries in %s are up-to-date", libS3Path));
    }
}
