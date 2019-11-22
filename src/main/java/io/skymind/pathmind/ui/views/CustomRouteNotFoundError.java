package io.skymind.pathmind.ui.views;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;

import io.skymind.pathmind.ui.views.login.LoginView;
import javax.servlet.http.HttpServletResponse;

public class CustomRouteNotFoundError extends RouteNotFoundError
{
	public CustomRouteNotFoundError() {
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		event.rerouteTo(LoginView.class);
		return HttpServletResponse.SC_ACCEPTED;
	}
}