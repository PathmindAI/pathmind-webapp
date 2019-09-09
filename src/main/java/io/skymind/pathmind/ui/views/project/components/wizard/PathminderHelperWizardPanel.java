package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.ui.utils.GuiUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;

// TODO -> Move all style to CSS.
public class PathminderHelperWizardPanel extends VerticalLayout
{
	private Label projectNameLabel = new Label();
	private Button nextStepButton = new Button("Next Step");

	public PathminderHelperWizardPanel()
	{
		projectNameLabel.getStyle().set("margin-top", "0px");

		add(getProjectH3(),
				projectNameLabel,
				GuiUtils.getFullWidthHr(),
				getInstructionsDiv(),
				WrapperUtils.wrapCenterFullWidthHorizontal(nextStepButton));

		getStyle().set("border", "1px solid #ccc");
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

	// TODO -> Move CSS to styles.css
	private Div getInstructionsDiv() {
		Div div = new Div();
		div.setWidthFull();
		div.getElement().setProperty("innerHTML",
				"<p>To prepare your AnyLogic model for reinforcement learning, install the PathmindHelper</p>" +
				"<ol>" +
					"<li>Download the <a href=\"https://skymind.ai/\">PathmindHelper</a>.</li>" +
					"<li>Add the PathmindHelper as a library in your AnyLogic Model.</li>" +
					"<li>Add one instance of the PathmindHelper to your model</li>" +
					"<li>Fill in these four functions:</li>" +
						"<ul>" +
							"<li>doAction</li>" +
							"<li>getObservation for rewards</li>" +
							"<li>getObservation for training</li>" +
							"<li>isDone</li>" +
						"</ul>" +
				"</ol>" +
				"<p>When you're ready, upload your model in the next step.</p>" +
				"<p style=\"font-size:11px;\"><a href=\"https://skymind.ai\">For more details, see our documentation</a></p>");
		return div;
	}
}
