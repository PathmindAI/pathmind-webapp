package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.plugins.IntercomIntegrationPlugin;
import io.skymind.pathmind.ui.utils.VaadinUtils;
import io.skymind.pathmind.ui.views.account.AccountView;
import io.skymind.pathmind.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.ui.views.project.NewProjectView;
import io.skymind.pathmind.utils.PathmindUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("login")
@Theme(Lumo.class)
@Tag("login-view")
@NpmPackage(value = "@polymer/iron-form", version = "^3.0.1")
@JsModule("./src/login-view.js")
public class LoginView extends PolymerTemplate<LoginView.Model>
		implements AfterNavigationObserver, BeforeEnterObserver, HasDynamicTitle, PageConfigurator
{

	@Autowired
	private ProjectDAO projectDAO;

	public LoginView(IntercomIntegrationPlugin intercomIntegrationPlugin)
	{
		intercomIntegrationPlugin.addPluginToPage();
	}

	private Class getRerouteClass() {
		if(projectDAO.getProjectsForUser(SecurityUtils.getUserId()).isEmpty())
			return NewProjectView.class;
		return DashboardView.class;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(getRerouteClass());
			// Make sure automatic push mode is enabled. If we don't do this, automatic push
			// won't work even we have proper annotations in place.
			UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
			return;
		}

	}

	@Override
	public String getPageTitle() {
		return PathmindUtils.getPageTitle();
	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		VaadinUtils.setupFavIcon(settings);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		getModel().setError(
				event.getLocation().getQueryParameters().getParameters().containsKey("error"));
	}

	public interface Model extends TemplateModel {
		void setError(boolean error);
	}

}
