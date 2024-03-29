package io.skymind.pathmind.webapp.ui.layouts.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.views.account.AccountUpgradeView;
import io.skymind.pathmind.webapp.ui.views.header.components.UpgradeToProButton;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;

public class SectionsHeaderPanel extends HorizontalLayout {

    public SectionsHeaderPanel(boolean hasLoginUser, CurrentUser currentUser) {
        HorizontalLayout sectionsHorizontalLayout = new HorizontalLayout();
        sectionsHorizontalLayout.add(linkedLogo());
        if (hasLoginUser) {
            RouterLink projectsLink = new RouterLink("Projects", ProjectsView.class);
            projectsLink.setHighlightCondition((link, event) ->
                event.getLocation().getPath().equals(link.getHref()) || event.getLocation().getPath().equals(""));
            
            RouterLink pricingLink = new RouterLink("Pricing", AccountUpgradeView.class);

            sectionsHorizontalLayout.add(
                    projectsLink,
                    getTutorialsAnchor(),
                    pricingLink,
                    getFAQAnchor(),
                    getHelpAnchor());

            if (currentUser.getUser().isTrialPlanUser()) {
                sectionsHorizontalLayout.add(new UpgradeToProButton());
            }
        }
        add(sectionsHorizontalLayout);

        sectionsHorizontalLayout.setId("nav-main-links");
    }

    private Anchor linkedLogo() {
        Image logo = new Image("frontend/images/pathmind-logo.svg", "Pathmind Logo");
        Anchor anchor = new Anchor("/", logo);
        anchor.addClassName("navbar-logo");
        return anchor;
    }

    private Anchor getTutorialsAnchor() {
        return getAnchor("https://help.pathmind.com/en/collections/2106204-tutorials", "Tutorials");
    }

    private Anchor getFAQAnchor() {
        return getAnchor("https://help.pathmind.com/en/collections/2299162-faq", "FAQ");
    }

    private Anchor getHelpAnchor() {
        return getAnchor("https://help.pathmind.com/", "Help");
    }

    private Anchor getAnchor(String url, String text) {
        Anchor anchor = new Anchor(url, text);
        anchor.setTarget("_blank");
        return anchor;
    }
}
