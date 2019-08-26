package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.utils.WrapperUtils;


public class ExperimentChartPanel extends VerticalLayout
{
	// TODO -> I have no idea what these components should be or what they do.
	private Label inputLabel = new Label("Input");
	private Label arrowsLabel = new Label(" > < ");
	private Label outputLabel = new Label("Output");

	public ExperimentChartPanel() {
		add(WrapperUtils.wrapCenteredFormHorizontal(
					inputLabel,
					arrowsLabel,
					outputLabel));
		add(new Label("Chart goes here"));
	}
}
