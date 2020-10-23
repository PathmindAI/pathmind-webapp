package io.skymind.pathmind.webapp.ui.components.policy;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;

public class ExportPolicyButton extends Anchor {

    private Button exportButton;

    private PolicyFileService policyFileService;

    private Supplier<Policy> getPolicySupplier;
    private String policyFilename;

    public ExportPolicyButton(SegmentIntegrator segmentIntegrator, PolicyFileService policyFileService, PolicyDAO policyDAO, Supplier<Policy> getPolicySupplier) {
        super();
        this.policyFileService = policyFileService;
        this.getPolicySupplier = getPolicySupplier;

        policyFilename = PolicyUtils.generatePolicyFileName(getPolicySupplier.get());

        exportButton = new Button("Export Policy");
        exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportButton.setWidth("200px");
        exportButton.addClickListener(evt -> {
            policyDAO.updateExportedDate(getPolicySupplier.get().getId());
            segmentIntegrator.policyExported();
        });

        add(exportButton);
        getElement().setAttribute("href", getResourceStream(policyFilename));
        getElement().setAttribute("download", true);
    }

    public String getPolicyFilename() {
        return policyFilename;
    }

    public Button getExportButton() {
        return exportButton;
    }

    private StreamResource getResourceStream(String filename) {
        return new StreamResource(removeInvalidChars(filename),
                () -> new ByteArrayInputStream(policyFileService.getPolicyFile(getPolicySupplier.get().getId())));
    }

}
