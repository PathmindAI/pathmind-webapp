package io.skymind.pathmind.ui.views.project.components;

import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextArea;
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.utils.UIConstants;
import io.skymind.pathmind.utils.WrapperUtils;

public class FileCheckPanel extends VerticalLayout
{
	private static final String FILE_CHECK = "FILE CHECK";
	private static final String FILE_CHECK_COMPLETED = "FILE CHECK COMPLETED!";

	private ProgressBar statusProgressBar = new ProgressBar();

	private HorizontalLayout errorsPanel;
	private TextArea errorsTextArea;

	private Label sectionTitleLabel = LabelFactory.createLabel(FILE_CHECK, CssMindPathStyles.SECTION_TITLE_LABEL);
	private Label projectNameLabel = LabelFactory.createLabel("", CssMindPathStyles.SECTION_SUBTITLE_LABEL);

	public FileCheckPanel()
	{
		add(
				getTitlePanel(),
				getStatusPanel(),
				new Hr(),
				getErrorsPanel());

		setWidth(UIConstants.CENTERED_FORM_WIDTH);
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
	}

	public VerticalLayout getTitlePanel() {
		return WrapperUtils.wrapCenteredFormVertical(
				sectionTitleLabel,
				projectNameLabel);
	}

	public void setProjectName(String projectName) {
		projectNameLabel.setText(projectName);
	}

	private HorizontalLayout getStatusPanel() {
		return WrapperUtils.wrapCenteredFormHorizontal(
				LabelFactory.createLabel("Status", CssMindPathStyles.CENTERED_FORM_LABEL),
				statusProgressBar
		);
	}

	private HorizontalLayout getErrorsPanel()
	{
		errorsTextArea = new TextArea();
		errorsTextArea.setReadOnly(true);
		errorsTextArea.setMaxHeight(UIConstants.SMALL_COMPONENT_HEIGHT);
		errorsTextArea.setWidthFull();

		errorsPanel = WrapperUtils.wrapCenteredFormHorizontal(
				LabelFactory.createLabel("Errors", CssMindPathStyles.CENTERED_FORM_LABEL),
				errorsTextArea
		);

		errorsPanel.setVisible(false);

		return errorsPanel;
	}

	public void reset() {
		errorsTextArea.clear();
		errorsPanel.setVisible(false);
		sectionTitleLabel.setText(FILE_CHECK);
		statusProgressBar.setValue(0);
	}

	public void updateProgressBar(double percentage) {
		statusProgressBar.setValue(percentage);
	}

	public void addError(String error) {
		errorsPanel.setVisible(true);
		errorsTextArea.setValue(error + "\n" + errorsTextArea.getValue());
	}

	public void done() {
		statusProgressBar.setValue(1.0);
		sectionTitleLabel.setText(FILE_CHECK_COMPLETED);
	}
}
