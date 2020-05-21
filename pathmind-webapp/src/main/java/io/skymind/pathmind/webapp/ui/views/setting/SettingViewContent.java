package io.skymind.pathmind.webapp.ui.views.setting;

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
import io.skymind.pathmind.shared.services.training.environment.ExecutionEnvironmentManager;
import io.skymind.pathmind.webapp.security.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@Tag("setting-view-content")
@JsModule("./src/setting/setting-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class SettingViewContent extends PolymerTemplate<SettingViewContent.Model> {
    private final PathmindUser user;
    private final ExecutionEnvironmentManager environmentManager;

    @Id("ec2InstanceTypeCB")
    private ComboBox<String> ec2InstanceType;

    @Id("saveBtn")
    private Button saveBtn;

    @Autowired
    public SettingViewContent(CurrentUser currentUser, ExecutionEnvironmentManager environmentManager) {
        this.user = currentUser.getUser();
        this.environmentManager = environmentManager;
    }

    @PostConstruct
    private void init() {
        initContent();
        initBtns();
    }

    private void initBtns() {
        saveBtn.addClickListener(e -> {
            // need pop up message
            environmentManager.getEnvironment().setEc2InstanceType(EC2InstanceType.fromName(ec2InstanceType.getValue()));
            log.info("kepricondebug selected : " + ec2InstanceType.getValue());
        });

    }

    private void initContent() {
        ec2InstanceType.setItems(EC2InstanceType.IT_16CPU_32GB.toString(), EC2InstanceType.IT_36CPU_72GB.toString());
        ec2InstanceType.setLabel("Instance Type");
        ec2InstanceType.setPlaceholder(environmentManager.getEnvironment().getEc2InstanceType().toString());
    }

    public interface Model extends TemplateModel {
//        void setEc2InstanceType(EC2InstanceType ec2InstanceType);
//        void setEc2InstanceType(String ec2InstanceType);
    }
}
