package io.skymind.pathmind.services.training.cloud.rescale.api;

import javax.validation.constraints.NotNull;

class DirectoryFileReference {
   @NotNull
   private final String path;
   @NotNull
   private final String resource;

   @NotNull
   public final String getPath() {
      return this.path;
   }

   @NotNull
   public final String getResource() {
      return this.resource;
   }

   public DirectoryFileReference(@NotNull String path, @NotNull String resource) {
      this.path = path;
      this.resource = resource;
   }
}