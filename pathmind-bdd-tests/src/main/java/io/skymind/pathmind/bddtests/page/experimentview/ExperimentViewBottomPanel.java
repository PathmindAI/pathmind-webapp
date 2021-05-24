package io.skymind.pathmind.bddtests.page.experimentview;

import io.skymind.pathmind.bddtests.Utils;
import io.skymind.pathmind.bddtests.page.GenericPage;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@DefaultUrl("page:home.page")
public class ExperimentViewBottomPanel extends PageObject {

    private GenericPage genericPage;
    private Utils utils;

    private String learningProgressFieldXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='row-2-of-3']";
    private String notesFieldXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::notes-field";
    private String learningProgressLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='row-2-of-3']/span";
    private String learningProgressTabOneLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='row-2-of-3']/descendant::vaadin-tab[1]";
    private String learningProgressTabTwoLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='row-2-of-3']/descendant::vaadin-tab[2]";
    private String learningProgressTabThreeLabelXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='row-2-of-3']/descendant::vaadin-tab[3]";
    private String rewardFunctionXpath = "//vaadin-vertical-layout[@slot='%s']/descendant::*[@class='%s']/descendant::code-viewer";

    private static final String LEARNING_PROGRESS_LABEL = "Learning Progress";
    private static final String LEARNING_PROGRESS_TAB_ONE_LABEL = "Metrics";
    private static final String LEARNING_PROGRESS_TAB_TWO_LABEL = "Histogram";
    private static final String LEARNING_PROGRESS_TAB_THREE_LABEL = "Mean Reward Score";
    private static final String NOTES_FIELD = "Notes";

    public void experimentPageCheckBottomPanel(String slot) {
        assertThat(getDriver().findElements(By.xpath(String.format(learningProgressFieldXpath, slot))).size(), is(not(0)));
        assertThat(getDriver().findElements(By.xpath(String.format(notesFieldXpath, slot))).size(), is(not(0)));
        genericPage.checkElement(true, String.format(learningProgressLabelXpath, slot), LEARNING_PROGRESS_LABEL);
        genericPage.checkElement(true, String.format(learningProgressTabOneLabelXpath, slot), LEARNING_PROGRESS_TAB_ONE_LABEL);
        genericPage.checkElement(true, String.format(learningProgressTabTwoLabelXpath, slot), LEARNING_PROGRESS_TAB_TWO_LABEL);
        genericPage.checkElement(true, String.format(learningProgressTabThreeLabelXpath, slot), LEARNING_PROGRESS_TAB_THREE_LABEL);

        WebElement notesShadow = utils.expandRootElement(getDriver().findElement(By.xpath(String.format(notesFieldXpath, slot))));
        assertThat(notesShadow.findElement(By.cssSelector(".title")).getText(), is(NOTES_FIELD));
    }

    public void experimentPageCheckRewardFunction(String slot, String rewardFunctionFilePath) throws IOException {
        assertThat(getDriver().findElement(By.xpath(String.format(rewardFunctionXpath, slot, genericPage.definePanel(slot)))).getText(), is(FileUtils.readFileToString(new File("models/" + rewardFunctionFilePath), StandardCharsets.UTF_8).replaceAll("\\r\\n", "")));
    }
}
