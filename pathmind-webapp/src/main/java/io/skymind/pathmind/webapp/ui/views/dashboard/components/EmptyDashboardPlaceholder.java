package io.skymind.pathmind.webapp.ui.views.dashboard.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindGreetingComponent;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.project.NewProjectView;

public class EmptyDashboardPlaceholder extends VerticalLayout {

    public EmptyDashboardPlaceholder() {
        setSpacing(false);

        Anchor gettingStartedButton = new Anchor("https://help.pathmind.com/en/articles/4004788-getting-started", "Getting Started Guide");
        gettingStartedButton.setTarget("_blank");
        gettingStartedButton.addClassName("button-link");

        RouterLink createProjectLink = new RouterLink("create your first project.", NewProjectView.class);
        createProjectLink.addClassName("create-project-link");

        VerticalLayout placeholderContent = WrapperUtils.wrapFormCenterVertical(
                WrapperUtils.wrapSizeFullCenterHorizontal(
                    LabelFactory.createLabel("Let's begin by opening the", CssMindPathStyles.SECTION_TITLE_LABEL),
                    gettingStartedButton),
                WrapperUtils.wrapSizeFullCenterHorizontal(
                    new Span("or skip ahead to "),
                    createProjectLink));

        placeholderContent.addClassName("dashboard-placeholder");
        
        add(new PathmindGreetingComponent(), placeholderContent);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setWidthFull();
    }
}
