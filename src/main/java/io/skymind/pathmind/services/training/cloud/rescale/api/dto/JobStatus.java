package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class JobStatus {
   @NotNull
   private String status;
   @NotNull
   private LocalDateTime statusDate;
   @Nullable
   private String statusReason;

   // for deserialization
   private JobStatus(){}

   public JobStatus(@NotNull String status, @NotNull LocalDateTime statusDate, @Nullable String statusReason) {
      this.status = status;
      this.statusDate = statusDate;
      this.statusReason = statusReason;
   }

   @NotNull
   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   @NotNull
   public LocalDateTime getStatusDate() {
      return this.statusDate;
   }

   public void setStatusDate(LocalDateTime statusDate) {
      this.statusDate = statusDate;
   }

   @Nullable
   public String getStatusReason() {
      return this.statusReason;
   }

   public void setStatusReason(@Nullable String statusReason) {
      this.statusReason = statusReason;
   }
}