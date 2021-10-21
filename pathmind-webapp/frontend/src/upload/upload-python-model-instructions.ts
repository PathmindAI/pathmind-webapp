import { LitElement, html, property } from "lit-element";
import type { UploadElement } from "@vaadin/vaadin-upload";

class UploadPythonModelInstructions extends LitElement {

  @property({ type: String })
  apiUrl = "";
  @property({ type: String })
  apiToken = "";
  @property({ type: Object })
  upload;

  render() {
    return html`
      <style>
        upload-model-instructions ol > li,
        upload-model-instructions ol > div > li {
            margin: var(--lumo-space-s) 0;
        }
        upload-model-instructions ol > div > li {
            margin-bottom: 0;
        }
      </style>
      <ol>
        <div>
          <li>Create a zip file that contains:</li>
          <ul>
            <li>your .py model files</li>
            <li>requirements.txt that contains a list of the extra Python packages</li>
          </ul>
          <li>Upload the new zip file below.
        </div>
      </ol>
      <vaadin-vertical-layout>
        <vaadin-upload
          id="uploader"
          accept="application/zip,.zip"
          target="${this.apiUrl}/py/upload"
          headers='{ "X-PM-API-TOKEN": "${this.apiToken}" }'
          max-files="1"
        >
          <vaadin-button theme="primary" slot="add-button">
            <iron-icon icon="vaadin:upload-alt"></iron-icon>
            Upload zip file
          </vaadin-button>
        </vaadin-upload>
      </vaadin-vertical-layout>
    `;
  }

  firstUpdated() {
    customElements.whenDefined('vaadin-upload').then(function() {
      this.upload = document.querySelector('vaadin-upload') as UploadElement;

      this.upload.addEventListener('upload-before', function(event) {
        console.log('upload xhr before open: ', event.detail.xhr);
  
        // Prevent the upload request:
        // event.preventDefault();
  
        var file = event.detail.file;
  
        // Custom upload request url for file
        file.uploadTarget = this.upload.target + '/' + file.name;
  
        // Custom name in the Content-Disposition header
        file.formDataName = 'attachment';
      });

      this.upload.addEventListener('upload-start', function(event) {
        console.log('upload xhr after send: ', event.detail.xhr);
      });

      this.upload.addEventListener('upload-response', function(event) {
        console.log("%cupload response", "background-color: yellow");
      });

      this.upload.addEventListener('upload-request', function(event) {
        console.log("%cupload request", "background-color: yellow");
      });

      this.upload.addEventListener('upload-success', function(event) {
        event.preventDefault();
        console.log("%cupload success", "background-color: yellow");
        this.$server.uploadSuccessHandler();
      });

      this.upload.addEventListener('upload-error', function(event) {
        event.preventDefault();
        console.log("%cupload error", "background-color: yellow");
        this.$server.uploadErrorHandler();
      });

      this.upload.addEventListener('file-reject', function(event) {
        event.preventDefault();
        console.log("%cfile rejected", "background-color: yellow");
        this.$server.fileRejectHandler();
      });
    });
  }

  createRenderRoot() {
    return this;
  }
}

customElements.define("upload-python-model-instructions", UploadPythonModelInstructions);