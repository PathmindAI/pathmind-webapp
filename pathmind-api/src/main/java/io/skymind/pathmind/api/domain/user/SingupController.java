package io.skymind.pathmind.api.domain.user;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import io.skymind.pathmind.api.domain.user.dto.SignUpCheckRequest;
import io.skymind.pathmind.api.domain.user.dto.SignUpRequest;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.analytics.SegmentTrackerService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.shared.data.PathmindUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import static io.skymind.pathmind.api.domain.user.SingupController.SIGNUP_URL;
import static io.skymind.pathmind.shared.segment.SegmentTrackingEvents.EVENT_SIGN_UP;
import static io.skymind.pathmind.shared.segment.SegmentTrackingEvents.EVENT_VERIFICATION_EMAIL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = SIGNUP_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class SingupController {

    public static final String SIGNUP_URL = "/signup";

    private final URI webappSignupUri;
    private final UserDAO userDAO;
    private final SegmentTrackerService segmentTrackerService;
    private final EmailNotificationService emailNotificationService;

    public SingupController(@Value("${pm.api.webapp.url}") String webappDomainUrl,
                            UserDAO userDAO,
                            SegmentTrackerService segmentTrackerService,
                            EmailNotificationService emailNotificationService) {
        this.webappSignupUri = UriComponentsBuilder.fromHttpUrl(webappDomainUrl).path("sign-in").build().toUri();
        this.userDAO = userDAO;
        this.segmentTrackerService = segmentTrackerService;
        this.emailNotificationService = emailNotificationService;
    }

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {

        if (userDAO.findByEmailIgnoreCase(signUpRequest.getEmail()) != null) {
            final HttpStatus status = HttpStatus.CONFLICT;
            return ResponseEntity.status(status)
                    .body(buildErrorMap(status, "email is already in use"));
        }

        PathmindUser user = new PathmindUser();
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstname(signUpRequest.getFirstName());
        user.setLastname(signUpRequest.getLastName());
        user.setEmailVerificationToken(UUID.randomUUID());
        long id = userDAO.insertUser(user);
        log.info("New user signed up: " + user.getEmail());
        final PathmindUser newUser = userDAO.findById(id);
        segmentTrackerService.track(EVENT_SIGN_UP, String.valueOf(id), new HashMap<>() {{
                    put("userId", String.valueOf(newUser.getId()));
                    put("userName", newUser.getName());
                    put("userEmail", newUser.getEmail());
                }}
        );
        emailNotificationService.sendVerificationEmail(user, user.getEmail(), true);
        segmentTrackerService.track(EVENT_VERIFICATION_EMAIL, String.valueOf(id), new HashMap<>() {{
                    put("userId", String.valueOf(newUser.getId()));
                    put("userName", newUser.getName());
                    put("userEmail", newUser.getEmail());
                }}
        );
        return ResponseEntity.created(webappSignupUri).build();
    }

    @PostMapping(path = "/check")
    public ResponseEntity<Boolean> signUp(@RequestBody @Valid SignUpCheckRequest checkRequest) {
        final String email = StringUtils.trimToEmpty(checkRequest.getEmail());
        if (userDAO.findByEmailIgnoreCase(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> processInputError(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        Map<String, Object> errorMap = buildErrorMap(
                HttpStatus.BAD_REQUEST,
                "Validation errors on form. Total " + exception.getBindingResult().getFieldErrorCount()
        );
        errorMap.put("errors", fieldErrors);
        return errorMap;
    }

    Map<String, Object> buildErrorMap(HttpStatus status, String message) {
        return Map.of("timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message,
                "path", SIGNUP_URL);
    }

}
