function filter(files) {
  Array.prototype.forEach.call(
    files,
    file => (file.filePath = file.webkitRelativePath)
  );
}

window.addClientSideFiltering = function(upload) {
  console.log("***  Upload found!");
  upload._addFiles = function(files) {
    filter(files);
    Array.prototype.forEach.call(files, upload._addFile.bind(upload));
  };
};
