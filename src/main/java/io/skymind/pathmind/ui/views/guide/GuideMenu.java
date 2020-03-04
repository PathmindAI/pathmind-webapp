package io.skymind.pathmind.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.views.model.UploadModelView;

@CssImport("./styles/components/guide-menu.css")
public class GuideMenu extends VerticalLayout {

    private long projectId;
    
    private GuideStep guideStep;
    
	@Autowired
	protected SegmentIntegrator segmentIntegrator;
    
    public GuideMenu(GuideStep guideStep, long projectId, SegmentIntegrator segmentIntegrator) {
		super();
        addClassName("guide-menu");
        setSpacing(false);

        this.projectId = projectId;
        this.guideStep = guideStep;
        this.segmentIntegrator = segmentIntegrator;

        Div guideLinksWrapper = new Div();
        guideLinksWrapper.addClassName("guide-links-wrapper");
        
        guideLinksWrapper.add(createChecklistItem("Overview", GuideOverview.class, 0));
        guideLinksWrapper.add(createChecklistItem("Install Pathmind Helper", InstallPathmindHelperView.class, 1));
        guideLinksWrapper.add(createChecklistItem("Build Observation Space", ObservationView.class, 2));
        guideLinksWrapper.add(createChecklistItem("Build Action Space", ActionSpaceView.class, 3));
        guideLinksWrapper.add(createChecklistItem("Triggering Actions", TriggerActionsView.class, 4));
        guideLinksWrapper.add(createChecklistItem("Define \"Done\" Condition", DoneConditionView.class, 5));
        guideLinksWrapper.add(createChecklistItem("Define Reward Variables", RewardView.class, 6));
        guideLinksWrapper.add(createChecklistItem("Conclusion / Re-cap", RecapView.class, 7));

        add(guideLinksWrapper);
        add(createSkipToUploadModelButton());
    }

    private Component createChecklistItem(String itemName, Class navigationTarget, long itemIndex) {
        long currentStep = itemIndex+1;
        long totalSteps = guideStep.Recap.getValue()+1;
        Span checklistStep = new Span("(Step "+currentStep+" of "+totalSteps+")");
        Div checklistItemWrapper = new Div();
        RouterLink checklistItem = new RouterLink(itemName, navigationTarget, projectId);

        if (itemIndex < guideStep.getValue()) {
            checklistItemWrapper.addClassName("completed");
        } else if (itemIndex > guideStep.getValue()) {
            checklistItemWrapper.addClassName("disabled");
        }

        checklistItemWrapper.add(checklistItem);
        checklistItemWrapper.add(checklistStep);

        return checklistItemWrapper;
    }

    private Button createSkipToUploadModelButton() {
        Button skipToUploadModelButton = new Button("Skip to Upload Model");
        skipToUploadModelButton.addThemeName("tertiary-inline");
        skipToUploadModelButton.addClickListener(e -> {
            segmentIntegrator.skippedGuideToUploadModel();
            UI.getCurrent().navigate(UploadModelView.class, projectId);
        });
        return skipToUploadModelButton;
    }
}
