package io.skymind.pathmind.ui.views.project;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.BasicViewInterface;
import io.skymind.pathmind.ui.views.experiment.RewardFunctionView;
import io.skymind.pathmind.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "fileCheck", layout = MainLayout.class)
public class FileCheckView extends VerticalLayout implements BasicViewInterface, HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ProjectView.class);

	private static final String BACK_TO_IMPORT = "Back To Import >";
	private static final String START_YOUR_PROJECT = "Start Your Project";

	@Autowired
	private ProjectRepository projectRepository;

	private Label sectionTitleLabel = LabelFactory.createLabel("FILE CHECK", CssMindPathStyles.SECTION_TITLE_LABEL);
	private Label projectNameLabel = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);

	private Button actionMenuButton = new Button("Back to Import >", click -> handleActionMenuButtonClicked());

	private ProgressBar statusProgressBar = new ProgressBar();
	private ListBox<String> errorsListBox = new ListBox<>();

	private HorizontalLayout errorsPanel = getErrorsPanel();

	public FileCheckView()
	{
		add(getActionMenu());
		add(getTitlePanel());
		add(getMainContent());

		setWidthFull();
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

	// TODO -> Remove the Test complete button.
	public VerticalLayout getMainContent() {
		return WrapperUtils.wrapCenteredFormVertical(
				getStatusPanel(),
				new Hr(),
				errorsPanel,
				new Button("Test complete", click -> completeFileCheck())
		);
	}

	private void completeFileCheck() {
		statusProgressBar.setValue(1.0);
		errorsPanel.setVisible(false);
		sectionTitleLabel.setText("FILE CHECK COMPLETED!");
		actionMenuButton.setText(START_YOUR_PROJECT);
		actionMenuButton.setId(START_YOUR_PROJECT);
	}

	// TODO -> Some css cleanup due to padding on the ListBox, etc. in terms of component alignment.
	private HorizontalLayout getErrorsPanel() {
		errorsListBox.setWidthFull();
		return WrapperUtils.wrapCenteredFormHorizontal(
				LabelFactory.createLabel("Errors", CssMindPathStyles.CENTERED_FORM_LABEL),
				errorsListBox
		);
	}

	private HorizontalLayout getStatusPanel() {
		return WrapperUtils.wrapCenteredFormHorizontal(
				LabelFactory.createLabel("Status", CssMindPathStyles.CENTERED_FORM_LABEL),
				statusProgressBar
		);
	}

	public VerticalLayout getTitlePanel() {
		return WrapperUtils.wrapCenterAlignmentFullVertical(
				sectionTitleLabel,
				projectNameLabel);
	}

	public ActionMenu getActionMenu() {
		actionMenuButton.setId(BACK_TO_IMPORT);
		return new ActionMenu(
				actionMenuButton);
	}

	private void handleActionMenuButtonClicked() {
		if(BACK_TO_IMPORT.equals(actionMenuButton.getId()))
			UI.getCurrent().navigate(CreateProjectView.class);
		else
			UI.getCurrent().navigate(RewardFunctionView.class);
	}

	@Override
	public void setParameter(BeforeEvent event, Long projectId) {
		projectRepository.findById(projectId)
				.ifPresentOrElse(
						project -> updateScreen(project),
						() -> log.info("TODO -> Implement"));
	}

	// TODO -> Correctly populate the data.
	private void updateScreen(Project project) {
		projectNameLabel.setText("Project: " + project.getName());
		statusProgressBar.setWidthFull();
		statusProgressBar.setValue(0.65);
		errorsListBox.setItems("Error 1", "Error 2");
	}
}
