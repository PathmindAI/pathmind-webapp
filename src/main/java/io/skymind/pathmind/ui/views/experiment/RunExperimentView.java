package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.Reward;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentChartPanel;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentRecentPanel;
import io.skymind.pathmind.ui.views.experiment.components.ExperimentScoreboardPanel;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "runExperiment", layout = MainLayout.class)
public class RunExperimentView extends VerticalLayout implements BasicViewInterface, HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(RunExperimentView.class);

	// TODO -> Implement correctly based on parameters passed into the view.
	private ExperimentRepository experimentRepository;

	private Label projectLabel = LabelFactory.createLabel("", CssMindPathStyles.PROJECT_TITLE);
	private ListBox<Reward> rewardsListBox = new ListBox<>();

	public RunExperimentView(@Autowired ExperimentRepository experimentRepository) {
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
			new Button("Stop")
		);
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	public Component getTitlePanel() {
		return projectLabel;
	}

	// TODO -> Since I'm not sure exactly what the panels on the right are I'm going to make some big
	// assumptions as to which Layout should wrap which one.
	@Override
	public Component getMainContent() {
		return WrapperUtils.wrapCenterAlignmentFullVertical(
				WrapperUtils.wrapCenterAlignmentFullWidthHorizontal(
					new ExperimentChartPanel(),
					new ExperimentScoreboardPanel()),
				new ExperimentRecentPanel(experimentRepository.findExperimentsByProjectId(1)) // TODO -> Hardcoded
		);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		updateScreen();
	}

	// TODO -> Fake data
	private void updateScreen() {
		projectLabel.setText("Coffee Shop");
	}
}
