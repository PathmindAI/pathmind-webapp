package io.skymind.pathmind.shared.security;

public class Routes {
    public static final String WITH_PARAMETER = "/**";

    public static final String ROOT_URL = "";

    public static final String LOGIN_URL = "sign-in";
    public static final String LOGIN_PROCESSING_URL = "sign-in";
    
    public static final String LOGOUT_URL = "sign-out";

    // Need to pass a x-ms-routing-name GET param on logout to make sure that after a logout
    // the browser will be redirected to the latest version of the application when using
    // canary deployments and slots. Param value can be anything.
    public static final String LOGOUT_SUCCESS_URL = "?x-ms-routing-name=latest";
    public static final String BAD_CREDENTIALS = "bad-credentials";
    public static final String EMAIL_VERIFICATION_FAILED = "email-verification-failed";

    public static final String SIGN_UP_URL = "early-access-sign-up";
    public static final String RESET_PASSWORD_URL = "reset-password";
    public static final String EMAIL_VERIFICATION_URL = "email-verification";
    public static final String VERIFICATION_EMAIL_SENT_URL = "verification-email-sent";

    public static final String ACCOUNT_EDIT_URL = "account/edit";
    public static final String ACCOUNT_URL = "account";
    public static final String ACCOUNT_CHANGE_PASS_URL = "account/change-password";
    public static final String ACCOUNT_UPGRADE_URL = "account/upgrade";
    public static final String PAYMENT_URL = "payment";
    public static final String UPGRADE_DONE = "upgrade-done";

    public static final String NEW_PROJECT = "newProject";
    public static final String MODEL_URL = "model";
    public static final String EXPERIMENT_URL = "experiment";
    public static final String NEW_EXPERIMENT = "newExperiment";
    public static final String UPLOAD_MODEL = "uploadModel";
    public static final String RESUME_UPLOAD_MODEL = "resumeUploadModel";
    public static final String ERROR_URL = "error";

    public static final String GUIDE_URL = "guide/overview";
    public static final String GUIDE_INSTALL_URL = "guide/install";
    public static final String GUIDE_OBSERVATION_URL = "guide/observation";
    public static final String GUIDE_ACTION_SPACE_URL = "guide/action-space";
    public static final String GUIDE_TRIGGER_ACTIONS_URL = "guide/trigger-actions";
    public static final String GUIDE_DONE_URL = "guide/done-condition";
    public static final String GUIDE_REWARD_URL = "guide/reward";
    public static final String GUIDE_RECAP_URL = "guide/recap";

    public static final String EXPORT_POLICY_URL = "exportPolicy";
    public static final String PROJECT_URL = "project";
    public static final String PROJECTS_URL = "projects";
    public static final String DASHBOARD_URL = "dashboard";
}
