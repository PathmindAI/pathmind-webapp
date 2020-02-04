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
import io.skymind.pathmind.ui.views.model.UploadModelView;

@Tag("guide-overview-content")
@JsModule("./src/guide/guide-overview-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GuideOverviewContent extends DefaultPageContent<GuideOverviewContent.Model> {
	@Id("nextBtn")
	private Button nextBtn;

	@Id("skipToUploadModelBtn")
	private Button skipToUploadModelBtn;

	public GuideOverviewContent() {
	}

	@Override
	protected void initBtns(GuideDAO guideDAO, GuideStep guideStep, long projectId, SegmentIntegrator segmentIntegrator) {
		nextBtn.addClickListener(e -> {
			if (guideStep.getId() == 0) {
				guideDAO.updateGuideStep(projectId, guideStep.nextStep());
				segmentIntegrator.completedGuideOverview();
			}
			UI.getCurrent().navigate(InstallPathmindHelperView.class, projectId);
		});
		skipToUploadModelBtn.addClickListener(e -> UI.getCurrent().navigate(UploadModelView.class, projectId));
	}

	public interface Model extends TemplateModel {
	}
}
