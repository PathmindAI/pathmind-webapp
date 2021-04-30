package io.skymind.pathmind.shared.security;

public class Routes {
    public static final String WITH_PARAMETER = "/**";

    public static final String LOGIN = "sign-in";
    public static final String LOGIN_PROCESSING = "sign-in";

    public static final String LOGOUT = "sign-out";
    public static final String LOGOUT_SUCCESS = "sign-in?sign-out";
    public static final String SESSION_EXPIRED = "session-expired";
    public static final String BAD_CREDENTIALS = "bad-credentials";
    public static final String EMAIL_VERIFICATION_FAILED = "email-verification-failed";

    public static final String EARLY_ACCESS_SIGN_UP = "early-access-sign-up";
    public static final String SIGN_UP = "sign-up";
    public static final String RESET_PASSWORD = "reset-password";
    public static final String EMAIL_VERIFICATION = "email-verification";
    public static final String VERIFICATION_EMAIL_SENT = "verification-email-sent";
    public static final String ONBOARDING_PAYMENT_SUCCESS = "onboarding-payment-success";

    public static final String ACCOUNT_EDIT = "account/edit";
    public static final String ACCOUNT = "account";
    public static final String ACCOUNT_CHANGE_PASS = "account/change-password";
    public static final String ACCOUNT_UPGRADE = "account/upgrade";
    public static final String PAYMENT = "payment";
    public static final String UPGRADE_DONE = "upgrade-done";

    public static final String NEW_PROJECT = "newProject";
    public static final String MODEL_PATH = "/model/";
    public static final String EXPERIMENT = "experiment";
    public static final String NEW_EXPERIMENT = "newExperiment";
    public static final String SHARED_EXPERIMENT = "sharedExperiment";
    public static final String UPLOAD_MODEL = "uploadModel";
    public static final String RESUME_UPLOAD_MODEL = "resumeUploadModel";
    public static final String ERROR = "error";
    public static final String UPLOAD_MODEL_ERROR = "uploadModelError";

    public static final String EXPORT_POLICY = "exportPolicy";
    public static final String PROJECT = "project";
    public static final String PROJECTS = "projects";
    public static final String DASHBOARD = "dashboard";

    public static final String SETTINGS = "settings";

    public static final String SEARCHRESULTS = "search-results";
}
