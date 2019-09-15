package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

class JobAnalysis {
   @NotNull
   private final String command;
   @NotNull
   private final Analysis analysis;
   @NotNull
   private final Hardware hardware;
   private final boolean useMpi;
   @NotNull
   private final Map<String, String> envVars;
   @NotNull
   private final List<FileReference> inputFiles;
   private final boolean useRescaleLicense;
   @Nullable
   private final List templateTasks;
   @Nullable
   private final String preProcessScript;
   @NotNull
   private final String preProcessScriptCommand;
   @Nullable
   private final String postProcessScript;
   @NotNull
   private final String postProcessScriptCommand;

   @NotNull
   public final String getCommand() {
      return this.command;
   }

   @NotNull
   public final Analysis getAnalysis() {
      return this.analysis;
   }

   @NotNull
   public final Hardware getHardware() {
      return this.hardware;
   }

   public final boolean getUseMpi() {
      return this.useMpi;
   }

   @NotNull
   public final Map<String, String> getEnvVars() {
      return this.envVars;
   }

   @NotNull
   public final List<FileReference> getInputFiles() {
      return this.inputFiles;
   }

   public final boolean getUseRescaleLicense() {
      return this.useRescaleLicense;
   }

   @Nullable
   public final List getTemplateTasks() {
      return this.templateTasks;
   }

   @Nullable
   public final String getPreProcessScript() {
      return this.preProcessScript;
   }

   @NotNull
   public final String getPreProcessScriptCommand() {
      return this.preProcessScriptCommand;
   }

   @Nullable
   public final String getPostProcessScript() {
      return this.postProcessScript;
   }

   @NotNull
   public final String getPostProcessScriptCommand() {
      return this.postProcessScriptCommand;
   }

   public JobAnalysis(@NotNull String command, @NotNull Analysis analysis, @NotNull Hardware hardware, boolean useMpi, @NotNull Map<String, String> envVars, @NotNull List inputFiles, boolean useRescaleLicense, @Nullable List templateTasks, @Nullable String preProcessScript, @NotNull String preProcessScriptCommand, @Nullable String postProcessScript, @NotNull String postProcessScriptCommand) {
      this.command = command;
      this.analysis = analysis;
      this.hardware = hardware;
      this.useMpi = useMpi;
      this.envVars = envVars;
      this.inputFiles = inputFiles;
      this.useRescaleLicense = useRescaleLicense;
      this.templateTasks = templateTasks;
      this.preProcessScript = preProcessScript;
      this.preProcessScriptCommand = preProcessScriptCommand;
      this.postProcessScript = postProcessScript;
      this.postProcessScriptCommand = postProcessScriptCommand;
   }
}