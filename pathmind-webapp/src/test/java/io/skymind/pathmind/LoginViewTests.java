package io.skymind.pathmind;

import com.github.mvysny.kaributesting.v10.LoginFormKt;
import com.vaadin.flow.component.login.LoginForm;
import io.skymind.pathmind.webapp.AbstractKaribuIntegrationTest;
import io.skymind.pathmind.webapp.ui.views.dashboard.DashboardView;
import io.skymind.pathmind.webapp.ui.views.login.LoginView;
import io.skymind.pathmind.webapp.ui.views.login.ResetPasswordView;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;

public class LoginViewTests extends AbstractKaribuIntegrationTest {

	@Test
	public void anonymousUsersShouldSeeLoginView() {
        KaribuUtils.assertActiveViewClass(LoginView.class);
	}

    @Test
    @WithUserDetails("steph@followsteph.com")
    public void authenticatedUsersShouldSeeDashboardView() {
        KaribuUtils.assertActiveViewClass(DashboardView.class);
    }

	@Test
	public void clickingOnResetPasswordShouldNavigateToResetPasswordView() {
		LoginFormKt._forgotPassword(_get(LoginForm.class));
		KaribuUtils.assertActiveViewClass(ResetPasswordView.class);
	}
}