package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyUpdateInfo {

    private byte[] policyFile;

    private String name;

    private String checkpointFileKey;

    private byte[] checkpointFile;

}
