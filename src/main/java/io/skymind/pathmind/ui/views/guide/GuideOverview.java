package io.skymind.pathmind.ui.views.guide;

import io.skymind.pathmind.bus.events.UserUpdateBusEvent;
import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value = Routes.GUIDE_URL, layout = MainLayout.class)
public class GuideOverview extends PathMindDefaultView {

	@Autowired
	private GuideDAO guideDAO;

	private final GuideOverviewContent guideOverviewContent;

	@Autowired
	public GuideOverview(GuideOverviewContent guideOverviewContent) {
		this.guideOverviewContent = guideOverviewContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("PATHMIND GUIDE", "Overview");
	}

	@Override
	protected Component getMainContent() {

		// A couple of ideas depending on what we decide. The first one you have to keep track of everything and it may be better initially since every page is static. However
		// if we move to a dynamic option the second option may work better. That being said if you try to go past the parameters you just go back to the same page where a real solution
		// would handle this better. More information in the comments of each class.

		// Also for now this is all faked in memory only and will reset each time the server is reloaded. It is NOT threadsafe, etc. There is no database connection. It's just
		// temporary stubs.

		// Option 1 ->
		guideDAO.getGuideStep(SecurityUtils.getUserId());
		guideDAO.updateGuideStep(SecurityUtils.getUserId(), GuideStep.DefineDoneCondition);

		// Option 2
		GuideStep guideStep = guideDAO.getGuideStep(SecurityUtils.getUserId());
		guideDAO.updateGuideStep(SecurityUtils.getUserId(), guideStep.nextStep());
		guideDAO.updateGuideStep(SecurityUtils.getUserId(), guideStep.previousStep());

		return guideOverviewContent;
	}
}
