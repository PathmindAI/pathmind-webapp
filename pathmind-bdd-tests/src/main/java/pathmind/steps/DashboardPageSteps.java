package pathmind.steps;

import net.thucydides.core.annotations.Step;
import pathmind.page.DashboardPage;

public class DashboardPageSteps {

    private DashboardPage dashboardPage;

	@Step
	public void clickProjectFromDashboard(String randomNumber) {
		dashboardPage.clickProjectFromDashboard(randomNumber);
	}
	@Step
	public void clickModelBreadcrumbFromDashboard(String projectName) {
		dashboardPage.clickModelBreadcrumbFromDashboard(projectName);
	}
	@Step
	public void clickExperimentBreadcrumbFromDashboard(String projectName) {
		dashboardPage.clickExperimentBreadcrumbFromDashboard(projectName);
	}
	@Step
	public void checkStageStatus(String projectName, String stage, String stageStatus) {
		dashboardPage.checkStageStatus(projectName, stage, stageStatus);
	}
	@Step
	public void checkExperimentNotesNotExist(String projectName) {
		dashboardPage.checkExperimentNotesNotExist(projectName);
	}
	@Step
	public void checkExperimentNotes(String projectName, String experimentNotes) {
		dashboardPage.checkExperimentNotes(projectName, experimentNotes);
	}
	@Step
	public void clickInNavigationIcon(String projectName) {
		dashboardPage.clickInNavigationIcon(projectName);
	}
	@Step
	public void clickInAutotestProjectStageBreadcrumb(String projectName) {
		dashboardPage.clickInAutotestProjectStageBreadcrumb(projectName);
	}
}
