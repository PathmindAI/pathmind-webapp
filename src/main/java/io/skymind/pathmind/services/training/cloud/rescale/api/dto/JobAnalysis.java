package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JobAnalysis {
   @NotNull
   private String command;
   @NotNull
   private Analysis analysis;
   @NotNull
   private Hardware hardware;
   private boolean useMpi;
   @NotNull
   private Map<String, String> envVars;
   @NotNull
   private List<FileReference> inputFiles;
   private boolean useRescaleLicense;
   @Nullable
   private List templateTasks;
   @Nullable
   private String preProcessScript;
   @NotNull
   private String preProcessScriptCommand;
   @Nullable
   private String postProcessScript;
   @NotNull
   private String postProcessScriptCommand;

   // for deserialization
   private JobAnalysis(){}

   public JobAnalysis(
           @NotNull String command,
           @NotNull Analysis analysis,
           @NotNull Hardware hardware,
           boolean useMpi,
           @NotNull Map<String, String> envVars,
           @NotNull List<FileReference> inputFiles,
           boolean useRescaleLicense,
           @Nullable List templateTasks,
           @Nullable String preProcessScript,
           @NotNull String preProcessScriptCommand,
           @Nullable String postProcessScript,
           @NotNull String postProcessScriptCommand
   ) {
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

   public static JobAnalysis create(String command, List<FileReference> inputFiles){
      return new JobAnalysis(
              command,
              Analysis.userIncluded(),
              Hardware.mercury(),
              false,
              Collections.emptyMap(),
              inputFiles,
              false,
              Collections.emptyList(),
              null,
              "",
              null,
              ""
      );
   }

   @NotNull
   public String getCommand() {
      return this.command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   @NotNull
   public Analysis getAnalysis() {
      return this.analysis;
   }

   public void setAnalysis(Analysis analysis) {
      this.analysis = analysis;
   }

   @NotNull
   public Hardware getHardware() {
      return this.hardware;
   }

   public void setHardware(Hardware hardware) {
      this.hardware = hardware;
   }

   public boolean getUseMpi() {
      return this.useMpi;
   }

   public void setUseMpi(boolean useMpi) {
      this.useMpi = useMpi;
   }

   @NotNull
   public Map<String, String> getEnvVars() {
      return this.envVars;
   }

   public void setEnvVars(Map<String, String> envVars) {
      this.envVars = envVars;
   }

   @NotNull
   public List<FileReference> getInputFiles() {
      return this.inputFiles;
   }

   public void setInputFiles(List<FileReference> inputFiles) {
      this.inputFiles = inputFiles;
   }

   public boolean getUseRescaleLicense() {
      return this.useRescaleLicense;
   }

   public void setUseRescaleLicense(boolean useRescaleLicense) {
      this.useRescaleLicense = useRescaleLicense;
   }

   @Nullable
   public List getTemplateTasks() {
      return this.templateTasks;
   }

   public void setTemplateTasks(@Nullable List templateTasks) {
      this.templateTasks = templateTasks;
   }

   @Nullable
   public String getPreProcessScript() {
      return this.preProcessScript;
   }

   public void setPreProcessScript(@Nullable String preProcessScript) {
      this.preProcessScript = preProcessScript;
   }

   @NotNull
   public String getPreProcessScriptCommand() {
      return this.preProcessScriptCommand;
   }

   public void setPreProcessScriptCommand(String preProcessScriptCommand) {
      this.preProcessScriptCommand = preProcessScriptCommand;
   }

   @Nullable
   public String getPostProcessScript() {
      return this.postProcessScript;
   }

   public void setPostProcessScript(@Nullable String postProcessScript) {
      this.postProcessScript = postProcessScript;
   }

   @NotNull
   public String getPostProcessScriptCommand() {
      return this.postProcessScriptCommand;
   }

   public void setPostProcessScriptCommand(String postProcessScriptCommand) {
      this.postProcessScriptCommand = postProcessScriptCommand;
   }
}