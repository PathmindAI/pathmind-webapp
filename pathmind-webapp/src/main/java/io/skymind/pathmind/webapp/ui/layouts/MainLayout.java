package io.skymind.pathmind.webapp.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import org.springframework.beans.factory.annotation.Value;

import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.ui.layouts.components.AccountHeaderPanel;
import io.skymind.pathmind.webapp.ui.layouts.components.SectionsHeaderPanel;
import io.skymind.pathmind.webapp.ui.utils.PageConfigurationUtils;

@Push
@CssImport(value = "./styles/styles.css", id = "shared-styles")
@CssImport(value = "./styles/components/vaadin-text-field.css", themeFor = "vaadin-text-field")
@CssImport(value = "./styles/components/vaadin-number-field.css", themeFor = "vaadin-number-field")
@CssImport(value = "./styles/components/vaadin-item.css", themeFor = "vaadin-item")
@CssImport(value = "./styles/components/vaadin-select.css", themeFor = "vaadin-select")
@CssImport(value = "./styles/components/vaadin-select-overlay.css", themeFor = "vaadin-select-overlay")
@CssImport(value = "./styles/components/vaadin-select-text-field.css", themeFor = "vaadin-select-text-field")
@CssImport(value = "./styles/components/vaadin-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/vaadin-context-menu-list-box.css", themeFor = "vaadin-context-menu-list-box")
@CssImport(value = "./styles/components/vaadin-context-menu-item.css", themeFor = "vaadin-context-menu-item")
@CssImport(value = "./styles/components/vaadin-menu-bar.css", themeFor = "vaadin-menu-bar")
@CssImport(value = "./styles/components/vaadin-grid.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/components/vaadin-grid-sorter.css", themeFor = "vaadin-grid-sorter")
@CssImport(value = "./styles/components/vaadin-split-layout.css", themeFor = "vaadin-split-layout")
@CssImport(value = "./styles/components/vaadin-tabs.css", themeFor = "vaadin-tabs")
@CssImport(value = "./styles/components/vaadin-tab.css", themeFor = "vaadin-tab")
@CssImport(value = "./styles/components/vaadin-text-area.css", themeFor = "vaadin-text-area")
@CssImport(value = "./styles/components/vaadin-checkbox.css", themeFor = "vaadin-checkbox")
@CssImport(value = "./styles/components/vaadin-custom-field.css", themeFor = "vaadin-custom-field")
@CssImport(value = "./styles/components/multiselect-combo-box.css", themeFor = "multiselect-combo-box")
@CssImport(value = "./styles/components/multiselect-combo-box-input.css", themeFor = "multiselect-combo-box-input")
@CssImport(value = "./styles/layouts/vaadin-app-layout.css", themeFor = "vaadin-app-layout")
@CssImport(value = "./styles/views/experiment-view.css")
@CssImport(value = "./styles/views/project-view.css")
@CssImport(value = "./styles/views/search-results-view.css")
@CssImport(value = "./styles/views/pathmind-dialog-view.css", id = "pathmind-dialog-view")
// Stripe should be added to every page to be able to use their fraud detection mechanism
@JavaScript("https://js.stripe.com/v3/")
@Theme(Lumo.class)
public class MainLayout extends AppLayout implements PageConfigurator {
    private AccountHeaderPanel accountHeaderPanel;

    public MainLayout(CurrentUser user,
            FeatureManager featureManager,
            @Value("${pathmind.stripe.public.key}") String publicKey,
            @Value("${pathmind.pathmind-api.url}") String pathmindApiUrl) {
        setId("pathmind-app-layout");
        boolean hasLoginUser = user != null && user.getUser() != null;
        addToNavbar(new SectionsHeaderPanel(hasLoginUser, user));
        if (hasLoginUser) {
            accountHeaderPanel = new AccountHeaderPanel(() -> getUI(), user.getUser(), featureManager);
            addToNavbar(accountHeaderPanel);
        }

        // Added a message just in case there's ever a failure.
        setContent(new Span("Error. Please contact Pathmind for assistance"));
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        PageConfigurationUtils.defaultPageConfiguration(settings);
    }

    public void clearSearchBoxValue() {
        if (accountHeaderPanel != null) {
            accountHeaderPanel.clearSearchBoxValue();
        }
    }

    public void setSearchBoxValue(String text) {
        if (accountHeaderPanel != null) {
            accountHeaderPanel.setSearchBoxValue(text);
        }
    }

    public String getSearchBoxValue() {
        if (accountHeaderPanel != null) {
            return accountHeaderPanel.getSearchBoxValue();
        }
        return "";
    }
}
