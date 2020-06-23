package io.skymind.pathmind;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

import io.skymind.pathmind.webapp.AbstractKaribuIntegrationTest;
import io.skymind.pathmind.webapp.ui.views.account.AccountEditView;
import io.skymind.pathmind.webapp.ui.views.account.AccountEditViewContent;
import io.skymind.pathmind.webapp.ui.views.account.AccountView;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static org.junit.Assert.assertSame;


@WithUserDetails("dev_pathmind@skymind.io")
@Transactional
public class AccountEditViewTest extends AbstractKaribuIntegrationTest {

    private AccountEditViewContent pageContent;

    @Before
    public void setUp() throws Exception {
        UI.getCurrent().navigate(AccountEditView.class);
        pageContent = _get(AccountEditView.class).accountEditViewContent;
    }

    @Test
    public void testChangingEmail() {
        // After modifying value in First Name field
        pageContent.email.setValue("skywalker@jedi.com");
        pageContent.updateBtn.click();

        // Should show popup
        _assertOne(ConfirmDialog.class);
    }

    @Test
    public void testChangingFirstName() {
        // After modifying value in First Name field
        pageContent.firstName.setValue("Quoi");
        pageContent.updateBtn.click();

        // Should navigate to Account View
        assertSame(AccountView.class, KaribuUtils.getActiveViewClass());
    }

    @Test
    public void testChangingLastName() {
        // After modifying value in First Name field
        pageContent.lastName.setValue("Skywalker");
        pageContent.updateBtn.click();

        // Should navigate to Account View
        assertSame(AccountView.class, KaribuUtils.getActiveViewClass());
    }
}
