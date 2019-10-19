package io.skymind.pathmind.ui.views.console;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.services.ConsoleService;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.project.components.panels.ExperimentGrid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

@CssImport("./styles/styles.css")
@Route(value = "console", layout = MainLayout.class)
public class ConsoleView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ConsoleView.class);

	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private UserDAO userDAO;

	private TextArea consoleTextArea;
	private ExperimentGrid experimentListPanel;

	private long experimentId;

	public ConsoleView()
	{
		super();
	}

	@Override
	protected void subscribeToEventBus() {
		// TODO -> Implement
		// consumer.
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	protected Component getTitlePanel() {
		return WrapperUtils.wrapWidthFullCenterHorizontal(LabelFactory.createLabel("Console Ouput", CssMindPathStyles.PROJECT_TITLE));
	}

	protected Component getMainContent()
	{
		consoleTextArea = new TextArea();
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

	protected void updateScreen(BeforeEnterEvent event) {
		// TODO -> Need to load experiments for project due to new changes in the data model.
//		project = projectDAO.getProjectForExperiment(experimentId);
		consoleTextArea.setValue(ConsoleService.getConsoleLogForRun(experimentId));
		// TODO => Update the experiment list panel. This is probably no longer on the project level...
//		experimentListPanel.update(project);
	}
}
