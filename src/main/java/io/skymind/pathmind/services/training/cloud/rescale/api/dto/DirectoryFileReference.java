package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import javax.validation.constraints.NotNull;

public class DirectoryFileReference {
   @NotNull
   private String path;
   @NotNull
   private String resource;


   // for deserialization
   private DirectoryFileReference(){}

   public DirectoryFileReference(@NotNull String path, @NotNull String resource) {
      this.path = path;
      this.resource = resource;
   }

   @NotNull
   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   @NotNull
   public String getResource() {
      return this.resource;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }
}