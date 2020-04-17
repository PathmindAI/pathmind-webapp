package io.skymind.pathmind.bddtests.page;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.util.List;

@DefaultUrl("page:home.page")
public class DashboardPage extends PageObject {

	public void clickProjectFromDashboard(String randomNumber) {
		getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='"+randomNumber+"']")).click();
		waitABit(2500);
	}

	public void clickModelBreadcrumbFromDashboard(String projectName) {
		getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='"+projectName+"']/following-sibling::a[text()='Model #1']")).click();
		waitABit(2500);
	}

	public void clickExperimentBreadcrumbFromDashboard(String projectName) {
		waitFor(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='display: none;']")));
		getDriver().findElement(By.xpath("//a[text()='"+projectName+"']/ancestor::vaadin-grid-cell-content")).click();
		waitABit(2500);
	}

	public void checkStageStatus(String projectName, String stage, String stageStatus) {
		WebElement stageElement = getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='"+projectName+"']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/descendant::span[text()='"+stage+"']"));
		assertThat(stageElement.getAttribute("class"), containsString(stageStatus));
	}

    public void checkExperimentNotesNotExist(String projectName) {
		List<WebElement> notesElementList = getDriver().findElements(By.xpath("//*[@class='breadcrumb' and text()='"+projectName+"']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/following-sibling::*[@class='dashboard-item-notes']/span[text()='Experiment notes']"));
		assertThat(notesElementList.size(), is(0));
    }

	public void checkExperimentNotes(String projectName, String experimentNotes) {
		WebElement notesElement = getDriver().findElement(By.xpath("//*[@class='breadcrumb' and text()='"+projectName+"']/parent::vaadin-horizontal-layout/parent::vaadin-vertical-layout/following-sibling::*[@class='dashboard-item-notes']//p"));
		assertThat(notesElement.getText(), containsString(experimentNotes));
	}

	public void clickInNavigationIcon(String projectName) {
		String xpath = dashboardLineXPathPrefix(projectName, "Model #1") + "//*[@class='navigate-icon']";
		getDriver().findElement(By.xpath(xpath)).click();
		waitABit(2500);
	}

	private String dashboardLineXPathPrefix(String projectName, String modelName) {
		return String.format("//*[@class='breadcrumb' and text()='%s']/following-sibling::span[text()='%s']/ancestor::*[@class='dashboard-line']",
				projectName, modelName);
	}

	public void clickInAutotestProjectStageBreadcrumb(String projectName) {
		String xpath = dashboardLineXPathPrefix(projectName, "Model #1") + "//*[@class='stages-container']";
		getDriver().findElement(By.xpath(xpath)).click();
		waitABit(2500);
	}
}
