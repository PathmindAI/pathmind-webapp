package io.skymind.pathmind.webapp.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.NEW_PROJECT, layout = MainLayout.class)
public class NewProjectView extends PathMindDefaultView {

    private final NewProjectViewContent newProjectViewContent;

    @Autowired
	public NewProjectView(NewProjectViewContent newProjectViewContent) {
        this.newProjectViewContent = newProjectViewContent;
    }

	protected Component getMainContent() {
        return newProjectViewContent;
	}

	@Override
	protected VerticalLayout getTitlePanel() {
		return null;
	}
}
