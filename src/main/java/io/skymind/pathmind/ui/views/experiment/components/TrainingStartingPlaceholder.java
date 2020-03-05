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
				new Paragraph("You’ll see the results as the training starts. This could take a couple minutes."),
	            new Paragraph("We'll send you an email when training completes!"),
	            progressBar);
		
		add(placeholderContent);
		addClassName("training-starting-placeholder");

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
