package io.skymind.pathmind.ui.components.archive;

import com.vaadin.flow.component.grid.Grid;
import io.skymind.pathmind.data.ArchivableData;
import io.skymind.pathmind.ui.components.TabPanel;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * If this class is used within a view with a grid then it will automatically add the Archived column in the grid
 * and give you the ability to flip back and forth between the archived and unarchived views as well as take care
 * of filtering out the archived items.
 */
public class ArchivesTabPanel<T> extends TabPanel
{
	private static final String ARCHIVES_TAB = "Archives";

	// These need to be here for the same reason as the init() method.
	private Grid<T> grid;
	private Supplier<List<T>> getItems;

	/**
	 * Because there is no ability to search for a column by the header's name (https://vaadin.com/forum/thread/17861491) I then
	 * use the next best thing which is that all our archives columns are the last column on the grid and so get the last column
	 * and adjust that column header's name.
	 */
	public ArchivesTabPanel(String tabName, Grid<T> grid, Supplier<List<T>> getItems, BiConsumer<Long, Boolean> archiveDAO)
	{
		super(tabName, ARCHIVES_TAB);

		this.grid = grid;
		this.getItems = getItems;

		setAlignItems(Alignment.START);

		// Add archive column to grid as the last column.
		grid.addComponentColumn(data -> gettArchivesButton(grid, getItems, (ArchivableData) data, archiveDAO))
				.setHeader("Archive")
				.setSortable(false);

		addTabClickListener(name -> {
			updateArchiveColumnHeader(grid, ARCHIVES_TAB.equals(name) ? "Unarchive" : "Archive");
			grid.setItems(getFilteredModels(getItems.get(), ARCHIVES_TAB.equals(name)));
		});
	}

	private ArchivesButton<T> gettArchivesButton(Grid<T> grid, Supplier<List<T>> getItems, ArchivableData data, BiConsumer<Long, Boolean> archiveDAO) {
		return new ArchivesButton<T>(grid, data, isArchived -> getFilteredModels(getItems.get(), isArchived), archiveDAO);
	}

	private void updateArchiveColumnHeader(Grid<T> grid, String header) {
		grid.getColumns().get(grid.getColumns().size()-1).setHeader(header);
	}

	private List<T> getFilteredModels(List<T> data, boolean isArchived) {
		return data.stream()
				.filter(d -> ((ArchivableData)d).isArchived() == isArchived)
				.collect(Collectors.toList());
	}

	/**
	 * This needs to be called because there is are no listeners for the grid to know if grid.setItems() has been called.
	 */
	public void initData() {
		grid.setItems(getFilteredModels(getItems.get(), false));
	}
}
