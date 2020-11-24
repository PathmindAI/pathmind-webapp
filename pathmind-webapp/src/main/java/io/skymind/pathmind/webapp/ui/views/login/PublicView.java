package io.skymind.pathmind.webapp.ui.views.login;

import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import io.skymind.pathmind.webapp.ui.utils.PageConfigurationUtils;
import io.skymind.pathmind.webapp.utils.PathmindUtils;

public interface PublicView extends HasDynamicTitle, PageConfigurator {

    default String getPageTitle() {
        return PathmindUtils.getPageTitle();
    }

    default void configurePage(InitialPageSettings settings) {
        PageConfigurationUtils.defaultPageConfiguration(settings);
        settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
                "<script src=\"https://www.googleoptimize.com/optimize.js?id=GTM-T2DSBKT\"></script>", InitialPageSettings.WrapMode.NONE);
        settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
                "<style>.async-hide { opacity: 0 !important} </style><script>(function(a,s,y,n,c,h,i,d,e){s.className+=' '+y;h.start=1*new Date;" +
                        "h.end=i=function(){s.className=s.className.replace(RegExp(' ?'+y),'')};" +
                        "(a[n]=a[n]||[]).hide=h;setTimeout(function(){i();h.end=null},c);h.timeout=c;" +
                        "})(window,document.documentElement,'async-hide','dataLayer',4000," +
                        "{'GTM-T2DSBKT':true});</script>", InitialPageSettings.WrapMode.NONE);
    }

}
