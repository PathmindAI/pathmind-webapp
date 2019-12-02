package io.skymind.pathmind.ui.views.login;

import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import io.skymind.pathmind.ui.utils.VaadinUtils;
import io.skymind.pathmind.utils.PathmindUtils;

public interface PublicView extends HasDynamicTitle, PageConfigurator {

	default String getPageTitle() {
		return PathmindUtils.getPageTitle();
	}

	default void configurePage(InitialPageSettings settings) {
		VaadinUtils.setupFavIcon(settings);
	}
}
