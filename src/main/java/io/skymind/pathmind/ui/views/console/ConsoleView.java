package io.skymind.pathmind.ui.views.console;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.services.ConsoleService;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.components.PathmindTextArea;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.project.components.panels.ExperimentGrid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./styles/styles.css")
@Route(value = Routes.CONSOLE_URL, layout = MainLayout.class)
@Slf4j
public class ConsoleView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private UserDAO userDAO;

	private PathmindTextArea consoleTextArea;
	private ExperimentGrid experimentListPanel;

	private long experimentId;

	public ConsoleView()
	{
		super();
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	protected Component getTitlePanel() {
		return WrapperUtils.wrapWidthFullCenterHorizontal(LabelFactory.createLabel("Console Ouput", CssMindPathStyles.PROJECT_TITLE));
	}

	protected Component getMainContent()
	{
		consoleTextArea = new PathmindTextArea();
		consoleTextArea.setSizeFull();
		experimentListPanel = new ExperimentGrid();

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				consoleTextArea,
				experimentListPanel,
				30);
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToExperiment(experimentId);
	}

	protected void initScreen(BeforeEnterEvent event) {
		// TODO -> Need to load experiments for project due to new changes in the data model.
//		project = projectDAO.getProjectForExperiment(experimentId);
		consoleTextArea.setValue(ConsoleService.getConsoleLogForRun(experimentId));
		// TODO => Update the experiment list panel. This is probably no longer on the project level...
//		experimentListPanel.update(project);
	}
}
