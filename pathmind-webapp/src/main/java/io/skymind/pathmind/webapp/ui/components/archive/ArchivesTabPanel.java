package io.skymind.pathmind.webapp.ui.components.archive;

import java.util.function.BiConsumer;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;

import io.skymind.pathmind.shared.data.ArchivableData;
import io.skymind.pathmind.webapp.ui.components.TabPanel;

/**
 * If this class is used within a view with a grid then it will automatically add the Archived column in the grid
 * and give you the ability to flip back and forth between the archived and unarchived views.
 * It should be used with a custom data provider per grid because filtering should be done at the query level instead of frontend.
 */
public class ArchivesTabPanel<T extends ArchivableData> extends TabPanel {
    private static final String ARCHIVES_TAB = "Archives";

    private Grid<T> grid;
    private BiConsumer<T, Boolean> archiveDAO;

    public ArchivesTabPanel(String tabName, Grid<T> grid, BiConsumer<T, Boolean> archiveDAO) {
        super(tabName, ARCHIVES_TAB);

        this.grid = grid;
        this.archiveDAO = archiveDAO;

        setAlignItems(Alignment.START);

        Column<T> archiveColumn = grid.addComponentColumn(data -> getArchivesButton(data)).setHeader("Archive").setSortable(false).setFlexGrow(0);

        addTabClickListener(name -> {
            if (archiveColumn != null) {
                archiveColumn.setHeader(ARCHIVES_TAB.equals(name) ? "Unarchive" : "Archive");
            }
        });
    }

    /**
     * This is public because, in case the action column is not created
     * automatically, the parent can still ask ArchievesTabPanel to create the
     * archivesButton. Since it has all the information already.
     */
    public ArchivesButton<T> getArchivesButton(T data) {
        return new ArchivesButton<T>(grid, data, archiveDAO);
    }

    public String getArchivesTabName() {
        return ARCHIVES_TAB;
    }
}
