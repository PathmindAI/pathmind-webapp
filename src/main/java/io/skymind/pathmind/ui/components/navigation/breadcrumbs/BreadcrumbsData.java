package io.skymind.pathmind.ui.components.navigation.breadcrumbs;

public class BreadcrumbsData {
  private String projectName;
  private long projectId;
  private String modelNumber;
  private long modelId;
  private String experimentNumber;
  private String experimentPageParameter;

  public BreadcrumbsData() {
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public long getProjectId() {
    return projectId;
  }

  public void setProjectId(long projectId) {
    this.projectId = projectId;
  }

  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public long getModelId() {
    return modelId;
  }

  public void setModelId(long modelId) {
    this.modelId = modelId;
  }

  public String getExperimentNumber() {
    return experimentNumber;
  }

  public void setExperimentNumber(String experimentNumber) {
    this.experimentNumber = experimentNumber;
  }

  public String getExperimentPageParameter() {
    return experimentPageParameter;
  }

  public void setExperimentPageParameter(String experimentPageParameter) {
    this.experimentPageParameter = experimentPageParameter;
  }
}