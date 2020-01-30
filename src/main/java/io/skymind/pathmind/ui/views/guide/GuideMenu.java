package io.skymind.pathmind.ui.views.guide;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.ui.views.model.UploadModelView;

@Tag("guide-menu")
@JsModule("./src/guide/guide-menu.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GuideMenu extends PolymerTemplate<GuideMenu.Model> {

    @Id("skipToUploadModelBtn")
    private Button skipToUploadModelBtn;
    
	@Autowired
	private GuideDAO guideDAO;

    @Autowired
    public GuideMenu() {
    }

    private long projectId;

    @PostConstruct
    private void init() {
        // the state is either "completed" or empty
        ChecklistItem overview = new ChecklistItem("Overview", "guide", stepIsCompleted("Overview"));
        ChecklistItem install = new ChecklistItem("Install Pathmind Helper", "guide/install", stepIsCompleted("Install Pathmind Helper"));
        ChecklistItem observation = new ChecklistItem("Build Observation Space", "guide/observation", stepIsCompleted("Build Observation Space"));
        ChecklistItem actionSpace = new ChecklistItem("Build Action Space", "guide/action-space", stepIsCompleted("Build Action Space"));
        ChecklistItem triggerActions = new ChecklistItem("Triggering Actions", "guide/trigger-actions", stepIsCompleted("Triggering Actions"));
        ChecklistItem done = new ChecklistItem("Define \"Done\" Condition", "guide/done-condition", stepIsCompleted("Define \"Done\" Condition"));
        ChecklistItem reward = new ChecklistItem("Define Reward Variables", "guide/reward", stepIsCompleted("Define Reward Variables"));
        ChecklistItem recap = new ChecklistItem("Conclusion / Re-cap", "guide/recap", stepIsCompleted("Conclusion / Re-cap"));

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
		projectId = 3;

		skipToUploadModelBtn.addClickListener(e -> UI.getCurrent().navigate(UploadModelView.class, projectId));
    }
    
    private String stepIsCompleted(String stepName) {
		GuideStep guideStep = guideDAO.getGuideStep(projectId);

        if (stepName == guideStep.toString()) {
            return "completed";
        } else {
            return "";
        }
    }

    public void setChecklist(List<ChecklistItem> checklist) {
        getModel().setChecklist(checklist);
    }

    public List<ChecklistItem> getChecklist() {
        return getModel().getChecklist();
    }

    public interface Model extends TemplateModel {
        @Include({ "name", "path", "state" })
        void setChecklist(List<ChecklistItem> checklist);

        List<ChecklistItem> getChecklist();
    }
}
