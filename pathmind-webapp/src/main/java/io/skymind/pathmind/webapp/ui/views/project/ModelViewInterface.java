package io.skymind.pathmind.webapp.ui.views.project;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;

public interface ModelViewInterface {
    
    Supplier<Optional<UI>> getUISupplier();

}
