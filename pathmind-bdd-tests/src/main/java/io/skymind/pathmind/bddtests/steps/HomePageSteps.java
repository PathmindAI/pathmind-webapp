package io.skymind.pathmind.bddtests.steps;

import net.thucydides.core.annotations.Step;
import io.skymind.pathmind.bddtests.page.HomePage;
import io.skymind.pathmind.bddtests.page.LoginPage;

public class HomePageSteps {

    private LoginPage loginPage;
    private HomePage homePage;

    @Step
    public void openProjectsPage() {
        homePage.openProjectsPage();
    }

    @Step
    public void logoutFromPathmind() {
        homePage.logoutFromPathmind();
    }

    @Step
    public void clickLearnBtn() {
        homePage.clickLearnBtn();
    }

    @Step
    public void checkThatLearnPageOpened(String learnPage) {
        homePage.checkThatLearnPageOpened(learnPage);
    }

    @Step
    public void openDashboardPage() {
        homePage.openDashboardPage();
    }

    @Step
    public void checkThatDashboardPageOpened() {
        homePage.checkThatDashboardPageOpened();
    }

    @Step
    public void checkThatProjectsPageOpened() {
        homePage.checkThatProjectsPageOpened();
    }

    @Step
    public void closeBrowserTab() {
        homePage.closeBrowserTab();
    }

    @Step
    public void openUserDropdown() {
        homePage.openUserDropdown();
    }

    @Step
    public void clickAccountBtn() {
        homePage.clickAccountBtn();
    }

    @Step
    public void clickBackButton() {
        homePage.getDriver().navigate().back();
    }

    @Step
    public void clickGettingStartedGuideButton() {
        homePage.clickGettingStartedGuideButton();
    }

    @Step
    public void checkThatProjectsButtonHighlightIs(Boolean status) {
        homePage.checkThatProjectsButtonHighlightIs(status);
    }
}
