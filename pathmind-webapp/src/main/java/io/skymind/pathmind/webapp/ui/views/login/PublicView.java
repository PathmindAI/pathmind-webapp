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
                "<!-- Google Analytics -->"+
                "<script>"+
                "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){"+
                "(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),"+
                "m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)"+
                "})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');"+
                "ga('create', 'UA-48811288-8', 'auto');"+
                "ga('send', 'pageview');"+
                "</script>"+
                "<!-- End Google Analytics -->"+
                "<script src=\"https://www.googleoptimize.com/optimize.js?id=GTM-T2DSBKT\"></script>", InitialPageSettings.WrapMode.NONE);
        settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
                "<style>.async-hide { opacity: 0 !important} </style><script>(function(a,s,y,n,c,h,i,d,e){s.className+=' '+y;h.start=1*new Date;"+
                "h.end=i=function(){s.className=s.className.replace(RegExp(' ?'+y),'')};"+
                "(a[n]=a[n]||[]).hide=h;setTimeout(function(){i();h.end=null},c);h.timeout=c;"+
                "})(window,document.documentElement,'async-hide','dataLayer',4000,"+
                "{'GTM-T2DSBKT':true});</script>", InitialPageSettings.WrapMode.NONE);
    }

}
// "<script src=\"https://www.googleoptimize.com/optimize.js?id=GTM-T2DSBKT\"></script>"
