package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.bus.PathmindBusEvent;
import io.skymind.pathmind.bus.utils.PolicyBusEventUtils;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.utils.DateAndTimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class PolicyStatusDetailsPanel extends VerticalLayout
{
	private static Logger log = LogManager.getLogger(PolicyStatusDetailsPanel.class);

	private Policy policy;

	private Label statusLabel = new Label(RunStatus.NotStarted.toString());
	private Label runProgressLabel = new Label();
	private Label runTypeLabel = new Label();
	private Label elapsedTimeLabel = new Label();

	private Flux<PathmindBusEvent> consumer;

	public PolicyStatusDetailsPanel(Flux<PathmindBusEvent> consumer)
	{
		this.consumer = consumer;

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

		HorizontalLayout wrapper = new HorizontalLayout(
				leftVerticalLayout,
				rightVerticalLayout);
		wrapper.getStyle().set("padding-top", "10px");
		wrapper.setWidthFull();

		add(wrapper);
	}

	// TODO -> CSS -> Move style to CSS
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
		updateComponentsForPolicy(policy);
		subscribeToEventBus(UI.getCurrent(), consumer);
	}

	private void updateComponentsForPolicy(Policy policy) {
		this.policy = policy;
		statusLabel.setText(PolicyUtils.getRunStatus(policy).toString());
		runProgressLabel.setText(DateAndTimeUtils.formatDateAndTimeShortFormatter(PolicyUtils.getRunCompletedTime(policy)));
		runTypeLabel.setText(policy.getRun().getRunTypeEnum().toString());
		elapsedTimeLabel.setText(PolicyUtils.getElapsedTime(policy));
	}

	private Policy getPolicy() {
		return policy;
	}

	// IMPORTANT NOTE -> I cannot use getUI() as requested in the PR 252 because PolicyUtils does not accept an optional and at this stage I want
	// to minimize any other code impact. Please add the request for PushUtils to accept Optionals as a new github issues. Thank you.
	private void subscribeToEventBus(UI ui, Flux<PathmindBusEvent> consumer) {
		PolicyBusEventUtils.consumerBusEventBasedOnPolicy(
				consumer,
				this::getPolicy,
				updatedPolicy -> PushUtils.push(ui, () -> {
					updateComponentsForPolicy(updatedPolicy);
					elapsedTimeLabel.setText(PolicyUtils.getElapsedTime(policy));
				}));
	}
}
