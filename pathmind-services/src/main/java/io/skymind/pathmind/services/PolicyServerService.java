package io.skymind.pathmind.services;

public interface PolicyServerService {
    String createOutputYaml(long modelId);

    void saveOutputYamlFile(long modelId, byte[] outputYaml);

    byte[] getOutputYamlFile(long modelId);

    String createSchemaYaml(long modelId);

    void saveSchemaYamlFile(long modelId, byte[] schemaYaml);

    byte[] getSchemaYamlFile(long modelId);
}
