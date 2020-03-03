package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import io.skymind.pathmind.utils.DateAndTimeUtils;

public class PathmindTrainingProgress extends VerticalLayout {
	private ProgressBar progressBar = new ProgressBar(0, 100);
	private Span progressValueLabel = LabelFactory.createLabel("");
	
	public PathmindTrainingProgress() {
		setPadding(false);
		setSpacing(false);
		add(progressBar, progressValueLabel);
	}
	
	public void setValue(double progress, double estimatedTime) {
		progressBar.setValue(progress);
		String formattedEstimatedTime = DateAndTimeUtils.formatETA((long) estimatedTime);
		progressValueLabel.setText(formatProgressLabel(progress, formattedEstimatedTime));
	}
	
	/**
	 * Formats in progress training status.<br/>
	 * Example output: <code>44 % (ETA: 54 min)</code>
	 */
	private String formatProgressLabel(double progress, String formattedEstimatedTime) {
		return String.format("%.0f %% (ETA: %s)", progress, formattedEstimatedTime);
	}
}
