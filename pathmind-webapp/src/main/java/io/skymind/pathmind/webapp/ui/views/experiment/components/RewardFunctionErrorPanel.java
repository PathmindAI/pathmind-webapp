package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;

import java.util.List;

public class RewardFunctionErrorPanel extends Div {

    private Div errorMessageWrapper = new Div();

    public RewardFunctionErrorPanel() {
        super();
        initErrorMessageWrapper();
        add(LabelFactory.createLabel("Errors", CssPathmindStyles.BOLD_LABEL), errorMessageWrapper);
        addClassName("errors-wrapper");
    }

    private void initErrorMessageWrapper() {
        errorMessageWrapper = new Div();
        errorMessageWrapper.addClassName("error-message-wrapper");
    }

    public void showErrors(List<String> errors) {
        clearErrorMessages();
        setMessage(errors);
        setClassName(errors);
    }

    private void setClassName(List<String> errors) {
        errorMessageWrapper.removeClassNames("hasError", "noError");
        errorMessageWrapper.addClassName((errors.size() == 0) ? "noError" : "hasError");
    }

    private void clearErrorMessages() {
        errorMessageWrapper.removeAll();
    }

    private void setMessage(List<String> errors) {
        if ((errors.size() == 0)) {
            errorMessageWrapper.add(new Icon(VaadinIcon.CHECK), new Span("No Errors"));
        } else {
            errorMessageWrapper.setText(String.join("\n", errors));
        }
    }
}
