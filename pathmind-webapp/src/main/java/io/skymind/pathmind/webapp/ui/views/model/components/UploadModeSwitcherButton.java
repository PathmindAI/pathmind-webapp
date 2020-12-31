package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.views.model.UploadMode;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;
import io.skymind.pathmind.webapp.ui.views.model.utils.UploadModelViewNavigationUtils;

public class UploadModeSwitcherButton extends Button {
    public UploadModeSwitcherButton(UploadMode mode, Supplier<Optional<UI>> getUISupplier, Model model) {
        setText(mode == UploadMode.FOLDER ? "Upload as Zip" : "Upload Folder");
        addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addClickListener(evt -> {
            UploadMode switchedMode = mode == UploadMode.FOLDER ? UploadMode.ZIP : UploadMode.FOLDER;
//            getUISupplier.get().ifPresent(ui ->
            evt.getSource().getUI().ifPresent(ui ->
                    ui.navigate(UploadModelView.class, UploadModelViewNavigationUtils.getUploadModelParameters(model.getProjectId(), switchedMode)));
        });
    }

}
