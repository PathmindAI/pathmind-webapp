package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;

public class TrainingStartingPlaceholder extends VerticalLayout {
	
	public TrainingStartingPlaceholder() {
		setPadding(false);
		setSpacing(false);
		ProgressBar progressBar = new ProgressBar();
		progressBar.setIndeterminate(true);

		add(LabelFactory.createLabel("Starting the training…", CssMindPathStyles.SECTION_TITLE_LABEL),
			new Paragraph("You’ll see the first results here within a few minutes."),
			new Paragraph("The Pathmind training process starts a cluster to explore multiple combinations of hyperparameters automatically. We train the policy for your simulation using the best configuration."),
			new Paragraph("Training will take a few hours. We’ll send you an email when it’s complete!"),
			progressBar);
		addClassName("training-starting-placeholder");
	}
}
