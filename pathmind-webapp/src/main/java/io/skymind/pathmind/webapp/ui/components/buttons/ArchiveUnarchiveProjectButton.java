package io.skymind.pathmind.webapp.ui.components.buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import io.skymind.pathmind.db.dao.ProjectDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;

public class ArchiveUnarchiveProjectButton extends Button {

    ProjectDAO projectDAO;
    Project project;
    long projectId;
    SegmentIntegrator segmentIntegrator;

    public ArchiveUnarchiveProjectButton(ProjectDAO projectDAO, SegmentIntegrator segmentIntegrator) {
        super();
        setProject(project);
        if (project != null) {
            setButtonIcon();
            setButtonText();
        }
        this.projectDAO = projectDAO;
        this.segmentIntegrator = segmentIntegrator;

        addClickListener(evt -> {
            String projectName = this.project.getName();
            if (project.isArchived()) {
                ConfirmationUtils.unarchive(projectName, () -> archiveAction());
            } else {
                ConfirmationUtils.archive(projectName, () -> archiveAction());
            }
        });

        addClassName("archive-project-button");
    }

    private void archiveAction() {
        Boolean newArchiveStatus = !project.isArchived();
        projectDAO.archive(this.projectId, newArchiveStatus);
        segmentIntegrator.archived(Model.class, newArchiveStatus);
        project.setArchived(newArchiveStatus);
        setButtonIcon();
        setButtonText();
        if (newArchiveStatus) {
            getUI().ifPresent(ui -> ui.navigate(ProjectsView.class));
        }
    }

    private void setButtonIcon() {
        if (project.isArchived()) {
            setIcon(new Icon(VaadinIcon.ARROW_BACKWARD));
        } else {
            setIcon(new Icon(VaadinIcon.ARCHIVE));
        }
    }

    private void setButtonText() {
        if (project.isArchived()) {
            setText("Unarchive");
        } else {
            setText("Archive");
        }
    }

    public void setProject(Project project) {
        if (project == null) {
            setVisible(false);
            return;
        }
        this.project = project;
        this.projectId = project.getId();
        setVisible(true);
        setButtonIcon();
        setButtonText();
    }

}
