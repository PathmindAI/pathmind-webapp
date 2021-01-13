package io.skymind.pathmind.bddtests.page;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class ExperimentView extends PageObject {

    /*
     * training-status-details-panel
     */
    private String titleLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::span[@class='section-title-label']";
    private String statusLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::vaadin-horizontal-layout[@class='training-status-details-panel']/descendant::span[1]";
    private String statusXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::vaadin-horizontal-layout[@class='training-status-details-panel']/descendant::span[2]";
    private String elapsedXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::vaadin-horizontal-layout[@class='training-status-details-panel']/descendant::span[4]";
    private String stopTrainingBtnXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::div[@class='buttons-wrapper']/vaadin-button[1]";
    private String shareSupportBtnXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::div[@class='buttons-wrapper']/vaadin-button[2]";

    private static final String STATUS_LABEL = "Status";
    private static final String ELAPSED_LABEL = "Elapsed";
    private static final String STOP_TRAINING_BTN_LABEL = "Stop Training";
    private static final String SHARE_SUPPORT_BTN_LABEL = "Share with support";

    public void experimentViewCheckExperimentHeader(String slot, String header, String status, boolean stopTrainingBtn, boolean shareWithSpBtn) {
        assertThat(getDriver().findElement(By.xpath(String.format(titleLabelXpath, slot))).getText(), is(header));
        assertThat(getDriver().findElement(By.xpath(String.format(statusLabelXpath, slot))).getText(), is(STATUS_LABEL));
        assertThat(getDriver().findElement(By.xpath(String.format(statusXpath, slot))).getText(), is(status));
        assertThat(getDriver().findElement(By.xpath(String.format(elapsedXpath, slot))).getText(), is(ELAPSED_LABEL));

        if (stopTrainingBtn) {
            assertThat(getDriver().findElements(By.xpath(String.format(stopTrainingBtnXpath, slot))).size(), is(not(0)));
            assertThat(getDriver().findElement(By.xpath(String.format(stopTrainingBtnXpath, slot))).getText(), is(STOP_TRAINING_BTN_LABEL));
        }
        if (shareWithSpBtn) {
            assertThat(getDriver().findElements(By.xpath(String.format(shareSupportBtnXpath, slot))).size(), is(not(0)));
            assertThat(getDriver().findElement(By.xpath(String.format(shareSupportBtnXpath, slot))).getText(), is(SHARE_SUPPORT_BTN_LABEL));
        }
    }
}
