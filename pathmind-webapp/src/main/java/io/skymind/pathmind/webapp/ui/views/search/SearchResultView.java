package io.skymind.pathmind.webapp.ui.views.search;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;

@Route(value= Routes.SEARCHRESULTS_URL, layout = MainLayout.class)
public class SearchResultView extends PathMindDefaultView {

    
    
    @Override
    protected Component getTitlePanel() {
        return LabelFactory.createLabel("Search results");
    }

    @Override
    protected Component getMainContent() {
        return null;
    }

}
