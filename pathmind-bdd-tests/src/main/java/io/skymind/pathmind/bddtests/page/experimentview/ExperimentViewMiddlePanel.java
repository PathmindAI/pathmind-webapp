package io.skymind.pathmind.bddtests.page.experimentview;

import io.skymind.pathmind.bddtests.page.GenericPage;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@DefaultUrl("page:home.page")
public class ExperimentViewMiddlePanel extends PageObject {

    private GenericPage genericPage;

    private String middlePanelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']";
    private String simMetricsLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout[@slot='primary']/span";
    private String simMetricsHeaderRowXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout[@slot='primary']/descendant::*[@class='header-row']";
    private String observationsLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout[@slot='secondary']/span";
    private String rewardFunctionLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout[9]/span";

    private static final String SIM_METRICS_LABEL = "Simulation Metrics";
    private static final String SIM_METRICS_HEADER_ROW = "Variable Name";
    private static final String OBSERVATIONS_LABEL = "Observations";
    private static final String REWARD_FUNCTION_LABEL = "Reward Function";

    public void experimentPageCheckMiddlePanel(String slot) {
        String panel = (slot.equals("primary")) ? "middle-panel" : "comparison-panel";
        assertThat(getDriver().findElements(By.xpath(String.format(middlePanelXpath, slot, panel))).size(), is(not(0)));
        genericPage.checkElement(true, String.format(simMetricsLabelXpath, slot, panel), SIM_METRICS_LABEL);
        genericPage.checkElement(true, String.format(simMetricsHeaderRowXpath, slot, panel), SIM_METRICS_HEADER_ROW);
        genericPage.checkElement(true, String.format(observationsLabelXpath, slot, panel), OBSERVATIONS_LABEL);
        genericPage.checkElement(true, String.format(rewardFunctionLabelXpath, slot, panel), REWARD_FUNCTION_LABEL);
    }
}
