package io.skymind.pathmind.ui.views.experiment.components;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.utils.WrapperUtils;

public class TrainingStartingPlaceholder extends VerticalLayout {
	
	public TrainingStartingPlaceholder() {
		setSpacing(false);
		ProgressBar progressBar = new ProgressBar();
		progressBar.setIndeterminate(true);
		VerticalLayout placeholderContent = WrapperUtils.wrapFormCenterVertical(
				LabelFactory.createLabel("Starting the training…", CssMindPathStyles.SECTION_TITLE_LABEL),
				new Paragraph("You’ll see the first results here within a few minutes."),
				new Paragraph("The Pathmind training process starts a cluster to explore multiple combinations of hyperparameters automatically. We train the policy for your simulation using the best configuration."),
				new Paragraph("Training will take a few hours. We’ll send you an email when it’s complete!"),
	            progressBar);
		
		add(placeholderContent);
		addClassName("training-starting-placeholder");

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
