package io.skymind.pathmind.db.converter;

import io.skymind.pathmind.shared.services.PolicyServerService;
import org.jooq.impl.EnumConverter;

public class PolicyServerStatusConverter extends EnumConverter<Integer, PolicyServerService.DeploymentStatus> {
    public PolicyServerStatusConverter(Class<Integer> fromType, Class<PolicyServerService.DeploymentStatus> toType) {
        super(fromType, toType);
    }

    @Override
    public Integer to(PolicyServerService.DeploymentStatus deploymentStatus) {
        if (deploymentStatus == null) {
            return null;
        } else {
            return deploymentStatus.code;
        }
    }
}
