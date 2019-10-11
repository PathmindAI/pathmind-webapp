package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;

// TODO -> CSS -> Move all style to CSS.
public class PathminderHelperWizardPanel extends VerticalLayout
{
	private Label projectNameLabel = new Label();
	private Button nextStepButton = new Button("Next", new Icon(VaadinIcon.CHEVRON_RIGHT));

	public PathminderHelperWizardPanel()
	{
		projectNameLabel.getStyle().set("margin-top", "0px");
		nextStepButton.setIconAfterText(true);

		add(getProjectH3(),
				projectNameLabel,
				GuiUtils.getFullWidthHr(),
				getInstructionsDiv(),
				WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton));

		setClassName("view-section"); // adds the white 'panel' style with rounded corners

		setWidthFull();
	}

	private H3 getProjectH3() {
		H3 h3 = new H3("Project");
		h3.getStyle().set("margin-bottom", "0px");
		return h3;
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		nextStepButton.addClickListener(listener);
	}

	public void setProjectName(String projectName) {
		projectNameLabel.setText(projectName);
	}

	// TODO -> CSS -> Move CSS to styles.css
	private Div getInstructionsDiv() {
		Div div = new Div();
		div.setWidthFull();
		div.getElement().setProperty("innerHTML",
				"<p>To prepare your AnyLogic model for reinforcement learning, install the Pathmind Helper</p>" +
				"<p><strong>The basics:</strong></p>" +
				"<ol>" +
					"<li>The Pathmind Helper is an AnyLogic palette item that you add to your simulation. You can <a href=\"https://help.pathmind.com/en/articles/3354371-using-the-pathmind-helper/\" target=\"_blank\">download it here</a>.</li>" +
					"<li>Add Pathmind Helper as a library in AnyLogic.</li>" +
					"<li>Add a Pathmind Helper to your model.</li>" +
					"<li>Fill in these functions:</li>" +
						"<ul>" +
							"<li>Observation for rewards</li>" +
							"<li>Observation for training</li>"+
							"<li>doAction</li>" +
						"</ul>" +
				"</ol>" +
				"<p>When you're ready, upload your model in the next step.</p>" +
				"<p><a href=\"https://help.pathmind.com/en/articles/3354371-using-the-pathmind-helper\" target=\"_blank\">For more details, see our documentation</a></p>");
		return div;
	}
}
