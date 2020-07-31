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
        assertThat(getDriver().findElement(By.xpath("//article/p[1]/b")).getText(), is("1. Install the latest version of Pathmind Helper in AnyLogic. (version >= 1.0.1)"));
        assertThat(getDriver().findElement(By.xpath("//article/p[3]/b")).getText(), is("2. Set \"Action Outputs\" to \"1\" in your Pathmind Helper properties."));
        assertThat(getDriver().findElement(By.xpath("//article/p[5]/b")).getText(), is(". Update your doAction() function. Instead of accepting an int, it now takes long[]."));
        assertThat(getDriver().findElement(By.xpath("(//article/p)[7]")).getText(), is("From there, you'll need to update your function body. For example:"));
        assertThat(getDriver().findElement(By.xpath("(//article/p)[9]")).getText(), is("Before:"));
        assertThat(getDriver().findElement(By.xpath("(//article/pre)[1]")).getText(), is("doAction(action) { \n" +
            "  if (action == 0) { \n" +
            "    foo();\n" +
            "  } else if (action == 1) {\n" +
            "    bar(); \n" +
            "  } \n" +
            "}"));
        assertThat(getDriver().findElement(By.xpath("(//article/p)[10]")).getText(), is("After:"));
        assertThat(getDriver().findElement(By.xpath("(//article/pre)[2]")).getText(), is("doAction(action) { \n" +
            "  if (action[0] == 0) { // Grab first index in actions array \n" +
            "    foo(); \n" +
            "  } else if (action[0] == 1) { // Grab first index in actions array\n" +
            "    bar(); \n" +
            "  } \n" +
            "}"));
        assertThat(getDriver().findElement(By.xpath("(//article/p)[11]")).getText(), is("If your action must be an integer, you can type cast it."));
        assertThat(getDriver().findElement(By.xpath("(//article/pre)[3]")).getText(), is("doAction(action) { \n" +
            "  if ((int)action[0] == 0) { // Type cast action to int \n" +
            "    foo(); \n" +
            "  } else if ((int)action[0] == 1) { // Type cast action to int \n" +
            "    bar(); \n" +
            "  } \n" +
            "}"));
        assertThat(getDriver().findElement(By.xpath("//article/p[13]/b")).getText(), is("4. Run your simulation using random actions to confirm that everything is working."));
        assertThat(getDriver().findElement(By.xpath("(//article/p)[15]")).getText(), is("The output in debug mode should look like the below."));
    }
}
