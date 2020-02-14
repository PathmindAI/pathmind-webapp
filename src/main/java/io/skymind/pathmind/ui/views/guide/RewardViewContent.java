package io.skymind.pathmind.ui.views.guide;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.views.guide.template.DefaultPageContent;

@Tag("reward-view-content")
@JsModule("./src/guide/reward-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RewardViewContent extends DefaultPageContent<RewardViewContent.Model> {
	@Id("backBtn")
	private Button backBtn;

	@Id("nextBtn")
	private Button nextBtn;

	public RewardViewContent() {
	}

	@Override
	protected void initBtns(GuideDAO guideDAO, GuideStep guideStep, long projectId, SegmentIntegrator segmentIntegrator) {
		backBtn.addClickListener(e -> {
			UI.getCurrent().navigate(DoneConditionView.class, projectId);
		});

		nextBtn.addClickListener(e -> {
			if (guideStep == GuideStep.DefineRewardVariable) {
				guideDAO.updateGuideStep(projectId, guideStep.nextStep());
				segmentIntegrator.completedGuideReward();
			}
			UI.getCurrent().navigate(RecapView.class, projectId);
		});
    }

	public interface Model extends TemplateModel {
	}
}
