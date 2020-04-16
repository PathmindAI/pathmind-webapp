package pathmind.steps;

import net.thucydides.core.annotations.Step;
import pathmind.page.NotFoundPage;

public class NotFoundPageSteps {
    private NotFoundPage notFoundPage;

    @Step
    public void openIncorrectPathPage() {
        notFoundPage.openIncorrectPathPage();
    }

    @Step
    public void check404PageOpened(){
        notFoundPage.check404PageOpened();
    }
}
