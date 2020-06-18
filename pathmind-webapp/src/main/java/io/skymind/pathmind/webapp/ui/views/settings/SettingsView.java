package io.skymind.pathmind.webapp.ui.views.settings;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.constants.ViewPermission;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.security.annotation.Permission;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.SETTINGS_URL, layout = MainLayout.class)
@Permission(permissions = ViewPermission.SETTINGS_READ)
public class SettingsView extends PathMindDefaultView {

    private final SettingsViewContent settingsViewContent;

    @Autowired
    public SettingsView(SettingsViewContent settingsViewContent) {
        this.settingsViewContent = settingsViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Settings");
    }

    @Override
    protected Component getMainContent() {
        return settingsViewContent;
    }
}
