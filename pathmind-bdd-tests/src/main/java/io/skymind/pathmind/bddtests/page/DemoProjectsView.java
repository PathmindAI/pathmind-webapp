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

    @FindBy(xpath = "//vaadin-dialog-overlay/descendant::vaadin-button")
    private WebElement closePopUpBtn;
    @FindBy(id = "overlay")
    private List<WebElement> overlay;
    @FindBy(xpath = "//h2")
    private WebElement demoPopupTitle;
    @FindBy(xpath = "//demo-list")
    private WebElement demoList;

    private static final String DEMO_POPUP_TITLE = "Get started with a pre-configured simulation";
    private static final String FIRST_DEMO_TITLE = "Automated Guided Vehicles (AGV)";
    private static final String FIRST_DEMO_URL = "https://downloads.intercomcdn.com/i/o/273937963/e33cea1e9b5ac12c5d6de951/image.png";
    private static final String FIRST_DEMO_TEXT = "A fleet of automated guided vehicles (AGVs) optimizes its dispatching routes to maximize product throughput in a manufacturing center. When component parts arrive to be processed, they must be brought to the appropriate machine according to a specific processing sequence.";
    private static final String FIRST_DEMO_RESULT = "Result: The reinforcement learning policy outperforms the optimizer by over 20%.";

    private static final String SECOND_DEMO_TITLE = "Product Delivery";
    private static final String SECOND_DEMO_URL = "https://downloads.intercomcdn.com/i/o/233456333/5221ec61b7e207c74823733d/image.png";
    private static final String SECOND_DEMO_TEXT = "This model simulates product delivery in Europe. The supply chain includes three manufacturing centers and fifteen distributors that order random amounts of the product every 1-2 days. There is a fleet of trucks in each manufacturing facility. When a manufacturing facility receives an order from a distributor, it checks the number of products in storage. If the required amount is available, it sends a loaded truck to the distributor. Otherwise, the order waits until the factory produces sufficient inventory.";
    private static final String SECOND_DEMO_RESULT = "Result: Reinforcement learning outperforms nearest manufacturing center heuristic by over 80%.";

    private static final String THIRD_DEMO_TITLE = "Autonomous Moon Landing";
    private static final String THIRD_DEMO_URL = "https://downloads.intercomcdn.com/i/o/222782983/ffe1bbf2a4c5766698a7dbca/Overview.png";
    private static final String THIRD_DEMO_TEXT = "This model simulates a lunar module as it attempts to make a safe landing on the moon. Several key factors are considered as the module approaches the designated landing area and each must have values within a safe zone to avoid crashing or drifting into space.";
    private static final String THIRD_DEMO_RESULT = "Result: The AI learns to land safely on the moon without human intervention.";

    public void closeDemoProjectsPopUp() {
        closePopUpBtn.click();
    }

    public void checkThatDemoProjectsPopupIsShown(boolean shown) {
        setImplicitTimeout(3, SECONDS);
        if (shown) {
            assertThat(overlay.size(), is(1));
        } else {
            assertThat(overlay.size(), is(0));
        }
        resetImplicitTimeout();
    }

    public void checkDemoPopupElements() {
        assertThat(demoPopupTitle.getText(), is(DEMO_POPUP_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > h4")).getText(), is(FIRST_DEMO_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > div > img")).getAttribute("src"), is(FIRST_DEMO_URL));
        assertThat(demoList.findElements(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > div")).size(), is(not(0)));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > p.description")).getText(), is(FIRST_DEMO_TEXT));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > p.result")).getText(), is(FIRST_DEMO_RESULT));
        assertThat(demoList.findElements(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(1) > vaadin-button")).size(), is(not(0)));

        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > h4")).getText(), is(SECOND_DEMO_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > div > img")).getAttribute("src"), is(SECOND_DEMO_URL));
        assertThat(demoList.findElements(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > div")).size(), is(not(0)));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > p.description")).getText(), is(SECOND_DEMO_TEXT));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > p.result")).getText(), is(SECOND_DEMO_RESULT));
        assertThat(demoList.findElements(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(2) > vaadin-button")).size(), is(not(0)));

        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > h4")).getText(), is(THIRD_DEMO_TITLE));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > div > img")).getAttribute("src"), is(THIRD_DEMO_URL));
        assertThat(demoList.findElements(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > div")).size(), is(not(0)));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > p.description")).getText(), is(THIRD_DEMO_TEXT));
        assertThat(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > p.result")).getText(), is(THIRD_DEMO_RESULT));
        assertThat(demoList.findElements(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(3) > vaadin-button")).size(), is(not(0)));
    }

    public void clickDemoPopupGetStartedBtn(String model) {
        String btnNumber= "";
        switch (model) {
            case "Automated Guided Vehicles (AGV)":
                btnNumber = "1";
                break;
            case "Product Delivery":
                btnNumber = "2";
                break;
            case "Autonomous Moon Landing":
                btnNumber = "3";
                break;
        }
        WebElement btn = utils.expandRootElement(demoList.findElement(By.cssSelector("vaadin-horizontal-layout > vaadin-vertical-layout:nth-child(" + btnNumber + ") > vaadin-button")));
        btn.findElement(By.id("button")).click();
        setImplicitTimeout(600, SECONDS);
        utils.waitForLoadingBar();
        resetImplicitTimeout();
    }
}
