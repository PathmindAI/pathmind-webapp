package io.skymind.pathmind.webapp.ui.views.dashboard.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindGreetingComponent;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class EmptyDashboardPlaceholder extends VerticalLayout {
	
	public EmptyDashboardPlaceholder() {
		setSpacing(false);
		
		VerticalLayout placeholderContent = WrapperUtils.wrapFormCenterVertical(
				LabelFactory.createLabel("Get started by creating your first project.", CssMindPathStyles.SECTION_TITLE_LABEL),
				VaadinIcon.ARROW_DOWN.create());
		placeholderContent.addClassName("dashboard-placeholder");
		
		add(new PathmindGreetingComponent(), placeholderContent);

		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setWidthFull();
	}
}
