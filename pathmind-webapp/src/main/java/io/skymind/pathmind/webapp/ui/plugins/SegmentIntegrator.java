package io.skymind.pathmind.webapp.ui.plugins;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.Json;
import elemental.json.JsonObject;
import io.skymind.pathmind.shared.data.ArchivableData;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.data.user.UserMetrics;
import io.skymind.pathmind.shared.security.PathmindUserDetails;
import io.skymind.pathmind.shared.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.shared.segment.SegmentTrackingEvents.*;

/**
 * SegmentIntegrator component is client side counter part of <code>SegmentTrackerService</code>
 * This component runs on user browser, and tracks user event using Segment JS API
 */
@SpringComponent
@UIScope
@Tag("segment-integrator")
@JsModule("./src/plugins/segment-integrator.js")
@Slf4j
public class SegmentIntegrator extends PolymerTemplate<SegmentIntegrator.Model> {

    private String sourceKey;
    private boolean enabled;
    private PathmindUserDetails user;

    public SegmentIntegrator(@Value("${pathmind.segment.website.source.key}") String key,
                             @Value("${pathmind.segment.enabled}") Boolean enabled) {
        this.sourceKey = key;
        this.enabled = enabled;
    }

    public void marketingSiteLead(String plan) {
        JsonObject additionalInfo = Json.createObject();
        additionalInfo.put("plan", plan);
        track(EVENT_MARKETING_SITE_LEAD, additionalInfo);
    }

    public void userLoggedIn() {
        track(EVENT_LOGIN);
    }

    public void userRegistered(PathmindUser user) {
        JsonObject additionalInfo = Json.createObject();
        additionalInfo.put("userId", user.getId());
        additionalInfo.put("userName", user.getName());
        additionalInfo.put("userEmail", user.getEmail());
        track(EVENT_SIGN_UP, additionalInfo);
    }

    public void verificationEmailSent() {
        track(EVENT_VERIFICATION_EMAIL);
    }

    public void emailVerified(PathmindUser user) {
        JsonObject additionalInfo = Json.createObject();
        if (user != null) {
            additionalInfo.put("userId", user.getId());
            additionalInfo.put("userName", user.getName());
            additionalInfo.put("userEmail", user.getEmail());
        }
        track(EVENT_VERIFY_EMAIL, additionalInfo);
    }

    public void modelImported(boolean result) {
        JsonObject additionalInfo = Json.createObject();
        additionalInfo.put("result", result ? "success" : "failed");
        track(EVENT_IMPORT_MODEL, additionalInfo);
    }

    public void observationsSelected() {
        track(EVENT_OBSERVATIONS_SELECTED);
    }

    public void onboardingTutorialClicked() {
        track(EVENT_ONBOARDING_TUTORIAL);
    }

    public void onboardingZipDownloaded() {
        track(EVENT_ONBOARDING_ZIP);
    }

    public void createProjectFromExample(String demoName) {
        JsonObject additionalInfo = Json.createObject();
        additionalInfo.put("demo", demoName);
        track(EVENT_CREATE_PROJECT_FROM_EXAMPLE, additionalInfo);
    }

    public void createFirstProject() {
        track(EVENT_CREATE_FIRST_PROJECT);
    }

    public void projectCreated() {
        track(EVENT_CREATE_PROJECT);
    }

    public void policyExported() {
        track(EVENT_EXPORT_POLICY);
    }

    public void draftSaved() {
        track(EVENT_SAVE_EXPERIMENT_DRAFT);
    }

    public void uploadModelError() {
        track(EVENT_UPLOAD_MODEL_ERROR);
    }

    public void modelDraftSaved() {
        track(EVENT_SAVE_MODEL_DRAFT);
    }

    public void passwordChanged() {
        track(EVENT_CHANGE_PW);
    }

    public void infoEdited() {
        track(EVENT_EDIT_INFO);
    }

    public void upgradeToProPlanClicked() {
        track(EVENT_CHOSE_PRO_PLAN);
    }

    public void accountUpgradedPro() {
        track(EVENT_ACCOUNT_UPGRADE_PRO);
    }

    public void navigatedToPricingFromAccountView() {
        track(EVENT_NAVIGATED_TO_PRICING_FROM_ACCOUNT_VIEW);
    }

    public void navigatedToPricingFromNewExpViewBanner() {
        track(EVENT_NAVIGATED_TO_PRICING_FROM_NEW_EXPERIMENT_VIEW_BANNER);
    }

    public void navigatedToPricingFromNewExpViewSettings() {
        track(EVENT_NAVIGATED_TO_PRICING_FROM_NEW_EXPERIMENT_VIEW_SETTINGS);
    }

    public void navigatedToPricingFromNewExpViewSimulationParameters() {
        track(EVENT_NAVIGATED_TO_PRICING_FROM_NEW_EXPERIMENT_VIEW_SIMULATION_PARAMETERS);
    }

    public void navigatedToPricingFromPolicyServerLimitPopup() {
        track(EVENT_NAVIGATED_TO_PRICING_FROM_POLICY_SERVER_LIMIT_POPUP);
    }

    public void navigatedToEditGoalsFromProjectView() {
        track(EVENT_NAVIGATED_TO_EDIT_GOALS_FROM_PROJECT_VIEW);
    }

    public void editGoals() {
        track(EVENT_EDIT_GOALS);
    }

    public void subscriptionCancelled() {
        track(EVENT_CANCEL_SUBSCRIPTION);
    }

    public void updatedNotesModelsView() {
        track(EVENT_UPDATED_NOTES_MODELS_VIEW);
    }

    public void updatedNotesExperimentsView() {
        track(EVENT_UPDATED_NOTES_EXPERIMENTS_VIEW);
    }

    public void updatedNotesExperimentView() {
        track(EVENT_UPDATED_NOTES_EXPERIMENT_VIEW);
    }

    public void addedNotesUploadModelView() {
        track(EVENT_ADDED_NOTES_UPLOAD_MODEL_VIEW);
    }

    public void addedNotesNewExperimentView() {
        track(EVENT_ADDED_NOTES_NEW_EXPERIMENT_VIEW);
    }

    public void performedSearch() {
        track(EVENT_SEARCHED_SITE);
    }

    public void newExperiment() {
        track(EVENT_NEW_EXPERIMENT);
    }

    public void startTraining() {
        track(EVENT_START_TRAINING);
    }

    public void stopTraining() {
        track(EVENT_STOP_TRAINING);
    }

    public void downloadedALP() {
        track(EVENT_DOWNLOAD_MODEL_ALP);
    }

    public void deployPolicyServer() {
        track(EVENT_POLICY_SERVER_DEPLOYMENT_STARTED);
    }

    public void redeployPolicyServer() {
        track(EVENT_POLICY_SERVER_REDEPLOYMENT_STARTED);
    }

    public void shutDownPolicyServer() {
        track(EVENT_POLICY_SERVER_SHUT_DOWN);
    }

    public void archived(Class<? extends ArchivableData> objectClass, boolean isArchived) {
        JsonObject additionalInfo = Json.createObject();
        additionalInfo.put("type", objectClass.getSimpleName().toLowerCase());
        String event = isArchived ? EVENT_ARCHIVED : EVENT_UNARCHIVED;
        track(event, additionalInfo);
    }

    public void userRunCapLimitReached(PathmindUserDetails user, UserMetrics.UserCapType userCapType, int percentage) {
        JsonObject additionalInfo = Json.createObject();
        additionalInfo.put("userId", user.getId());
        additionalInfo.put("userName", user.getName());
        additionalInfo.put("userEmail", user.getEmail());
        additionalInfo.put("userCapType", userCapType.name());
        additionalInfo.put("percentage", percentage);
        track(EVENT_USER_RUN_CAP_LIMIT, additionalInfo);
    }

    public void errorPageDisplayed(String location, String exceptionMessage) {
        JsonObject additionalInfo = Json.createObject();
        additionalInfo.put("location", location);
        additionalInfo.put("exception", exceptionMessage);
        track(EVENT_ERROR_PAGE, additionalInfo);
    }

    private void track(String event) {
        track(event, Json.createObject());
    }

    private void track(String event, JsonObject props) {
        if (enabled) {
            getElement().callJsFunction("track", event, props);
        } else {
            log.info("Segment integration is disabled, not sending " + event + " track event");
        }
    }

    private void page() {
        if (enabled) {
            getElement().callJsFunction("page");
        } else {
            log.info("Segment integration is disabled, not sending page visit");
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (enabled) {
            getModel().setSourceKey(sourceKey);
            if (user == null && user != SecurityUtils.getUser()) {
                user = SecurityUtils.getUser();
                getModel().setUser(new SegmentUser(user));
            }
            page();
        } else {
            log.info("Segment integration is disabled, not sending page visit");
        }
    }

    public interface Model extends TemplateModel {
        void setSourceKey(String key);

        void setUser(SegmentUser user);
    }

}
