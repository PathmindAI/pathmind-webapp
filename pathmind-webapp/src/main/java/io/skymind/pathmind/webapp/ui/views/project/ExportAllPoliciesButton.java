package io.skymind.pathmind.webapp.ui.views.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.StreamResource;
import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.services.PolicyFileService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import static io.skymind.pathmind.shared.utils.PathmindStringUtils.removeInvalidChars;
import static io.skymind.pathmind.shared.utils.PathmindStringUtils.toCamelCase;

@Slf4j
public class ExportAllPoliciesButton extends Anchor {

    private final PolicyFileService policyFileService;
    private final ExperimentDAO experimentDAO;

    public ExportAllPoliciesButton(PolicyFileService policyFileService, ExperimentDAO experimentDAO) {
        this.policyFileService = policyFileService;
        this.experimentDAO = experimentDAO;
        Button exportButton = new Button("Export Policies");
        exportButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        exportButton.setIcon(new Icon(VaadinIcon.DOWNLOAD_ALT));
        exportButton.addClassName("download-policies-button");
        add(exportButton);
    }

    private StreamResource getResourceStream(List<Policy> policies) {

        if (CollectionUtils.isEmpty(policies)) {
            return null;
        }

        Policy p0 = policies.get(0);
        String filename = removeInvalidChars(String.format("%s-M%s-%s-Policies.zip", toCamelCase(p0.getProject().getName()), p0.getModel().getName(), p0.getModel().getPackageName()));

        return new StreamResource(removeInvalidChars(filename),
                () -> {

                    ByteArrayInputStream zip = null;

                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         ZipOutputStream zos = new ZipOutputStream(baos)) {

                        List<Pair<String, byte[]>> data = policies.parallelStream()
                                .map(policy -> {
                                    String policyFilename = PolicyUtils.generatePolicyFileName(policy);
                                    policyFilename = removeInvalidChars(policyFilename);
                                    byte[] bytes = policyFileService.getFreezingOrPolicyFile(policy);
                                    return Pair.of(policyFilename, bytes);
                                })
                                .collect(Collectors.toList());


                        for (Pair<String, byte[]> nameBytes : data) {
                            String policyFilename = nameBytes.getLeft();
                            byte[] bytes = nameBytes.getRight();
                            ZipEntry entry = new ZipEntry(policyFilename);
                            entry.setSize(bytes.length);
                            zos.putNextEntry(entry);
                            zos.write(bytes);
                            zos.closeEntry();
                        }

                        zos.close();
                        zip =  new ByteArrayInputStream(baos.toByteArray());

                    } catch (IOException e) {
                        log.error("Failed to compress list of policies for model", e);
                    }

                    return zip;

                });
    }

    public void setModelId(long modelId) {

        List<Policy> bestPolicies = experimentDAO.getExperimentsForModelWithPolicies(modelId, SecurityUtils.getUserId()).stream()
                .filter(Experiment::isTrainingCompleted)
                .map(Experiment::getBestPolicy)
                .filter(Objects::nonNull)
                .filter(Policy::hasFile)
                .collect(Collectors.toList());

        if (bestPolicies.isEmpty()) {
            setVisible(false);
            return;
        }

        getElement().setAttribute("href", getResourceStream(bestPolicies));
        getElement().setAttribute("download", true);

    }
}
