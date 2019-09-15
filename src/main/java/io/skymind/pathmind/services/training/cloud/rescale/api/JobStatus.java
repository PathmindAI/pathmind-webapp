package io.skymind.pathmind.services.training.cloud.rescale.api;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class JobStatus {
   @NotNull
   private final String status;
   @NotNull
   private final LocalDateTime statusDate;
   @Nullable
   private final String statusReason;

   @NotNull
   public final String getStatus() {
      return this.status;
   }

   @NotNull
   public final LocalDateTime getStatusDate() {
      return this.statusDate;
   }

   @Nullable
   public final String getStatusReason() {
      return this.statusReason;
   }

   public JobStatus(@NotNull String status, @NotNull LocalDateTime statusDate, @Nullable String statusReason) {
      this.status = status;
      this.statusDate = statusDate;
      this.statusReason = statusReason;
   }
}