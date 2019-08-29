package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.PathmindConstants;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.ui.views.project.ProjectView;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "rewardFunction", layout = MainLayout.class)
public class RewardFunctionView extends VerticalLayout implements BasicViewInterface, HasUrlParameter<Integer>
{
	private Logger log = LogManager.getLogger(RewardFunctionView.class);

	private Label projectLabel = LabelFactory.createLabel("", CssMindPathStyles.PROJECT_TITLE);

	private TextArea rewardsFunctionTextArea = new TextArea();

	// TODO I assume we don't need this here and that the project, etc. are all retrieved from the Experiment
	// or something along those lines but since I haven't yet setup the fake database schema for experiment
	// since I don't fully understand the hierarchy I'm just going to pull the project name directly to
	// confirm that the parameter is correctly wired up.
	@Autowired
	private ProjectRepository projectRepository;

	public RewardFunctionView() {
		add(getActionMenu());
		add(getMainContent());

		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	// TODO -> Implement the actual navigation to the new screen. For now it's a hardcoded value.
	@Override
	public ActionMenu getActionMenu() {
		return new ActionMenu(
			new Button("+ New Experiment"),
			new Button("Test Run >", click ->
					UI.getCurrent().navigate(ProjectView.class, PathmindConstants.TODO_PARAMETER))
		);
	}

	// I do NOT want to implement a default interface because this is to remind me
	// what to implement and a default would remove that ability.
	@Override
	public Component getTitlePanel() {
		return null;
	}

	// TODO -> Since I'm not sure exactly what the panels on the right are I'm going to make some big
	// assumptions as to which Layout should wrap which one.
	@Override
	public Component getMainContent() {
		return WrapperUtils.wrapCenterAlignmentFullHorizontal(
				getMainPanel(),
				getRightPanel());
	}

	private VerticalLayout getMainPanel() {
		VerticalLayout mainVerticalLayout = new VerticalLayout(
				projectLabel,
				getBasicOptionsPanel(),
				getRewardsPanel());
		return mainVerticalLayout;
	}

	private VerticalLayout getBasicOptionsPanel() {
		return new VerticalLayout(
			new H3("Basic Options"),
			getBasicOptionsForm(),
			new H3("Reward Functions")
		);
	}

	// TODO -> Add rewardsListBox renderer
	private Component getRewardsPanel() {
		return rewardsFunctionTextArea;
	}

	private Component getBasicOptionsForm() {
		return new HorizontalLayout(
				new TextField("Observation Count"),
				new TextField("Possible Actions Count"),
				new TextField("Simulation Step Count")
		);
	}

	private VerticalLayout getRightPanel() {
		VerticalLayout rightVerticalLayout = new VerticalLayout(
				new Label("Errors"),
				new Hr(),
				new Label("Something else"),
				new Hr(),
				new Label("Something else"),
				new Hr(),
				new Label("Something else"));
		rightVerticalLayout.setHeightFull();
		rightVerticalLayout.setWidth("300px");
		return rightVerticalLayout;
	}

	// TODO -> Duplicate code for now as I refactor the code on my new understanding from the call yesterday.
	@Override
	public void setParameter(BeforeEvent event, Integer projectId) {
		Project project = projectRepository.getProject(projectId);
		if(project == null) {
			log.info("TODO -> Implement");
			return;
		} else {
			updateScreen(project);
		}
	}

	// TODO -> Fake data
	private void updateScreen(Project project) {
		rewardsFunctionTextArea.setValue(
				"reward = formulate\n" +
				"reward += more math");
		projectLabel.setText(project.getName());

	}
}
