package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

class Job {
   @NotNull
   private final String name;
   @NotNull
   private final List<JobAnalysis> jobanalyses;
   @Nullable
   private final String id;
   @Nullable
   private final String paramFile;
   @Nullable
   private final String caseFile;
   @Nullable
   private final List resourceFilters;
   @NotNull
   private final List jobvariables;
   private final boolean isTemplateDryRun;
   private final boolean includeNominalRun;
   @Nullable
   private final Integer monteCarloIterations;

   @NotNull
   public final String getName() {
      return this.name;
   }

   @NotNull
   public final List getJobanalyses() {
      return this.jobanalyses;
   }

   @Nullable
   public final String getId() {
      return this.id;
   }

   @Nullable
   public final String getParamFile() {
      return this.paramFile;
   }

   @Nullable
   public final String getCaseFile() {
      return this.caseFile;
   }

   @Nullable
   public final List getResourceFilters() {
      return this.resourceFilters;
   }

   @NotNull
   public final List getJobvariables() {
      return this.jobvariables;
   }

   public final boolean isTemplateDryRun() {
      return this.isTemplateDryRun;
   }

   public final boolean getIncludeNominalRun() {
      return this.includeNominalRun;
   }

   @Nullable
   public final Integer getMonteCarloIterations() {
      return this.monteCarloIterations;
   }

   public Job(@NotNull String name, @NotNull List jobanalyses, @Nullable String id, @Nullable String paramFile, @Nullable String caseFile, @Nullable List resourceFilters, @NotNull List jobvariables, boolean isTemplateDryRun, boolean includeNominalRun, @Nullable Integer monteCarloIterations) {
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
}