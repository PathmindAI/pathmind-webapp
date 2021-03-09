package io.skymind.pathmind.webapp.ui.views.experiment.actions.experiment;

import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.components.atoms.ButtonWithLoading;
import io.skymind.pathmind.webapp.ui.components.molecules.CopyField;

public class ServePolicyAction {

    public static void servePolicy(Supplier<Experiment> getExperimentSupplier, ButtonWithLoading buttonWithLoading) {
        long experimentId = getExperimentSupplier.get().getId();
        Dialog dialog = new Dialog();
        Div dialogContent = new Div();
        Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClickListener(event -> {
            buttonWithLoading.setLoading(false);
            buttonWithLoading.setDisabled(false);
            dialog.close();
        });
        dialogContent.add(
            new H3("Policy Serving"),
            new Paragraph("This is the serving endpoint for the experiment's best policy."),
            new CopyField("https://app.pathmind.com/servepolicy/"+experimentId), // TODO -> use actual url
            new Paragraph(new Span("Read the "),
                    new Anchor("link", "documentation"), // TODO -> use actual url
                    new Span(" to learn more about its usage."))
        );
        dialogContent.addClassName("serve-policy-instructions");
        dialog.add(dialogContent, closeButton);
        dialog.open();
    }
}
