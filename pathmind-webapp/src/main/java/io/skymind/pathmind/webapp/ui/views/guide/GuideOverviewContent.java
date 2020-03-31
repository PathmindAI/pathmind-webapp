package io.skymind.pathmind.webapp.ui.views.guide;

import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultPageContent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.shared.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.model.utils.UploadModelViewNavigationUtils;

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
			if (guideStep == GuideStep.Overview) {
				guideDAO.updateGuideStep(projectId, guideStep.nextStep());
				segmentIntegrator.completedGuideOverview();
			}
			UI.getCurrent().navigate(InstallPathmindHelperView.class, projectId);
		});
		skipToUploadModelBtn.addClickListener(e -> {
			segmentIntegrator.skippedGuideToUploadModel();
			UI.getCurrent().navigate(UploadModelView.class, UploadModelViewNavigationUtils.getUploadModelParameters(projectId));
		});
	}

	public interface Model extends TemplateModel {
	}
}
