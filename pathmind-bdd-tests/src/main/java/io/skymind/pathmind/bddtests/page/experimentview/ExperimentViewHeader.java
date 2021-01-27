package io.skymind.pathmind.bddtests.page.experimentview;

import io.skymind.pathmind.bddtests.page.GenericPage;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class ExperimentViewHeader extends PageObject {

    private GenericPage genericPage;

    private String titleLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::span[@class='section-title-label']";
    private String statusLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::vaadin-horizontal-layout[@class='training-status-details-panel']/descendant::span[1]";
    private String statusXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::vaadin-horizontal-layout[@class='training-status-details-panel']/descendant::span[2]";
    private String elapsedXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::vaadin-horizontal-layout[@class='training-status-details-panel']/descendant::span[4]";
    private String stopTrainingBtnXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::div[@class='buttons-wrapper']/vaadin-button[1]";
    private String getStopTrainingBtnByTextXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::div[@class='buttons-wrapper']/vaadin-button[text()='Stop Training']";
    private String shareSupportBtnXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::div[@class='buttons-wrapper']/vaadin-button[2]";
    private String shareSupportBtnByTextXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::div[@class='buttons-wrapper']/vaadin-button[text()='Share with support']";
    private String shareSupportLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::tag-label[not(@hidden='true')]";

    private static final String STATUS_LABEL = "Status";
    private static final String ELAPSED_LABEL = "Elapsed";
    private static final String STOP_TRAINING_BTN_LABEL = "Stop Training";
    private static final String SHARE_SUPPORT_BTN_LABEL = "Share with support";
    private static final String SHARE_SUPPORT_LABEL = "Shared with Support";

    public void experimentViewCheckExperimentHeader(String slot, String header, String status, boolean stopTrainingBtn, boolean shareWithSpBtn, boolean shareWithSpLabel) {
        assertThat(getDriver().findElement(By.xpath(String.format(titleLabelXpath, slot))).getText(), is(header));
        assertThat(getDriver().findElement(By.xpath(String.format(statusLabelXpath, slot))).getText(), is(STATUS_LABEL));
        if (status.equals("Starting Cluster")){
            assertThat(getDriver().findElement(By.xpath(String.format(statusXpath, slot))).getText(), anyOf(is(status), is("Running")));
        }else {
            assertThat(getDriver().findElement(By.xpath(String.format(statusXpath, slot))).getText(), anyOf(is(status), is("Stopped")));
        }
        assertThat(getDriver().findElement(By.xpath(String.format(elapsedXpath, slot))).getText(), is(ELAPSED_LABEL));
        if (stopTrainingBtn){
            genericPage.checkElement(stopTrainingBtn, String.format(stopTrainingBtnXpath, slot), STOP_TRAINING_BTN_LABEL);
        } else {
            assertThat(getDriver().findElement(By.xpath(String.format(stopTrainingBtnXpath, slot))).getAttribute("hidden"), is("true"));
        }
        if (shareWithSpBtn){
            genericPage.checkElement(shareWithSpBtn, String.format(shareSupportBtnXpath, slot), SHARE_SUPPORT_BTN_LABEL);
        } else {
            assertThat(getDriver().findElement(By.xpath(String.format(shareSupportBtnXpath, slot))).getAttribute("hidden"), is("true"));
        }
        genericPage.checkElement(shareWithSpLabel, String.format(shareSupportLabelXpath, slot), SHARE_SUPPORT_LABEL);
    }
}
