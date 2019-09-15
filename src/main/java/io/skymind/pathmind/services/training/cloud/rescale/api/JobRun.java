package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

class JobRun {
   @Nullable
   private final LocalDateTime dateCompleted;
   @Nullable
   private final LocalDateTime dateInserted;
   private final int displayOrder;
   @NotNull
   private final String id;
   private final boolean isOptimal;
   private final int outputFileCount;
   private final int outputFileSize;
   @Nullable
   private final String parent;
   private final int type;
   @NotNull
   private final List<Variable> variables;

   @Nullable
   public final LocalDateTime getDateCompleted() {
      return this.dateCompleted;
   }

   @Nullable
   public final LocalDateTime getDateInserted() {
      return this.dateInserted;
   }

   public final int getDisplayOrder() {
      return this.displayOrder;
   }

   @NotNull
   public final String getId() {
      return this.id;
   }

   public final boolean isOptimal() {
      return this.isOptimal;
   }

   public final int getOutputFileCount() {
      return this.outputFileCount;
   }

   public final int getOutputFileSize() {
      return this.outputFileSize;
   }

   @Nullable
   public final String getParent() {
      return this.parent;
   }

   public final int getType() {
      return this.type;
   }

   @NotNull
   public final List getVariables() {
      return this.variables;
   }

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
}