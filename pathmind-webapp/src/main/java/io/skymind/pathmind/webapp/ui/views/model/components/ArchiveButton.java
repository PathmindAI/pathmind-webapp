package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.Command;

public class ArchiveButton extends Button {

    private Boolean isArchived;

    public ArchiveButton(Boolean isArchived, Command clickListener) {
        super();
        this.isArchived = isArchived;
        updateUIstatus();
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		setClassName("action-button");
        addClickListener(clickEvent -> {
            this.isArchived = !this.isArchived;
            updateUIstatus();
            clickListener.execute();
        });
    }

    private void updateUIstatus() {
        Icon archiveIcon = isArchived ? VaadinIcon.ARROW_BACKWARD.create() : VaadinIcon.ARCHIVE.create();
		setIcon(archiveIcon);
        getElement().setAttribute("title", isArchived ? "Unarchive" : "Archive");
    }
}