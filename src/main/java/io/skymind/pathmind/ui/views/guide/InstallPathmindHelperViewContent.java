package io.skymind.pathmind.ui.views.guide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;

@Tag("install-pathmind-helper-view-content")
@JsModule("./src/guide/install-pathmind-helper-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InstallPathmindHelperViewContent extends PolymerTemplate<InstallPathmindHelperViewContent.Model> {

    @Id("backBtn")
    private Button backBtn;

    @Id("nextBtn")
    private Button nextBtn;

	@Autowired
	private GuideDAO guideDAO;

    @Autowired
    public InstallPathmindHelperViewContent() {
    }

	protected void initBtns(GuideStep guideStep, long projectId) {
		backBtn.addClickListener(e -> {
			guideDAO.updateGuideStep(projectId, guideStep.previousStep());
			UI.getCurrent().navigate(GuideOverview.class, projectId);
		});

		nextBtn.addClickListener(e -> {
			guideDAO.updateGuideStep(projectId, guideStep.nextStep());
			UI.getCurrent().navigate(ObservationView.class, projectId);
		});
    }

	public interface Model extends TemplateModel {
	}
}
