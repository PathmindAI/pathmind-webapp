import { BLACK_LIST } from "./black-list.js";

function filter(files) {
  Array.prototype.forEach.call(
    files,
    file =>
      (file.filePath = file.webkitRelativePath.substring(
        file.webkitRelativePath.indexOf("/")
      ))
  );
  return Array.from(files).filter(file => matchesFilter(file.filePath));
}

function matchesFilter(filePath) {
  if (filePath.match("^/model.jar")) {
    return true;
  } else if (filePath.match("^/database//*")) {
    return true;
  } else if (filePath.match("^/lib//*")) {
    return !BLACK_LIST.includes(filePath);
  }
  return false;
}

window.addClientSideFiltering = function(upload) {
  upload._addFiles = function(files) {
    files = filter(files);
    console.log(files);
    Array.prototype.forEach.call(files, upload._addFile.bind(upload));
  };
};
