package io.skymind.pathmind.security.framework;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import io.skymind.pathmind.security.SecurityUtils;
import io.skymind.pathmind.ui.views.LoginView;

public interface LoginCheckBeforeEnterEvent extends BeforeEnterObserver
{
	default public void beforeEnter(BeforeEnterEvent event) {
		if(!SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(LoginView.class);
		}
	}
}
