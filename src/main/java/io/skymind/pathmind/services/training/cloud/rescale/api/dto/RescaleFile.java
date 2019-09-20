package io.skymind.pathmind.services.training.cloud.rescale.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public
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

   private int typeId;
   @NotNull
   private String id;
   @NotNull
   private String name;
   private boolean isUploaded;
   private boolean isDeleted;
   private boolean viewInBrowser;
   @NotNull
   private LocalDateTime dateUploaded;
   @Nullable
   private String relativePath;
   @NotNull
   private String downloadUrl;
   @NotNull
   private String path;
   @NotNull
   private List<String> sharedWith;
   @NotNull
   private String owner;
   @NotNull
   private String encodedEncryptionKey;
   private int decryptedSize;
   @NotNull
   private String md5;

   // for deserialization
   private RescaleFile(){}

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

   public int getTypeId() {
      return this.typeId;
   }

   public void setTypeId(int typeId) {
      this.typeId = typeId;
   }

   @NotNull
   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   @NotNull
   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isUploaded() {
      return this.isUploaded;
   }

   public void setUploaded(boolean uploaded) {
      isUploaded = uploaded;
   }

   public boolean isDeleted() {
      return this.isDeleted;
   }

   public void setDeleted(boolean deleted) {
      isDeleted = deleted;
   }

   public boolean getViewInBrowser() {
      return this.viewInBrowser;
   }

   public void setViewInBrowser(boolean viewInBrowser) {
      this.viewInBrowser = viewInBrowser;
   }

   @NotNull
   public LocalDateTime getDateUploaded() {
      return this.dateUploaded;
   }

   public void setDateUploaded(LocalDateTime dateUploaded) {
      this.dateUploaded = dateUploaded;
   }

   @Nullable
   public String getRelativePath() {
      return this.relativePath;
   }

   public void setRelativePath(@Nullable String relativePath) {
      this.relativePath = relativePath;
   }

   @NotNull
   public String getDownloadUrl() {
      return this.downloadUrl;
   }

   public void setDownloadUrl(String downloadUrl) {
      this.downloadUrl = downloadUrl;
   }

   @NotNull
   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   @NotNull
   public List getSharedWith() {
      return this.sharedWith;
   }

   public void setSharedWith(List<String> sharedWith) {
      this.sharedWith = sharedWith;
   }

   @NotNull
   public String getOwner() {
      return this.owner;
   }

   public void setOwner(String owner) {
      this.owner = owner;
   }

   @NotNull
   public String getEncodedEncryptionKey() {
      return this.encodedEncryptionKey;
   }

   public void setEncodedEncryptionKey(String encodedEncryptionKey) {
      this.encodedEncryptionKey = encodedEncryptionKey;
   }

   public int getDecryptedSize() {
      return this.decryptedSize;
   }

   public void setDecryptedSize(int decryptedSize) {
      this.decryptedSize = decryptedSize;
   }

   @NotNull
   public String getMd5() {
      return this.md5;
   }

   public void setMd5(String md5) {
      this.md5 = md5;
   }
}