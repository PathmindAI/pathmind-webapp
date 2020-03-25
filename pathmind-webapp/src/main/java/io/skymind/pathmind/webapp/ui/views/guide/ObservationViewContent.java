package io.skymind.pathmind.webapp.ui.views.guide;

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
import io.skymind.pathmind.webapp.ui.views.guide.template.DefaultPageContent;

@Tag("observation-view-content")
@JsModule("./src/guide/observation-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ObservationViewContent extends DefaultPageContent<TemplateModel> {
    @Id("backBtn")
    private Button backBtn;

    @Id("nextBtn")
    private Button nextBtn;

    public ObservationViewContent() {
    }

    @Override
	protected void initBtns(GuideDAO guideDAO, GuideStep guideStep, long projectId, SegmentIntegrator segmentIntegrator) {
        backBtn.addClickListener(e -> {
            UI.getCurrent().navigate(TriggerActionsView.class, projectId);
        });

		nextBtn.addClickListener(e -> {
            if (guideStep == GuideStep.BuildObservationSpace) {
                guideDAO.updateGuideStep(projectId, guideStep.nextStep());
                segmentIntegrator.completedGuideObservation();
            }
            UI.getCurrent().navigate(RewardView.class, projectId);
        });
    }
}
