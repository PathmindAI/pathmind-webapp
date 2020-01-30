package io.skymind.pathmind.ui.views.guide;

import javax.annotation.PostConstruct;

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
import io.skymind.pathmind.security.CurrentUser;

@Tag("reward-view-content")
@JsModule("./src/guide/reward-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RewardViewContent extends PolymerTemplate<RewardViewContent.Model> {

	@Id("backBtn")
	private Button backBtn;

	@Id("nextBtn")
	private Button nextBtn;

	@Autowired
	private GuideDAO guideDAO;

	@Autowired
	public RewardViewContent(CurrentUser currentUser) {
	}

	@PostConstruct
	private void init() {
		initBtns();
	}

	private void initBtns() {
		// Fake project
		long projectId = 3;

		GuideStep guideStep = guideDAO.getGuideStep(projectId);

		backBtn.addClickListener(e -> {
			guideDAO.updateGuideStep(projectId, guideStep.previousStep());
			UI.getCurrent().navigate(DoneConditionView.class);
		});

		nextBtn.addClickListener(e -> {
			guideDAO.updateGuideStep(projectId, guideStep.nextStep());
			UI.getCurrent().navigate(RecapView.class);
		});
    }

	public interface Model extends TemplateModel {
	}
}
