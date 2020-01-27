package pathmind.page;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pathmind.Utils;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@DefaultUrl("page:home.page")
public class AccountPage extends PageObject {

    private Utils utils;

    @FindBy(xpath = "//account-view-content")
    private WebElement accountViewShadow;
    @FindBy(id = "editInfoBtn")
    private WebElement editInfoBtnShadow;
    @FindBy(id = "changePasswordBtn")
    private WebElement changePasswordBtnShadow;
    @FindBy(id = "upgradeBtn")
    private WebElement upgradeBtnShadow;
    @FindBy(id = "editPaymentBtn")
    private WebElement editPaymentBtnShadow;

    public void checkThatAccountPageOpened() {
        waitABit(2500);
        assertThat(getDriver().findElement(By.cssSelector(".section-label-title")).getText(), containsString("ACCOUNT"));
        assertThat(getDriver().getTitle(), containsString("Pathmind | Account"));
        WebElement e = utils.expandRootElement(accountViewShadow);
        assertThat(e.findElement(By.cssSelector("vaadin-tab")).isDisplayed(), is(true));
        assertThat(e.findElement(By.cssSelector("vaadin-tab")).getText(), containsString("Account Information"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(1)")).getText(), containsString("User Email"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(3)")).getText(), containsString("First Name"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(1) .info div:nth-child(5)")).getText(), containsString("Last Name"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(2) .info div:nth-child(1)")).getText(), containsString("Password"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(3) .info div:nth-child(1)")).getText(), containsString("Current Subscription"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(3) .info div:nth-child(2)")).getText(), containsString("Early Access"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(4) .info div:nth-child(1)")).getText(), containsString("Payment"));
        assertThat(e.findElement(By.cssSelector(".inner-content vaadin-horizontal-layout:nth-child(4) .info div:nth-child(2)")).getText(), containsString("Billing Information"));
        assertThat(e.findElement(By.id("editInfoBtn")).isDisplayed(), is(true));
        assertThat(e.findElement(By.id("editInfoBtn")).getText(), containsString("Edit"));
        assertThat(e.findElement(By.id("changePasswordBtn")).isDisplayed(), is(true));
        assertThat(e.findElement(By.id("changePasswordBtn")).getText(), containsString("Change"));
        assertThat(e.findElement(By.id("upgradeBtn")).isDisplayed(), is(true));
        assertThat(e.findElement(By.id("upgradeBtn")).getText(), containsString("Upgrade"));
        assertThat(e.findElement(By.id("editPaymentBtn")).isDisplayed(), is(true));
        assertThat(e.findElement(By.id("editPaymentBtn")).getText(), containsString("Edit"));
        assertThat(e.findElement(By.cssSelector(".support")).isDisplayed(), is(true));
        assertThat(e.findElement(By.cssSelector(".support")).getText(), containsString("Contact Support"));
        assertThat(e.findElement(By.cssSelector(".support")).getAttribute("href"), containsString("mailto:support@pathmind.com"));
    }
}
