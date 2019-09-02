package io.skymind.pathmind.ui.views.console;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.services.ConsoleService;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.project.ProjectView;
import io.skymind.pathmind.ui.views.project.components.ExperimentListPanel;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "console", layout = MainLayout.class)
public class ConsoleView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ConsoleView.class);

	@Autowired
	private ExperimentRepository experimentRepository;
	@Autowired
	private ProjectRepository projectRepository;

	private TextArea consoleTextArea;
	private ExperimentListPanel experimentListPanel;

	private long projectId;
	private long experimentId;

	public ConsoleView()
	{
		super();
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
			new Button("< Back", click ->
					UI.getCurrent().navigate(ProjectView.class, projectId))
		);
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	protected Component getTitlePanel() {
		return WrapperUtils.wrapFullWidthHorizontal(LabelFactory.createLabel("Console Ouput", CssMindPathStyles.PROJECT_TITLE));
	}

	protected Component getMainContent()
	{
		consoleTextArea = new TextArea();
		consoleTextArea.setSizeFull();
		experimentListPanel = new ExperimentListPanel();

		return WrapperUtils.wrapCenterAlignmentFullSplitLayoutVertical(
				consoleTextArea,
				experimentListPanel,
				30);
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	protected void updateScreen(BeforeEnterEvent event) {
		projectId = projectRepository.getProjectForExperiment(experimentId).getId();
		consoleTextArea.setValue(ConsoleService.getConsoleLogForExperiment(experimentId));
		experimentListPanel.setExperiments(experimentRepository.getOtherExperimentsForSameProject(experimentId));
	}
}
