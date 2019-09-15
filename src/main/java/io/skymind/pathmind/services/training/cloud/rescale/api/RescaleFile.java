package io.skymind.pathmind.services.training.cloud.rescale.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
class RescaleFile {
   /**
    * 1 = inpute file,
    * 2 = template file,
    * 3 = parameter file,
    * 4 = script file,
    * 5 = output file,
    * 7 = design variable file,
    * 8 = case fvile,
    * 9 = optimizer file,
    * 10 = temporary file
    */

   private final int typeId;
   @NotNull
   private final String id;
   @NotNull
   private final String name;
   private final boolean isUploaded;
   private final boolean isDeleted;
   private final boolean viewInBrowser;
   @NotNull
   private final LocalDateTime dateUploaded;
   @Nullable
   private final String relativePath;
   @NotNull
   private final String downloadUrl;
   @NotNull
   private final String path;
   @NotNull
   private final List<String> sharedWith;
   @NotNull
   private final String owner;
   @NotNull
   private final String encodedEncryptionKey;
   private final int decryptedSize;
   @NotNull
   private final String md5;

   public final int getTypeId() {
      return this.typeId;
   }

   @NotNull
   public final String getId() {
      return this.id;
   }

   @NotNull
   public final String getName() {
      return this.name;
   }

   public final boolean isUploaded() {
      return this.isUploaded;
   }

   public final boolean isDeleted() {
      return this.isDeleted;
   }

   public final boolean getViewInBrowser() {
      return this.viewInBrowser;
   }

   @NotNull
   public final LocalDateTime getDateUploaded() {
      return this.dateUploaded;
   }

   @Nullable
   public final String getRelativePath() {
      return this.relativePath;
   }

   @NotNull
   public final String getDownloadUrl() {
      return this.downloadUrl;
   }

   @NotNull
   public final String getPath() {
      return this.path;
   }

   @NotNull
   public final List getSharedWith() {
      return this.sharedWith;
   }

   @NotNull
   public final String getOwner() {
      return this.owner;
   }

   @NotNull
   public final String getEncodedEncryptionKey() {
      return this.encodedEncryptionKey;
   }

   public final int getDecryptedSize() {
      return this.decryptedSize;
   }

   @NotNull
   public final String getMd5() {
      return this.md5;
   }

   public RescaleFile(int typeId, @NotNull String id, @NotNull String name, boolean isUploaded, boolean isDeleted, boolean viewInBrowser, @NotNull LocalDateTime dateUploaded, @Nullable String relativePath, @NotNull String downloadUrl, @NotNull String path, @NotNull List<String> sharedWith, @NotNull String owner, @NotNull String encodedEncryptionKey, int decryptedSize, @NotNull String md5) {
      this.typeId = typeId;
      this.id = id;
      this.name = name;
      this.isUploaded = isUploaded;
      this.isDeleted = isDeleted;
      this.viewInBrowser = viewInBrowser;
      this.dateUploaded = dateUploaded;
      this.relativePath = relativePath;
      this.downloadUrl = downloadUrl;
      this.path = path;
      this.sharedWith = sharedWith;
      this.owner = owner;
      this.encodedEncryptionKey = encodedEncryptionKey;
      this.decryptedSize = decryptedSize;
      this.md5 = md5;
   }
}