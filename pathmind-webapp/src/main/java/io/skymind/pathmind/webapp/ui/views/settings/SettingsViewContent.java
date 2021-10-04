package io.skymind.pathmind.webapp.ui.views.settings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.shared.constants.BatchMode;
import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.Conda;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.components.CloseableNotification;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.account.AccountUpgradeView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("settings-view-content")
@JsModule("./src/settings/settings-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class SettingsViewContent extends LitTemplate {
    private final PathmindUser user;
    private final ExecutionEnvironment env;
    private final SegmentIntegrator segmentIntegrator;

    @Id("userLogCB")
    private Select<String> userLog;

    @Id("ec2InstanceTypeCB")
    private Select<String> ec2InstanceType;

    @Id("condaVersionCB")
    private Select<String> condaVersion;

    @Id("anylogicVersionCB")
    private Select<String> anylogicVersion;

    @Id("nativerlVersionCB")
    private Select<String> nativerlVersion;

    @Id("helperVersionCB")
    private Select<String> helperVersion;

    @Id("numSampleCB")
    private Select<String> numSample;

    @Id("numWorkerCB")
    private Select<String> numWorker;

    @Id("maxMemoryCB")
    private Select<String> maxMemory;

    @Id("hiddenNodeCB")
    private Select<String> hiddenNode;

    @Id("hiddenLayerCB")
    private Select<String> hiddenLayer;

    @Id("schedulerCB")
    private Select<String> scheduler;

    @Id("freezingCB")
    private Select<String> freezing;

    @Id("rayDebugCB")
    private Select<String> rayDebug;

    @Id("maxTrainingTimeCB")
    private Select<String> maxTrainingTime;

    @Id("longerTrainingCB")
    private Select<String> longerTraining;

    @Id("startCheckIterationCB")
    private Select<String> startCheckIteration;

    @Id("gammaCB")
    private Select<String> gamma;

    @Id("rolloutFragmentLengthCB")
    private Select<String> rolloutFragmentLength;

    @Id("batchModeCB")
    private Select<String> batchMode;

    @Id("batchSizeCB")
    private Select<String> batchSize;

    @Id("rewardBalancePeriodField")
    private IntegerField rewardBalancePeriodField;

    @Id("saveBtn")
    private Button saveBtn;

    @Id("ctaOverlay")
    private VerticalLayout overlay;

    @Id("upgradeBtn")
    private Button upgradeBtn;

    private Boolean isPaidUser = false;
    private Boolean isInternalUser = false;
    private Boolean hideSaveButton = false;
    private Map<Select<String>, String> settingsList = new HashMap<Select<String>, String>();

    @Autowired
    public SettingsViewContent(CurrentUser currentUser, ExecutionEnvironmentManager environmentManager, SegmentIntegrator segmentIntegrator) {
        this(currentUser.getUser(), environmentManager, segmentIntegrator, false);
    }

    public SettingsViewContent(PathmindUser currentUser, ExecutionEnvironmentManager environmentManager, SegmentIntegrator segmentIntegrator, Boolean hideSaveButton) {
        this.user = currentUser;
        this.env = environmentManager.getEnvironment(this.user.getId());
        this.segmentIntegrator = segmentIntegrator;
        this.hideSaveButton = hideSaveButton;
        init();
    }

    private void init() {
        UserRole accountType = user.getAccountType();
        isPaidUser = UserRole.isPaidUser(accountType);
        isInternalUser = UserRole.isInternalUser(accountType);
        getElement().setProperty("isPaidUser", isPaidUser);
        getElement().setProperty("isInternalUser", isInternalUser);
        getElement().setProperty("hideSaveButton", hideSaveButton);
        initSettingsMap();
        initContent();
        initBtns();
    }

    private void initSettingsMap() {
        settingsList.put(hiddenNode, "Number of Hidden Nodes");
        settingsList.put(hiddenLayer, "Number of Hidden Layers");
        settingsList.put(longerTraining, "Enable Longer Training");
        settingsList.put(startCheckIteration, "Early Stopper Start Iteration");
        settingsList.put(userLog, "Enable User Log");
        settingsList.put(ec2InstanceType, "Instance Type");
        settingsList.put(nativerlVersion, "NativeRL Version");
        settingsList.put(anylogicVersion, "AnyLogic Version");
        settingsList.put(condaVersion, "Conda Version");
        settingsList.put(helperVersion, "PM Helper Version");
        settingsList.put(numSample, "Number of PBT samples");
        settingsList.put(numWorker, "Number of Workers per sample");
        settingsList.put(maxMemory, "Max Memory Size in MB");
        settingsList.put(scheduler, "Scheduler");
        settingsList.put(freezing, "Enable Freezing");
        settingsList.put(rayDebug, "Enable Ray Debug");
        settingsList.put(maxTrainingTime, "Max Training Time (hour)");
        settingsList.put(gamma, "Gamma value");
        settingsList.put(rolloutFragmentLength, "Rollout Fragment Length");
        settingsList.put(batchMode, "Train Batch Mode");
        settingsList.put(batchSize, "Train Batch Size");
    }

    private void initBtns() {
        if (!hideSaveButton) {
            saveBtn.addClickListener(e -> {
                saveSettings();
                String text = "Current settings are saved!";
                CloseableNotification notification = new CloseableNotification(text);
                notification.setDuration(-1);
                notification.open();
            });
        }
        if (!isPaidUser && !isInternalUser) {
            upgradeBtn.addClickListener(click -> {
                segmentIntegrator.navigatedToPricingFromNewExpViewSettings();
                getUI().ifPresent(ui -> ui.navigate(AccountUpgradeView.class));
            });
        }
    }

    private void initContent() {
        /* ------------------------ For both paid & internal users ------------------------ */
        // init hidden node
        List<String> hiddenNodes = List.of("64", "128", "256", "512", "1024");
        hiddenNode.setItems(hiddenNodes);
        hiddenNode.setLabel(settingsList.get(hiddenNode));
        hiddenNode.setValue(String.valueOf(env.getHiddenNode()));

        // init hidden layer
        List<String> hiddenLayers = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        hiddenLayer.setItems(hiddenLayers);
        hiddenLayer.setLabel(settingsList.get(hiddenLayer));
        hiddenLayer.setValue(String.valueOf(env.getHiddenLayer()));

        // init longer training
        List<String> longerTrainings = List.of("TRUE", "FALSE");
        longerTraining.setItems(longerTrainings);
        longerTraining.setLabel(settingsList.get(longerTraining));
        longerTraining.setValue(String.valueOf(env.isLongerTraining()).toUpperCase());
        longerTraining.addValueChangeListener(event -> {
            startCheckIteration.setVisible(event.getValue().equals("TRUE"));
        });

        // init start check iteration
        List<String> startCheckIterations = List.of("250", "500", "750", "1000", "1250", "1500", "2000");
        startCheckIteration.setItems(startCheckIterations);
        startCheckIteration.setLabel(settingsList.get(startCheckIteration));
        startCheckIteration.setValue(String.valueOf(env.getStartCheckIterationForLongerTraining()).toUpperCase());
        startCheckIteration.setVisible(longerTraining.getValue().equals("TRUE"));

        if (!isPaidUser && !isInternalUser) {
            hiddenNode.setEnabled(false);
            hiddenLayer.setEnabled(false);
            longerTraining.setEnabled(false);
            startCheckIteration.setEnabled(false);
        }

        /* ------------------------ For internal users only ------------------------ */
        if (!isInternalUser) {
            return;
        }
        // init user log
        List<String> userLogs = List.of("TRUE", "FALSE");
        userLog.setItems(userLogs);
        userLog.setLabel(settingsList.get(userLog));
        userLog.setValue(String.valueOf(env.isUserLog()).toUpperCase());
        
        // init EC2 instance types
        List<String> ec2Instances = Arrays.stream(EC2InstanceType.values())
                .map(EC2InstanceType::toString)
                .collect(Collectors.toList());
        ec2InstanceType.setItems(ec2Instances);
        ec2InstanceType.setLabel(settingsList.get(ec2InstanceType));
        ec2InstanceType.setValue(env.getEc2InstanceType().toString());

        // init NativeRL versions
        List<String> nativerlVersions = NativeRL.activeValues().stream()
                .map(NativeRL::toString)
                .collect(Collectors.toList());
        nativerlVersion.setItems(nativerlVersions);
        nativerlVersion.setLabel(settingsList.get(nativerlVersion));
        nativerlVersion.setValue(env.getNativerlVersion().toString());

        // init AnyLogic versions
        List<String> anyLogicVersions = Arrays.stream(AnyLogic.values())
                .map(AnyLogic::toString)
                .collect(Collectors.toList());
        anylogicVersion.setItems(anyLogicVersions);
        anylogicVersion.setLabel(settingsList.get(anylogicVersion));
        anylogicVersion.setValue(env.getAnylogicVersion().toString());

        // init conda versions
        List<String> condaVersions = Arrays.stream(Conda.values())
                .map(Conda::toString)
                .collect(Collectors.toList());
        condaVersion.setItems(condaVersions);
        condaVersion.setLabel(settingsList.get(condaVersion));
        condaVersion.setValue(env.getCondaVersion().toString());

        // init pathmind helper versions
        List<String> helperVersions = Arrays.stream(PathmindHelper.values())
                .map(PathmindHelper::toString)
                .collect(Collectors.toList());
        helperVersion.setItems(helperVersions);
        helperVersion.setLabel(settingsList.get(helperVersion));
        helperVersion.setValue(env.getPathmindHelperVersion().toString());

        // init number of samples
        List<String> numSamples = List.of("1", "2", "3", "4", "8");
        numSample.setItems(numSamples);
        numSample.setLabel(settingsList.get(numSample));
        numSample.setValue(String.valueOf(env.getPBT_NUM_SAMPLES()));

        // init number of workers
        List<String> numWorkers = List.of("1", "2", "4", "8", "16", "32");
        numWorker.setItems(numWorkers);
        numWorker.setLabel(settingsList.get(numWorker));
        numWorker.setValue(String.valueOf(env.getNumWorker()));

        // init max memory
        List<String> maxMemories = List.of("4096", "16384");
        maxMemory.setItems(maxMemories);
        maxMemory.setLabel(settingsList.get(maxMemory));
        maxMemory.setValue(String.valueOf(env.getMaxMemory()));

        // init scheduler
        List<String> schedulers = List.of("PBT", "PB2");
        scheduler.setItems(schedulers);
        scheduler.setLabel(settingsList.get(scheduler));
        scheduler.setValue(env.getScheduler());

        // init freezing
        List<String> freezings = List.of("TRUE", "FALSE");
        freezing.setItems(freezings);
        freezing.setLabel(settingsList.get(freezing));
        freezing.setValue(String.valueOf(env.isFreezing()).toUpperCase());

        // init ray debug
        List<String> rayDebugs = List.of("TRUE", "FALSE");
        rayDebug.setItems(rayDebugs);
        rayDebug.setLabel(settingsList.get(rayDebug));
        rayDebug.setValue(String.valueOf(env.isRayDebug()).toUpperCase());

        // init max training time
        List<String> maxTrainingTimes = List.of("12", "24", "48");
        maxTrainingTime.setItems(maxTrainingTimes);
        maxTrainingTime.setLabel(settingsList.get(maxTrainingTime));
        maxTrainingTime.setValue(String.valueOf(env.getPBT_MAX_TIME_IN_SEC() / 3600));

        // init gamma value
        List<String> gammas = List.of("0.0", "0.5", "0.9", "0.99");
        gamma.setItems(gammas);
        gamma.setLabel(settingsList.get(gamma));
        gamma.setValue(String.valueOf(env.getGamma()));

        // init rollout fragment length
        List<String> rolloutFragmentLengths = List.of("200", "500", "1000", "2000");
        rolloutFragmentLength.setItems(rolloutFragmentLengths);
        rolloutFragmentLength.setLabel(settingsList.get(rolloutFragmentLength));
        rolloutFragmentLength.setValue(String.valueOf(env.getRolloutFragmentLength()));

        // init train batch mode
        List<String> batchModes = Arrays.stream(BatchMode.values()).map(BatchMode::toString).collect(Collectors.toList());
        batchMode.setItems(batchModes);
        batchMode.setLabel(settingsList.get(batchMode));
        batchMode.setValue(env.getBatchMode().toString());

        // init train batch size
        List<String> batchSizes = List.of("no selection", "3000", "4000", "6000", "8000", "10000", "12000");
        batchSize.setItems(batchSizes);
        batchSize.setLabel(settingsList.get(batchSize));
        batchSize.setValue(env.getTrainBatchSize() == 0 ? "no selection" : String.valueOf(env.getTrainBatchSize()));

        // init reward balance period
        rewardBalancePeriodField.setLabel("Reward Balance Period");
        rewardBalancePeriodField.setValue(env.getRewardBalancePeriod());
    }

    public void saveSettings() {
        if (!isPaidUser && !isInternalUser) {
            return;
        }
        env.setHiddenNode(Integer.parseInt(hiddenNode.getValue()));
        env.setHiddenLayer(Integer.parseInt(hiddenLayer.getValue()));
        env.setLongerTraining(Boolean.valueOf(longerTraining.getValue()));
        env.setStartCheckIterationForLongerTraining(Integer.parseInt(startCheckIteration.getValue()));
        if (!isInternalUser) {
            return;
        }
        env.setUserLog(Boolean.valueOf(userLog.getValue()));
        env.setEc2InstanceType(EC2InstanceType.fromName(ec2InstanceType.getValue()));
        env.setAnylogicVersion(AnyLogic.valueOf(anylogicVersion.getValue()));
        env.setCondaVersion(Conda.valueOf(condaVersion.getValue()));
        env.setNativerlVersion(NativeRL.valueOf(nativerlVersion.getValue()));
        env.setPathmindHelperVersion(PathmindHelper.valueOf(helperVersion.getValue()));
        env.setPBT_NUM_SAMPLES(Integer.parseInt(numSample.getValue()));
        env.setNumWorker(Integer.parseInt(numWorker.getValue()));
        env.setMaxMemory(Integer.parseInt(maxMemory.getValue()));
        env.setScheduler(scheduler.getValue());
        env.setFreezing(Boolean.valueOf(freezing.getValue()));
        env.setRayDebug(Boolean.valueOf(rayDebug.getValue()));
        env.setPBT_MAX_TIME_IN_SEC(Integer.parseInt(maxTrainingTime.getValue()) * 60 * 60);
        env.setGamma(Double.parseDouble(gamma.getValue()));
        env.setRolloutFragmentLength(Integer.parseInt(rolloutFragmentLength.getValue()));
        env.setBatchMode(BatchMode.fromName(batchMode.getValue()));
        env.setTrainBatchSize(batchSize.getValue().equals("no selection") ? 0 : Integer.valueOf(batchSize.getValue()));
    }

    public String getSettingsText() {
        if (!isPaidUser && !isInternalUser) {
            return "";
        }
        return settingsList.entrySet().stream()
                .filter(e -> e.getKey().getValue() != null)
                .map(e -> e.getValue() + ": " + e.getKey().getValue())
                .collect(Collectors.joining(", ")) +
                "Reward Balance Period: " + rewardBalancePeriodField.getValue();
    }
}
