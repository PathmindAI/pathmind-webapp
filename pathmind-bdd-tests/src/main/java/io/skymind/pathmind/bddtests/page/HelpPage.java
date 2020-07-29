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
        assertThat(getDriver().findElement(By.xpath("//h1")).getText(), is("Converting models to support Tuples"));
        assertThat(getDriver().findElement(By.xpath("//article/p/b")).getText(), is("This only applies to models with a single action output. aka, non-tuple"));
        assertThat(getDriver().findElement(By.xpath("(//article/ol/li)[1]")).getText(), is("Upgrade to the latest version of Pathmind Helper in AnyLogic. (version >= 1.1.0)"));
        assertThat(getDriver().findElement(By.xpath("(//article/ol/li)[2]")).getText(), is("Update their doAction() function. Instead of accepting an int, it now takes long[]. A simple way to change this is :"));
        assertThat(getDriver().findElement(By.xpath("(//article/p)[3]")).getText(), is("Before:"));
        assertThat(getDriver().findElement(By.xpath("(//article/p)[4]")).getText(), is("After:"));
        assertThat(getDriver().findElement(By.xpath("(//article/pre)[1]")).getText(), is("doAction(action) { \n" +
            "  if (action == 0) { \n" +
            "    foo();\n" +
            "  } else if (action == 1) {\n" +
            "    bar(); \n" +
            "  } \n" +
            "}"));
        assertThat(getDriver().findElement(By.xpath("(//article/pre)[2]")).getText(), is("doAction(action) { \n" +
            "  if (action[0] == 0) { // Grab first index in actions array \n" +
            "    foo(); \n" +
            "  } else if (action[0] == 1) { // Grab first index in actions array\n" +
            "    bar(); \n" +
            "  } \n" +
            "}"));
    }
}
