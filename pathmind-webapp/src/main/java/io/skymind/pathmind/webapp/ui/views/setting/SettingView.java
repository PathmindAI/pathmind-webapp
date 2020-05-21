package io.skymind.pathmind.webapp.ui.views.setting;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.SETTING_URL, layout = MainLayout.class)
public class SettingView extends PathMindDefaultView {

    private final SettingViewContent settingViewContent;

    @Autowired
    public SettingView(SettingViewContent settingViewContent) {
        this.settingViewContent = settingViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Setting");
    }

    @Override
    protected Component getMainContent() {
        return settingViewContent;
    }
}
