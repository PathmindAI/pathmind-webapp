package io.skymind.pathmind.ui.views.dashboard.components;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.utils.WrapperUtils;

public class EmptyDashboardPlaceholder extends VerticalLayout {
	
	public EmptyDashboardPlaceholder() {
		setSpacing(false);
		Image pathMindLogo = new Image("frontend/images/pathmind-logo.png", "Pathmind Logo");
		pathMindLogo.addClassName("navbar-logo");
		add(LabelFactory.createLabel("Welcome to", CssMindPathStyles.SMALL_LIGHT_LABEL),
				pathMindLogo);
		
		VerticalLayout placeholderContent = WrapperUtils.wrapFormCenterVertical(
				LabelFactory.createLabel("Get started by creating your first project.", CssMindPathStyles.SECTION_TITLE_LABEL),
				VaadinIcon.ARROW_DOWN.create());
		placeholderContent.addClassName("dashboard-placeholder");
		add(placeholderContent);

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
