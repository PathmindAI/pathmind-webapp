package pathmind.steps;

import net.thucydides.core.annotations.Step;
import pathmind.page.AccountPage;

public class AccountPageSteps {

    private AccountPage accountPage;

    @Step
    public void checkThatAccountPageOpened(){
        accountPage.checkThatAccountPageOpened();
    }
}
