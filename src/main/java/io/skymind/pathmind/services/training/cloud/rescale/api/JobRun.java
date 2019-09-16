package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

class JobRun {
   @Nullable
   private LocalDateTime dateCompleted;
   @Nullable
   private LocalDateTime dateInserted;
   private int displayOrder;
   @NotNull
   private String id;
   private boolean isOptimal;
   private int outputFileCount;
   private int outputFileSize;
   @Nullable
   private String parent;
   private int type;
   @NotNull
   private List<Variable> variables;

   // for deserialization
   private JobRun(){}

   public JobRun(@Nullable LocalDateTime dateCompleted, @Nullable LocalDateTime dateInserted, int displayOrder, @NotNull String id, boolean isOptimal, int outputFileCount, int outputFileSize, @Nullable String parent, int type, @NotNull List<Variable> variables) {
      this.dateCompleted = dateCompleted;
      this.dateInserted = dateInserted;
      this.displayOrder = displayOrder;
      this.id = id;
      this.isOptimal = isOptimal;
      this.outputFileCount = outputFileCount;
      this.outputFileSize = outputFileSize;
      this.parent = parent;
      this.type = type;
      this.variables = variables;
   }

   @Nullable
   public LocalDateTime getDateCompleted() {
      return this.dateCompleted;
   }

   public void setDateCompleted(@Nullable LocalDateTime dateCompleted) {
      this.dateCompleted = dateCompleted;
   }

   @Nullable
   public LocalDateTime getDateInserted() {
      return this.dateInserted;
   }

   public void setDateInserted(@Nullable LocalDateTime dateInserted) {
      this.dateInserted = dateInserted;
   }

   public int getDisplayOrder() {
      return this.displayOrder;
   }

   public void setDisplayOrder(int displayOrder) {
      this.displayOrder = displayOrder;
   }

   @NotNull
   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public boolean isOptimal() {
      return this.isOptimal;
   }

   public void setOptimal(boolean optimal) {
      isOptimal = optimal;
   }

   public int getOutputFileCount() {
      return this.outputFileCount;
   }

   public void setOutputFileCount(int outputFileCount) {
      this.outputFileCount = outputFileCount;
   }

   public int getOutputFileSize() {
      return this.outputFileSize;
   }

   public void setOutputFileSize(int outputFileSize) {
      this.outputFileSize = outputFileSize;
   }

   @Nullable
   public String getParent() {
      return this.parent;
   }

   public void setParent(@Nullable String parent) {
      this.parent = parent;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   @NotNull
   public List getVariables() {
      return this.variables;
   }

   public void setVariables(List<Variable> variables) {
      this.variables = variables;
   }
}