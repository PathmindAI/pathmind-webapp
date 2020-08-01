package io.skymind.pathmind.bddtests.page;

import io.skymind.pathmind.bddtests.Utils;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DefaultUrl("page:home.page")
public class HelpPage extends PageObject {

    private Utils utils;

    public void checkConvertingModelsToSupportTuplesPageElements() {
        waitABit(5000);
        assertThat(getDriver().getTitle(), is("Converting models to support Tuples | Pathmind Knowledge Base"));

    }
}
