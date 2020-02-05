function filter(files) {
  Array.prototype.forEach.call(
    files,
    file =>
      (file.filePath = file.webkitRelativePath.substring(
        file.webkitRelativePath.indexOf("/") + 1
      ))
  );
  return Array.from(files).filter(file => matchesFilter(file.filePath));
}

function matchesFilter(filePath) {
  if (filePath.match("^model.jar")) {
    return true;
  } else if (filePath.match("^database//*")) {
    return true;
  }
  return false;
}
window.Pathmind = {
  ModelUploader: {
    addClientSideFiltering: function(upload) {
      upload._addFiles = function(files) {
        files = filter(files);
        if (files.length > 0) {
          Array.prototype.forEach.call(files, upload._addFile.bind(upload));
        } else {
          upload.dispatchEvent(
            new CustomEvent("no-file-to-upload", {
              bubbles: true,
              composed: true
            })
          );
        }
      };
    },
    isInputDirSupported: function() {
      var tmpInput = document.createElement("input");
      if ("webkitdirectory" in tmpInput) return true;
      return false;
    }
  }
};
