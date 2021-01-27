package io.skymind.pathmind.webapp.ui.components.archive;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import io.skymind.pathmind.shared.data.ArchivableData;
import io.skymind.pathmind.webapp.ui.components.TabPanel;
import io.skymind.pathmind.webapp.utils.VaadinDateAndTimeUtils;

/**
 * If this class is used within a view with a grid then it will automatically add the Archived column in the grid
 * and give you the ability to flip back and forth between the archived and unarchived views as well as take care
 * of filtering out the archived items.
 */
public class ArchivesTabPanel<T extends ArchivableData> extends TabPanel {
    private static final String ARCHIVES_TAB = "Archives";

    // These need to be here for the same reason as the init() method.
    private Grid<T> grid;
    private Supplier<List<T>> getItems;
    private BiConsumer<T, Boolean> archiveDAO;
    private Supplier<Optional<UI>> getUISupplier;
    public ArchivesTabPanel(String tabName, Grid<T> grid, Supplier<List<T>> getItems, BiConsumer<T, Boolean> archiveDAO, Supplier<Optional<UI>> getUISupplier) {
        this(tabName, true, grid, getItems, archiveDAO, getUISupplier);
    }

    /**
     * By default ArchivesTabPanel creates an component column, you can set
     * isAutoAppendColumn to false, in order to disable this feature
     */
    public ArchivesTabPanel(String tabName, boolean isAutoCreateActionColumn, Grid<T> grid, Supplier<List<T>> getItems, BiConsumer<T, Boolean> archiveDAO, Supplier<Optional<UI>> getUISupplier) {
        super(tabName, ARCHIVES_TAB);

        this.grid = grid;
        this.getItems = getItems;
        this.archiveDAO = archiveDAO;
        this.getUISupplier = getUISupplier;

        setAlignItems(Alignment.START);

        Column<T> archiveColumn = isAutoCreateActionColumn
                ? grid.addComponentColumn(data -> getArchivesButton(data)).setHeader("Archive").setSortable(false).setWidth("120px").setFlexGrow(0)
                : null;

        addTabClickListener(name -> {
            if (archiveColumn != null) {
                updateArchiveColumnHeader(archiveColumn, ARCHIVES_TAB.equals(name) ? "Unarchive" : "Archive");
            }
            grid.setItems(getFilteredModels(getItems.get(), ARCHIVES_TAB.equals(name)));
        });
    }

    /**
     * This is public because, in case the action column is not created
     * automatically, the parent can still ask ArchievesTabPanel to create the
     * archivesButton. Since it has all the information already.
     */
    public ArchivesButton<T> getArchivesButton(T data) {
        return new ArchivesButton<T>(grid, data, isArchived -> getFilteredModels(getItems.get(), isArchived), archiveDAO);
    }

    private void updateArchiveColumnHeader(Column<T> archiveColumn, String header) {
        archiveColumn.setHeader(header);
    }

    private List<T> getFilteredModels(List<T> data, boolean isArchived) {
        return data.stream()
                .filter(d -> ((ArchivableData) d).isArchived() == isArchived)
                .collect(Collectors.toList());
    }

    /**
     * This needs to be called because there is are no listeners for the grid to know if grid.setItems() has been called.
     */
    public void initData() {
        VaadinDateAndTimeUtils.withUserTimeZoneId(getUISupplier, timeZoneId -> {
            // Grid column renderers might be using timeZone to format dates and times. Making sure here that timezone is loaded properly
            grid.setItems(getFilteredModels(getItems.get(), false));
        });
    }
}
