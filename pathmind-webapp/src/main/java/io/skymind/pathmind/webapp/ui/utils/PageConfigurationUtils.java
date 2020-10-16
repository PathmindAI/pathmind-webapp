package io.skymind.pathmind.webapp.ui.utils;

import com.vaadin.flow.server.InitialPageSettings;

public class PageConfigurationUtils {
    
    public static void defaultPageConfiguration(InitialPageSettings settings) {
        VaadinUtils.setupFavIcon(settings);
        settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
                "<script src=\"https://www.googleoptimize.com/optimize.js?id=GTM-T2DSBKT\"></script>", InitialPageSettings.WrapMode.NONE);
	}

}
