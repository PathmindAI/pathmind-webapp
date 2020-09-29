package io.skymind.pathmind.webapp.ui.views.settings;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.shared.constants.EC2InstanceType;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironment;
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import io.skymind.pathmind.shared.services.training.versions.AnyLogic;
import io.skymind.pathmind.shared.services.training.versions.Conda;
import io.skymind.pathmind.shared.services.training.versions.NativeRL;
import io.skymind.pathmind.shared.services.training.versions.PathmindHelper;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.components.CloseableNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag("settings-view-content")
@JsModule("./src/settings/settings-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class SettingsViewContent extends PolymerTemplate<SettingsViewContent.Model> {
    private final PathmindUser user;
    private final ExecutionEnvironment env;

    @Id("ec2InstanceTypeCB")
    private ComboBox<String> ec2InstanceType;

    @Id("condaVersionCB")
    private ComboBox<String> condaVersion;

    @Id("anylogicVersionCB")
    private ComboBox<String> anylogicVersion;

    @Id("nativerlVersionCB")
    private ComboBox<String> nativerlVersion;

    @Id("helperVersionCB")
    private ComboBox<String> helperVersion;

    @Id("saveBtn")
    private Button saveBtn;

    @Autowired
    public SettingsViewContent(CurrentUser currentUser, ExecutionEnvironmentManager environmentManager) {
        this.user = currentUser.getUser();
        this.env = environmentManager.getEnvironment(this.user.getId());
    }

    @PostConstruct
    private void init() {
        initContent();
        initBtns();
    }

    private void initBtns() {
        saveBtn.addClickListener(e -> {
            env.setEc2InstanceType(EC2InstanceType.fromName(ec2InstanceType.getValue()));
            env.setAnylogicVersion(AnyLogic.valueOf(anylogicVersion.getValue()));
            env.setCondaVersion(Conda.valueOf(condaVersion.getValue()));
            env.setNativerlVersion(NativeRL.valueOf(nativerlVersion.getValue()));
            env.setPathmindHelperVersion(PathmindHelper.valueOf(helperVersion.getValue()));

            String text = "Current settings are saved!";
            CloseableNotification notification = new CloseableNotification(text);
            notification.setDuration(-1);
            notification.open();
        });

    }

    private void initContent() {
        // init EC2 instance types
        List<String> ec2Instances = Arrays.stream(EC2InstanceType.values())
                .map(EC2InstanceType::toString)
                .collect(Collectors.toList());
        ec2InstanceType.setItems(ec2Instances);
        ec2InstanceType.setLabel("Instance Type");
        ec2InstanceType.setPlaceholder(env.getEc2InstanceType().toString());
        ec2InstanceType.setValue(env.getEc2InstanceType().toString());

        // init NativeRL versions
        List<String> nativerlVersions = NativeRL.activeValues().stream()
            .map(NativeRL::toString)
            .collect(Collectors.toList());

        nativerlVersion.setItems(nativerlVersions);
        nativerlVersion.setLabel("NativeRL Version");
        nativerlVersion.setPlaceholder(env.getNativerlVersion().toString());
        nativerlVersion.setValue(env.getNativerlVersion().toString());

        // init AnyLogic versions
        List<String> anyLogicVersions = Arrays.stream(AnyLogic.values())
                .map(AnyLogic::toString)
                .collect(Collectors.toList());

        anylogicVersion.setItems(anyLogicVersions);
        anylogicVersion.setLabel("AnyLogic Version");
        anylogicVersion.setPlaceholder(env.getAnylogicVersion().toString());
        anylogicVersion.setValue(env.getAnylogicVersion().toString());

        // init conda versions
        List<String> condaVersions = Arrays.stream(Conda.values())
            .map(Conda::toString)
            .collect(Collectors.toList());

        condaVersion.setItems(condaVersions);
        condaVersion.setLabel("Conda Version");
        condaVersion.setPlaceholder(env.getCondaVersion().toString());
        condaVersion.setValue(env.getCondaVersion().toString());

        // init pathmind helper versions
        List<String> helperVersions = Arrays.stream(PathmindHelper.values())
            .map(PathmindHelper::toString)
            .collect(Collectors.toList());

        helperVersion.setItems(helperVersions);
        helperVersion.setLabel("PM helper Version");
        helperVersion.setPlaceholder(env.getPathmindHelperVersion().toString());
        helperVersion.setValue(env.getPathmindHelperVersion().toString());
    }

    public interface Model extends TemplateModel {
//        void setEc2InstanceType(EC2InstanceType ec2InstanceType);
//        void setEc2InstanceType(String ec2InstanceType);
    }
}
