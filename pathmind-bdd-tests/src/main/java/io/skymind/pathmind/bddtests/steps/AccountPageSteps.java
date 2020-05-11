package io.skymind.pathmind.bddtests.steps;

import net.thucydides.core.annotations.Step;
import io.skymind.pathmind.bddtests.page.AccountPage;

public class AccountPageSteps {

    private AccountPage accountPage;

    @Step
    public void checkThatAccountPageOpened(){
        accountPage.checkThatAccountPageOpened();
    }
}
