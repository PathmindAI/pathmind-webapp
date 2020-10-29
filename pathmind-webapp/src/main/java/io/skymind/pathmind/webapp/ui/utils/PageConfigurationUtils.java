package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.server.InitialPageSettings;

public class PageConfigurationUtils {
    
    public static void defaultPageConfiguration(InitialPageSettings settings) {
        VaadinUtils.setupFavIcon(settings);
	}

}
