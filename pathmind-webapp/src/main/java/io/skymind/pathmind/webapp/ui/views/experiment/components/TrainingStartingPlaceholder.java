package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TrainingStartingPlaceholder extends VerticalLayout {
	
	public TrainingStartingPlaceholder() {
		add(new Span("Starting the training…"),
			new Paragraph("You’ll see the first results here within a few minutes."),
			new Paragraph("The Pathmind training process starts a cluster to explore multiple combinations of hyperparameters automatically. We train the policy for your simulation using the best configuration."),
			new Paragraph("Training will take a few hours. We’ll send you an email when it’s complete!"));
		addClassName("training-starting-placeholder");
	}
}
