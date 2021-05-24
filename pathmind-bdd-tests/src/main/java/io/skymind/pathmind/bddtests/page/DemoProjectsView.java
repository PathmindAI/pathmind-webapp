package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class DemoProjectsView extends PageObject {

    private Utils utils;

    @FindBy(id = "overlay")
    private List<WebElement> overlay;
    @FindBy(xpath = "//h3")
    private WebElement demoPopupTitle;
    @FindBy(xpath = "//demo-list")
    private WebElement demoList;

    private static final String DEMO_POPUP_TITLE = "Get started with a pre-configured simulation";
    private static final String FIRST_DEMO_TITLE = "Automated Guided Vehicles";
    private static final String FIRST_DEMO_URL = "https://downloads.intercomcdn.com/i/o/273937963/e33cea1e9b5ac12c5d6de951/image.png";

    private static final String SECOND_DEMO_TITLE = "Product Delivery";
    private static final String SECOND_DEMO_URL = "https://downloads.intercomcdn.com/i/o/233456333/5221ec61b7e207c74823733d/image.png";

    private static final String THIRD_DEMO_TITLE = "Interconnected Call Centers";
    private static final String THIRD_DEMO_URL = "/frontend/images/callcenters.png";

    public void checkDemoListElements() {
        assertThat(demoPopupTitle.getText(), is(DEMO_POPUP_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > span")).getText(), is(FIRST_DEMO_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > img")).getAttribute("src"), is(FIRST_DEMO_URL));

        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > span")).getText(), is(SECOND_DEMO_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > img")).getAttribute("src"), is(SECOND_DEMO_URL));

        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > span")).getText(), is(THIRD_DEMO_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > img")).getAttribute("src"), containsString(THIRD_DEMO_URL));
    }

    public void clickDemoListItem(String model) {
        String btnNumber= "";
        switch (model) {
            case "Automated Guided Vehicles":
                btnNumber = "1";
                break;
            case "Product Delivery":
                btnNumber = "2";
                break;
            case "Interconnected Call Centers":
                btnNumber = "3";
                break;
        }
        WebElement btn = demoList.findElement(By.cssSelector(".demo-item:nth-child(" + btnNumber + ")"));
        btn.click();
        setImplicitTimeout(600, SECONDS);
        utils.waitForLoadingBar();
        resetImplicitTimeout();
    }
}
