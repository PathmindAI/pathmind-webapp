package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.ConsoleEntry;
import io.skymind.pathmind.data.Reward;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.ui.views.experiment.components.ConsoleGrid;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentChartPanel;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentRecentPanel;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentScoreboardPanel;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "console", layout = MainLayout.class)
public class ConsoleView extends VerticalLayout implements BasicViewInterface, HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ConsoleView.class);

	// TODO -> Only used to populate the screen with test data for now.
	private ExperimentRepository experimentRepository;

	private ConsoleGrid consoleGrid = new ConsoleGrid();

	public ConsoleView(@Autowired ExperimentRepository experimentRepository) {
		this.experimentRepository = experimentRepository;

		add(getActionMenu());
		add(getTitlePanel());
		add(getMainContent());

		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	@Override
	public ActionMenu getActionMenu() {
		return new ActionMenu(
			new Button("< Back")
		);
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	public Component getTitlePanel() {
		return WrapperUtils.wrapCenterAlignmentFullHorizontal(LabelFactory.createLabel("Console Ouput", CssMindPathStyles.PROJECT_TITLE));
	}

	// TODO -> Hardcoded data.
	public Component getMainContent()
	{
		SplitLayout splitLayout = new SplitLayout(
				WrapperUtils.wrapCenterAlignmentFullVertical(consoleGrid),
				WrapperUtils.wrapFullWidthHorizontal(new ExperimentRecentPanel(experimentRepository.findExperimentsByProjectId(1))));

		splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
		splitLayout.setSizeFull();
		splitLayout.setSplitterPosition(50);

		return splitLayout;
	}

	// TODO -> Parameter Validation
	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		updateScreen();
	}

	// TODO -> Implement
	private void updateScreen() {
		consoleGrid.setItems(ConsoleEntry.getFakeData(10));
	}
}
