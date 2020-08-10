package io.skymind.pathmind.webapp.ui.components.archive;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.shared.data.ArchivableData;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;

/**
 * Button is used because if we directly use the Icon then when we click on the icon it also results in
 * the grid's row selection listener being called which is not what we want.
 */
public class ArchivesButton<T extends ArchivableData> extends Button
{
	public ArchivesButton(Grid<T> grid, T data, Function<Boolean, List<T>> getFilteredData, BiConsumer<T, Boolean> archiveDAO)
	{
		super();

		String entityName = data.getClass().getSimpleName().toLowerCase();

		setupButton(data);

		addClickListener(click -> {
			if (data.isArchived()) {
				ConfirmationUtils.unarchive(entityName, () -> changeArchiveStatus(grid, data, getFilteredData, archiveDAO));
			} else {
				ConfirmationUtils.archive(entityName, () -> changeArchiveStatus(grid, data, getFilteredData, archiveDAO));
			}
		});
	}

	private void setupButton(ArchivableData data) {
		Icon archiveIcon = data.isArchived() ? VaadinIcon.ARROW_BACKWARD.create() : VaadinIcon.ARCHIVE.create();
		setIcon(archiveIcon);
		addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		setClassName("action-button");
        getElement().setAttribute("title", data.isArchived() ? "Unarchive" : "Archive");
	}

	// Weird looking logic but it's so that we stay on the same page once you reverse the archive value. We also
	// need to set the items here so that the item is removed from the table.
	private void changeArchiveStatus(Grid<T> grid, T data, Function<Boolean, List<T>> getFilteredData, BiConsumer<T, Boolean> archiveDAO) {
		archiveDAO.accept(data, !data.isArchived());
		grid.setItems(getFilteredData.apply(!data.isArchived()));
	}
}
