package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.Run;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PolicyStatusDetailsPanel extends VerticalLayout
{
	private static Logger log = LogManager.getLogger(PolicyStatusDetailsPanel.class);

	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	// TODO -> Should be swapping back and forth between Label and ProgressBar.
	private Label runProgressLabel = new Label();
	private Label runTypeLabel = new Label();
	private Label elapsedTimeLabel = new Label();

	public PolicyStatusDetailsPanel()
	{
		Label[] labels = Arrays.asList(
			getElementLabel("Status"),
			getElementLabel(""),
			getElementLabel("Run Type"),
			getElementLabel("Elapsed"))
				.stream().toArray(Label[]::new);

		removeTopMargins(labels);
		removeTopMargins(statusLabel, runProgressLabel, runTypeLabel, elapsedTimeLabel);

		VerticalLayout leftVerticalLayout = new VerticalLayout(labels);
		leftVerticalLayout.setHorizontalComponentAlignment(Alignment.END, labels);
		leftVerticalLayout.setWidthFull();
		leftVerticalLayout.setPadding(false);

		VerticalLayout rightVerticalLayout = new VerticalLayout(
				statusLabel,
				runProgressLabel,
				runTypeLabel,
				elapsedTimeLabel);
		rightVerticalLayout.setDefaultHorizontalComponentAlignment(Alignment.START);
		rightVerticalLayout.setPadding(false);

		HorizontalLayout wrapper = WrapperUtils.wrapFormCenterHorizontal(
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

	public void update(Policy policy)
	{
		statusLabel.setText(policy.getRun().getStatusEnum().name());
		runProgressLabel.setText("TODO");
		runTypeLabel.setText(policy.getRun().getRunTypeEnum().name());
		elapsedTimeLabel.setText("TODO");
	}
}
