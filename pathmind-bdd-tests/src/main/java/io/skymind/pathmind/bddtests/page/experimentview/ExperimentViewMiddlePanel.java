package io.skymind.pathmind.bddtests.page.experimentview;

import io.skymind.pathmind.bddtests.page.GenericPage;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class ExperimentViewMiddlePanel extends PageObject {

    private GenericPage genericPage;

    private String middlePanelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']";
    private String simMetricsLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout[@slot='primary']/descendant::span";
    private String simMetricsHeaderRowXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout[@slot='primary']/descendant::*[@class='header-row']";
    private String observationsLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout[@slot='secondary']/descendant::span";
    private String rewardFunctionLabelXpath = "(//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-vertical-layout/descendant::span[@class='bold-label'])[3]";
    private String rewardVariablesListXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::*[@class='reward-variable-name']";
    private String simulationMetricChosenXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::*[@class='reward-variable-name' and text()='%s' and @chosen]";
    private String simulationMetricNotChosenXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::*[@class='reward-variable-name' and text()='%s' and not(@chosen)]";
    private String observationCheckedXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-checkbox[not(@hidden) and text()='%s' and @aria-checked='true']";
    private String observationNotCheckedXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-checkbox[not(@hidden) and text()='%s' and @aria-checked='false']";
    private String observationXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::vaadin-checkbox[not(@hidden) and text()='%s']";
    private String rewardVariableXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::span[@class='reward-variable-name' and text()='%s']";
    private String rewardVariableCheckedXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::span[@class='reward-variable-name' and text()='%s' and @chosen]";
    private String rewardVariableNotCheckedXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::span[@class='reward-variable-name' and text()='%s' and not(@chosen)]";


    private static final String SIM_METRICS_LABEL = "Simulation Metrics";
    private static final String SIM_METRICS_HEADER_ROW = "Metric";
    private static final String OBSERVATIONS_LABEL = "Observations";
    private static final String REWARD_FUNCTION_LABEL = "Reward Function";

    public void experimentPageCheckMiddlePanel(String slot) {
        String panel = genericPage.definePanel(slot);
        assertThat(getDriver().findElements(By.xpath(String.format(middlePanelXpath, slot, panel))).size(), is(not(0)));
        genericPage.checkElement(true, String.format(simMetricsLabelXpath, slot, panel), SIM_METRICS_LABEL);
        genericPage.checkElement(true, String.format(simMetricsHeaderRowXpath, slot, panel), SIM_METRICS_HEADER_ROW);
        genericPage.checkElement(true, String.format(observationsLabelXpath, slot, panel), OBSERVATIONS_LABEL);
        genericPage.checkElement(true, String.format(rewardFunctionLabelXpath, slot, panel), REWARD_FUNCTION_LABEL);
    }

    public void experimentPageCheckSimulationMetrics(String slot, String commaSeparatedMetrics) {
        List<String> items = Arrays.asList(commaSeparatedMetrics.split("\\s*,\\s*"));
        List<String> actual = new ArrayList<>();
        for (WebElement webElement : getDriver().findElements(By.xpath(String.format(rewardVariablesListXpath, slot, genericPage.definePanel(slot))))) {
            actual.add(webElement.getText());
        }
        assertThat(actual, containsInRelativeOrder(items.toArray()));
    }

    public void experimentPageCheckSimulationMetricIsChosen(String slot, String rewardVar, boolean chosen) {
        if (chosen) {
            genericPage.checkElement(true, String.format(simulationMetricChosenXpath, slot, genericPage.definePanel(slot), rewardVar), rewardVar);
        } else {
            genericPage.checkElement(true, String.format(simulationMetricNotChosenXpath, slot, genericPage.definePanel(slot), rewardVar), rewardVar);
        }
    }

    public void experimentPageCheckObservationIsChecked(String slot, String observation, boolean checked) {
        if (checked) {
            genericPage.checkElement(true, String.format(observationCheckedXpath, slot, genericPage.definePanel(slot), observation), observation);
        } else {
            genericPage.checkElement(true, String.format(observationNotCheckedXpath, slot, genericPage.definePanel(slot), observation), observation);
        }
    }

    public void experimentPageSlotClickRewardVariable(String slot, String rewardVar) {
        getDriver().findElement(By.xpath(String.format(rewardVariableXpath, slot, genericPage.definePanel(slot), rewardVar))).click();
    }

    public void experimentPageSlotCheckRewardVariableIsChosen(String slot, String rewardVar, boolean chosen) {
        if (chosen) {
            genericPage.checkElement(true, String.format(rewardVariableCheckedXpath, slot, genericPage.definePanel(slot), rewardVar), rewardVar);
        } else {
            genericPage.checkElement(true, String.format(rewardVariableNotCheckedXpath, slot, genericPage.definePanel(slot), rewardVar), rewardVar);
        }
    }

    public void experimentPageCheckObservationIsHighlighted(String slot, String observation, boolean highlighted) {
        if (highlighted) {
            assertThat(getDriver().findElement(By.xpath(String.format(observationXpath, slot, genericPage.definePanel(slot), observation))).getAttribute("class"), is("highlight-label"));
        } else {
            assertThat(getDriver().findElement(By.xpath(String.format(observationXpath, slot, genericPage.definePanel(slot), observation))).getAttribute("class"), is(""));
        }
    }
}
