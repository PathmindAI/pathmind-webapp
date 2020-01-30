package io.skymind.pathmind.ui.views.guide;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import io.skymind.pathmind.ui.views.model.UploadModelView;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("guide-menu")
@JsModule("./src/guide/guide-menu.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GuideMenu extends PolymerTemplate<GuideMenu.Model> {

    @Id("skipToUploadModelBtn")
    private Button skipToUploadModelBtn;

    @Autowired
    public GuideMenu() {
    }

    @PostConstruct
    private void init() {
        // the state is either "completed" or empty
        ChecklistItem overview = new ChecklistItem("Overview", "guide", "completed", "overview");
        ChecklistItem install = new ChecklistItem("Install Pathmind Helper", "guide/install", "completed", "install");
        ChecklistItem observation = new ChecklistItem("Build Observation Space", "guide/observation", "completed", "observation");
        ChecklistItem actionSpace = new ChecklistItem("Build Action Space", "guide/action-space", "", "action-space");
        ChecklistItem triggerActions = new ChecklistItem("Triggering Actions", "guide/trigger-actions", "", "trigger-actions");
        ChecklistItem done = new ChecklistItem("Define \"Done\" Condition", "guide/done-condition", "", "done-condition");
        ChecklistItem reward = new ChecklistItem("Define Reward Variables", "guide/reward", "", "reward");
        ChecklistItem recap = new ChecklistItem("Conclusion / Re-cap", "guide/recap", "", "recap");

        List<ChecklistItem> checklist = new ArrayList<ChecklistItem>();
        checklist.add(overview);
        checklist.add(install);
        checklist.add(observation);
        checklist.add(actionSpace);
        checklist.add(triggerActions);
        checklist.add(done);
        checklist.add(reward);
        checklist.add(recap);

        setChecklist(checklist);

        initBtn();
    }

    private void initBtn() {
		// Fake project
		long projectId = 3;

		skipToUploadModelBtn.addClickListener(e -> UI.getCurrent().navigate(UploadModelView.class, projectId));
	}

    public void setChecklist(List<ChecklistItem> checklist) {
        getModel().setChecklist(checklist);
    }

    public List<ChecklistItem> getChecklist() {
        return getModel().getChecklist();
    }

    public interface Model extends TemplateModel {
        @Include({ "name", "path", "state", "htmlId" })
        void setChecklist(List<ChecklistItem> checklist);

        List<ChecklistItem> getChecklist();
    }
}
