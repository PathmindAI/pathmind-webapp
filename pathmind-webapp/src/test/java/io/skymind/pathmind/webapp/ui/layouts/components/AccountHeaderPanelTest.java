package io.skymind.pathmind.webapp.ui.layouts.components;

import com.github.mvysny.kaributesting.v10.ContextMenuKt;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.skymind.pathmind.KaribuUtils;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.ui.views.account.AccountView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.Assert.assertEquals;

public class AccountHeaderPanelTest {

    private AccountHeaderPanel accountHeaderPanel;
    private UI ui;

    @Before
    public void setUp() {
        PathmindUser user = new PathmindUser();
        user.setFirstname("Joe");
        user.setLastname("Cool");
        accountHeaderPanel = new AccountHeaderPanel(user);
        ui = KaribuUtils.setup(accountHeaderPanel);
    }

    @Test
    public void nameIsFormattedCorrectly() {
        MenuBar menuBar = _get(accountHeaderPanel, MenuBar.class);

        HorizontalLayout rootItemComponent = (HorizontalLayout) menuBar.getItems().get(0).getChildren().findFirst().orElse(null);
        Span usernameSpan = (Span) rootItemComponent.getComponentAt(1);

        assertEquals("Joe Cool", usernameSpan.getText());
    }

    @Test
    public void clickingOnAccountNavigatesToAccountView() {
        Mockito.doNothing().when(ui).navigate(AccountView.class);

        ContextMenuKt._clickItemWithCaption(_get(accountHeaderPanel, MenuBar.class), "Account");

        Mockito.verify(ui, Mockito.times(1)).navigate(AccountView.class);
    }
}