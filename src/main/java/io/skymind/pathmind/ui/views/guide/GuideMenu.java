package io.skymind.pathmind.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
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

        this.projectId = projectId;
        this.guideStep = guideStep;
        this.segmentIntegrator = segmentIntegrator;
        
        add(createChecklistItem("Overview", GuideOverview.class, 0));
        add(createChecklistItem("Install Pathmind Helper", InstallPathmindHelperView.class, 1));
        add(createChecklistItem("Build Observation Space", ObservationView.class, 2));
        add(createChecklistItem("Build Action Space", ActionSpaceView.class, 3));
        add(createChecklistItem("Triggering Actions", TriggerActionsView.class, 4));
        add(createChecklistItem("Define \"Done\" Condition", DoneConditionView.class, 5));
        add(createChecklistItem("Define Reward Variables", RewardView.class, 6));
        add(createChecklistItem("Conclusion / Re-cap", RecapView.class, 7));

        add(createSkipToUploadModelButton());
    }

    private Component createChecklistItem(String itemName, Class navigationTarget, long itemIndex) {
        RouterLink checklistItem = new RouterLink(itemName, navigationTarget, projectId);

        if (itemIndex < guideStep.getValue()) {
            checklistItem.addClassName("completed");
        } else if (itemIndex > guideStep.getValue()) {
            checklistItem.addClassName("disabled");
        }

        return checklistItem;
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
