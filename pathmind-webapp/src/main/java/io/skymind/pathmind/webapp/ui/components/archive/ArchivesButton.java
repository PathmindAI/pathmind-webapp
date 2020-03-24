package io.skymind.pathmind.webapp.ui.components.archive;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.shared.data.ArchivableData;
import io.skymind.pathmind.webapp.ui.components.PathmindButton;

/**
 * Button is used because if we directly use the Icon then when we click on the icon it also results in
 * the grid's row selection listener being called which is not what we want.
 */
public class ArchivesButton<T> extends PathmindButton
{
	public ArchivesButton(Grid<T> grid, ArchivableData data, Function<Boolean, List<T>> getFilteredData, BiConsumer<Long, Boolean> archiveDAO)
	{
		super();

		String archiveConfirmationText = data.isArchived() ? "Unarchive" : "Archive";

		setupButton(data);

		ConfirmDialog confirmDialog = new ConfirmDialog(
				"Confirm " + archiveConfirmationText,
				"Are you sure you want to " + archiveConfirmationText.toLowerCase() + " this " + data.getClass().getSimpleName() + "?",
				archiveConfirmationText,
				confirmEvent -> changeArchiveStatus(grid, data, getFilteredData, archiveDAO),
				"Cancel",
				cancelEvent -> {});

		addClickListener(click -> confirmDialog.open());
	}

	private void setupButton(ArchivableData data) {
		Icon archiveIcon = data.isArchived() ? VaadinIcon.ARROW_BACKWARD.create() : VaadinIcon.ARCHIVE.create();
		setIcon(archiveIcon);
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		setClassName("action-button");
		setTitle(data.isArchived() ? "Unarchive" : "Archive");
	}

	// Weird looking logic but it's so that we stay on the same page once you reverse the archive value. We also
	// need to set the items here so that the item is removed from the table.
	private void changeArchiveStatus(Grid<T> grid, ArchivableData data, Function<Boolean, List<T>> getFilteredData, BiConsumer<Long, Boolean> archiveDAO) {
		archiveDAO.accept(data.getId(), !data.isArchived());
		data.setArchived(!data.isArchived());
		grid.setItems(getFilteredData.apply(!data.isArchived()));
	}
}
