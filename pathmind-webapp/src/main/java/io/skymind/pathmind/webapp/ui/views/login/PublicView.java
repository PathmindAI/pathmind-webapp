package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import io.skymind.pathmind.webapp.ui.utils.PageConfigurationUtils;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

@Push
public interface PublicView extends HasDynamicTitle, PageConfigurator {

    default String getPageTitle() {
        return PathmindUtils.getPageTitle();
    }

    default void configurePage(InitialPageSettings settings) {
        PageConfigurationUtils.defaultPageConfiguration(settings);
    }

}
