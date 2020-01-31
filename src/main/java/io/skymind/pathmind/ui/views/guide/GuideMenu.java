package io.skymind.pathmind.ui.views.guide;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.ui.views.model.UploadModelView;

@CssImport("./styles/components/guide-menu.css")
public class GuideMenu extends VerticalLayout {

    private long projectId;
    
	private GuideStep guideStep;
    
    public GuideMenu(GuideStep guideStep, long projectId) {
		super();
        addClassName("guide-menu");

        this.projectId = projectId;
        this.guideStep = guideStep;
        
        add(createChecklistItem("Overview", GuideOverview.class, 0));
        add(createChecklistItem("Install Pathmind Helper", InstallPathmindHelperView.class, 1));
        add(createChecklistItem("Build Observation Space", ObservationView.class, 2));
        add(createChecklistItem("Build Action Space", ActionSpaceView.class, 3));
        add(createChecklistItem("Triggering Actions", TriggerActionsView.class, 4));
        add(createChecklistItem("Define \"Done\" Condition", DoneConditionView.class, 5));
        add(createChecklistItem("Define Reward Variables", RewardView.class, 6));
        add(createChecklistItem("Conclusion / Re-cap", RecapView.class, 7));

        add(createSkipToUploadModelLink());
    }

    private Component createChecklistItem(String itemName, Class navigationTarget, long itemIndex) {
        RouterLink checklistItem = new RouterLink(itemName, navigationTarget, projectId);

        if (itemIndex < guideStep.getId()) {
            checklistItem.addClassName("completed");
        } else if (itemIndex > guideStep.getId()) {
            checklistItem.addClassName("disabled");
        }

        return checklistItem;
    }

    private RouterLink createSkipToUploadModelLink() {
        RouterLink skipToUploadModelLink = new RouterLink("Skip to Upload Model", UploadModelView.class, projectId);
        skipToUploadModelLink.addClassName("skipLink");
        return skipToUploadModelLink;
    }
}
