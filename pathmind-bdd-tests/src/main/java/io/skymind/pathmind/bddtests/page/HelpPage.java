package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DefaultUrl("page:home.page")
public class HelpPage extends PageObject {

    private Utils utils;

    public void checkConvertingModelsToSupportTuplesPageElements() {
        waitABit(5000);
        assertThat(getDriver().findElement(By.cssSelector("h1")).getText(), is("Converting models to support Tuples"));

    }
}
