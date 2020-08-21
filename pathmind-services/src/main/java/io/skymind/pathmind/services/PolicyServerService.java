package io.skymind.pathmind.services;

public interface PolicyServerService {
    String createSchemaYaml(long modelId);

    void saveSchemaYamlFile(long modelId, byte[] schemaYaml);

    byte[] getSchemaYamlFile(long modelId);
}
