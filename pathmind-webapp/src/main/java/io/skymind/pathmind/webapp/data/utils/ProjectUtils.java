package io.skymind.pathmind.webapp.data.utils;

import java.time.LocalDateTime;

import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.shared.security.SecurityUtils;
import org.apache.commons.lang3.StringUtils;

public class ProjectUtils {
    private ProjectUtils() {
    }

    public static Project generateNewDefaultProject() {
        return generateNewDefaultProject(0, null);
    }

    public static Project generateNewDefaultProject(long id, String name) {
        Project project = new Project();
        project.setDateCreated(LocalDateTime.now());
        project.setLastActivityDate(LocalDateTime.now());
        project.setPathmindUserId(SecurityUtils.getUserId());
        if (id != 0) {
            project.setId(id);
        }
        if (StringUtils.isNotEmpty(name)) {
            project.setName(name);
        }
        return project;
    }
}
