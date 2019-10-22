package io.skymind.pathmind.ui.components.archive;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.skymind.pathmind.data.ArchivableData;
import io.skymind.pathmind.ui.utils.ExceptionWrapperUtils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Button is used because if we directly use the Icon then when we click on the icon it also results in
 * the grid's row selection listener being called which is not what we want.
 */
public class ArchivesButton<T> extends Button
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
		Icon archiveIcon = new Icon(data.isArchived() ? VaadinIcon.ARROW_BACKWARD : VaadinIcon.ARCHIVE);
		archiveIcon.setSize("32px");
		setIcon(archiveIcon);
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	}

	// Weird looking logic but it's so that we stay on the same page once you reverse the archive value. We also
	// need to set the items here so that the item is removed from the table.
	private void changeArchiveStatus(Grid<T> grid, ArchivableData data, Function<Boolean, List<T>> getFilteredData, BiConsumer<Long, Boolean> archiveDAO) {
		ExceptionWrapperUtils.handleButtonClicked(() -> {
			archiveDAO.accept(data.getId(), !data.isArchived());
			data.setArchived(!data.isArchived());
			grid.setItems(getFilteredData.apply(!data.isArchived()));
		});
	}
}
