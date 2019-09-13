package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.utils.ExperimentUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.utils.DateTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ExperimentStatusDetailsPanel extends VerticalLayout
{
	private static Logger log = LogManager.getLogger(ExperimentStatusDetailsPanel.class);

	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runTypeLabel = new Label();
	private Label elapsedTimeLabel = new Label();
	private Label completedLabel = new Label();
	public Label algorithmLabel = new Label();

	public ExperimentStatusDetailsPanel()
	{
		Label[] labels = Arrays.asList(
			getElementLabel("Status"),
			getElementLabel("Run Type"),
			getElementLabel("Elapsed"),
			getElementLabel("Completed"),
			getElementLabel("Algorithm"))
				.stream().toArray(Label[]::new);

		removeTopMargins(labels);
		removeTopMargins(statusLabel, runTypeLabel, elapsedTimeLabel, completedLabel, algorithmLabel);

		VerticalLayout leftVerticalLayout = new VerticalLayout(labels);
		leftVerticalLayout.setHorizontalComponentAlignment(Alignment.END, labels);
		leftVerticalLayout.setWidthFull();
		leftVerticalLayout.setPadding(false);

		VerticalLayout rightVerticalLayout = new VerticalLayout(
				statusLabel,
				runTypeLabel,
				elapsedTimeLabel,
				completedLabel,
				algorithmLabel);
		rightVerticalLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
		rightVerticalLayout.setPadding(false);

		HorizontalLayout wrapper = WrapperUtils.wrapCenteredFormHorizontal(
				leftVerticalLayout,
				rightVerticalLayout);
		wrapper.getStyle().set("padding-top", "10px");

		add(wrapper);
	}

	// TODO -> Move style to CSS
	private Label getElementLabel(String label) {
		Label fieldLabel = new Label(label + " : ");
		fieldLabel.getStyle().set("font-weight", "bold");
		return fieldLabel;
	}

	private void removeTopMargins(Label... labels) {
		Arrays.stream(labels).forEach(label ->
				label.getStyle().set("margin-top", "0px"));
	}

	public void update(Experiment experiment)
	{
		statusLabel.setText(experiment.getStatusEnum().toString());
		runTypeLabel.setText(experiment.getRunTypeEnum().toString());
		elapsedTimeLabel.setText(DateTimeUtils.formatTime(ExperimentUtils.getElapsedTime(experiment)));
		completedLabel.setText(experiment.getCompletedEnum().toString());
		algorithmLabel.setText(experiment.getAlgorithm().toString());
	}
}
