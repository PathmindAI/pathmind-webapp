package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

public class Job {
   @NotNull
   private String name;
   @NotNull
   private List<JobAnalysis> jobanalyses;
   @Nullable
   private String id;
   @Nullable
   private String paramFile;
   @Nullable
   private String caseFile;
   @Nullable
   private List resourceFilters;
   @NotNull
   private List jobvariables;
   private boolean isTemplateDryRun;
   private boolean includeNominalRun;
   @Nullable
   private Integer monteCarloIterations;

   // for deserialization
   private Job(){}

   public Job(@NotNull String name, @NotNull List<JobAnalysis> jobanalyses, @Nullable String id, @Nullable String paramFile, @Nullable String caseFile, @Nullable List resourceFilters, @NotNull List jobvariables, boolean isTemplateDryRun, boolean includeNominalRun, @Nullable Integer monteCarloIterations) {
      this.name = name;
      this.jobanalyses = jobanalyses;
      this.id = id;
      this.paramFile = paramFile;
      this.caseFile = caseFile;
      this.resourceFilters = resourceFilters;
      this.jobvariables = jobvariables;
      this.isTemplateDryRun = isTemplateDryRun;
      this.includeNominalRun = includeNominalRun;
      this.monteCarloIterations = monteCarloIterations;
   }

   public static Job create(@NotNull String name, JobAnalysis analysis){
      return new Job(
              name,
              Collections.singletonList(analysis),
              null,
              null,
              null,
              null,
              Collections.emptyList(),
              false,
              false,
              null);
   }

   @NotNull
   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   @NotNull
   public List getJobanalyses() {
      return this.jobanalyses;
   }

   public void setJobanalyses(List<JobAnalysis> jobanalyses) {
      this.jobanalyses = jobanalyses;
   }

   @Nullable
   public String getId() {
      return this.id;
   }

   public void setId(@Nullable String id) {
      this.id = id;
   }

   @Nullable
   public String getParamFile() {
      return this.paramFile;
   }

   public void setParamFile(@Nullable String paramFile) {
      this.paramFile = paramFile;
   }

   @Nullable
   public String getCaseFile() {
      return this.caseFile;
   }

   public void setCaseFile(@Nullable String caseFile) {
      this.caseFile = caseFile;
   }

   @Nullable
   public List getResourceFilters() {
      return this.resourceFilters;
   }

   public void setResourceFilters(@Nullable List resourceFilters) {
      this.resourceFilters = resourceFilters;
   }

   @NotNull
   public List getJobvariables() {
      return this.jobvariables;
   }

   public void setJobvariables(List jobvariables) {
      this.jobvariables = jobvariables;
   }

   public boolean isTemplateDryRun() {
      return this.isTemplateDryRun;
   }

   public void setTemplateDryRun(boolean templateDryRun) {
      isTemplateDryRun = templateDryRun;
   }

   public boolean getIncludeNominalRun() {
      return this.includeNominalRun;
   }

   public void setIncludeNominalRun(boolean includeNominalRun) {
      this.includeNominalRun = includeNominalRun;
   }

   @Nullable
   public Integer getMonteCarloIterations() {
      return this.monteCarloIterations;
   }

   public void setMonteCarloIterations(@Nullable Integer monteCarloIterations) {
      this.monteCarloIterations = monteCarloIterations;
   }
}