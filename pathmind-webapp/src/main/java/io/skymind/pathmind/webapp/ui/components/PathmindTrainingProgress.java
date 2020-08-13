package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import io.skymind.pathmind.shared.utils.DateAndTimeUtils;

public class PathmindTrainingProgress extends VerticalLayout {
    private ProgressBar progressBar = new ProgressBar(0, 100);
    private Span trainingText = new Span("Training....");
    private Span progressValueLabel = new Span();
    private Span etaLabel = new Span();
    private boolean textMode = false;
	
	public PathmindTrainingProgress() {
		setPadding(false);
		setSpacing(false);
		add(progressBar, new Div(trainingText, progressValueLabel, etaLabel));
		setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
	}
	
	/**
	 * Formats in progress training status.<br/>
	 * Example output: <code>44% (ETA: 54 min)</code>
	 */
	public void setValue(double progress, double estimatedTime) {
		progressBar.setValue(progress);
		String formattedEstimatedTime = DateAndTimeUtils.formatETA((long) estimatedTime);
        progressValueLabel.setText(String.format("%.0f%%", progress));
        etaLabel.setText(String.format("(ETA: %s)", formattedEstimatedTime));
        etaLabel.setVisible(progress > 0);
        setVisibleComponents();
    }
    
    public void setTextMode(Boolean textMode) {
        getElement().setProperty("textmode", textMode);
        this.textMode = textMode;
        setVisibleComponents();
    }

    public void setVisibleComponents() {
        progressBar.setVisible(!textMode);
        trainingText.setVisible(textMode);
    }
}