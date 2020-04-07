package io.skymind.pathmind.webapp.ui.views.dashboard.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindGreetingComponent;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class EmptyDashboardPlaceholder extends VerticalLayout {
	
	public EmptyDashboardPlaceholder() {
		setSpacing(false);

		Anchor gettingStartedButton = new Anchor("https://help.pathmind.com/en/articles/3750911-2-finding-the-cheese", "Getting Started Guide");
		gettingStartedButton.setTarget("_blank");
		gettingStartedButton.addClassName("button-link");

		VerticalLayout placeholderContent = WrapperUtils.wrapFormCenterVertical(
				WrapperUtils.wrapSizeFullCenterHorizontal(
					LabelFactory.createLabel("Let's begin by opening the", CssMindPathStyles.SECTION_TITLE_LABEL),
					gettingStartedButton),
				new Span("or"),
				LabelFactory.createLabel("Get started by creating your first project.", CssMindPathStyles.SECTION_TITLE_LABEL),
				VaadinIcon.ARROW_DOWN.create());

		placeholderContent.addClassName("dashboard-placeholder");
		
		add(new PathmindGreetingComponent(), placeholderContent);

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
