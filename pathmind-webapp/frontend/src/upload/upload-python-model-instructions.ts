import { LitElement, html, property } from "lit-element";
import type { UploadElement } from "@vaadin/vaadin-upload";
import "../components/atoms/loading-spinner.ts";

class UploadPythonModelInstructions extends LitElement {

  @property({ type: String })
  apiUrl = "";
  @property({ type: String })
  apiToken = "";
  @property({ type: Object })
  upload;
  @property({ type: String })
  envValue = "";
  @property({ type: String })
  error = "";
  @property({ type: Number })
  projectId = -1;

  render() {
    return html`
      <style>
        upload-python-model-instructions ol > li,
        upload-python-model-instructions ol > div > li {
          margin: var(--lumo-space-s) 0;
        }
        upload-python-model-instructions ol > div > li {
          margin-bottom: 0;
        }
        upload-python-model-instructions #nextButton {
          margin: var(--lumo-space-m) auto;
        }
        .upload-model-view upload-python-model-instructions vaadin-upload {
          margin-top: var(--lumo-space-s);
        }
      </style>
      <ol>
        <div>
          <li>Create a zip file that contains:</li>
          <ul>
            <li>your .py model files</li>
            <li>requirements.txt that contains a list of the extra Python packages</li>
          </ul>
          <li>Enter the environment in the text field below. The first few parts of the name is the file structure, followed by the function name in the .py file.<br/>For example, <code>simulation.zip/examples/mouse/mouse_env_pathmind.py</code> with the function <code>MouseAndCheese</code> has an env name of <code>examples.mouse.mouse_env_pathmind.MouseAndCheese</code></li>
          <li>Upload the new zip file below.
        </div>
      </ol>
      <vaadin-vertical-layout>
        <vaadin-text-field
          id="env"
          label="env"
          placeholder="examples.mouse.mouse_env_pathmind.MouseAndCheese"
          @change="${event => this.envValueChanged(event)}"
        >
        </vaadin-text-field>
        <vaadin-upload
          id="uploader"
          accept="application/zip,.zip"
          max-files="1"
        >
          <vaadin-button theme="primary" slot="add-button">
            <iron-icon icon="vaadin:upload-alt"></iron-icon>
            Add zip file
          </vaadin-button>
        </vaadin-upload>
        <p class="error-label">${this.error}</p>
        <vaadin-button id="nextButton" theme="primary" disabled>
          <span>Next</span>
          <loading-spinner hidden></loading-spinner></vaadin-button>
      </vaadin-vertical-layout>
    `;
  }

  updated(changedProperties) {
    changedProperties.forEach((oldValue, name) => {
      if (name === "error" || name === "envValue") {
        this.setAllowNextStep();
      }
    })
  }

  setAllowNextStep() {
      const uploadButton = document.querySelector('#nextButton');
      if (this.error === "" && this.envValue !== "" && this.upload.files.length > 0) {
      uploadButton.removeAttribute("disabled");
    } else {
      uploadButton.setAttribute("disabled", "true");
    }
  }

  envValueChanged(event) {
    this.envValue = event.target.value;
    this.error = "";
  }

  firstUpdated() {
    customElements.whenDefined('vaadin-upload').then(() => {
      this.upload = document.querySelector('vaadin-upload') as UploadElement;
      const uploadButton = document.querySelector('#nextButton');

      uploadButton.addEventListener("click", () => {
        const file = this.upload.files[0];

        uploadButton.querySelector("span").setAttribute("hidden", "true");
        uploadButton.querySelector("loading-spinner").removeAttribute("hidden");
        uploadButton.setAttribute("disabled", "true");
  
        // Custom upload request url for file
        const uploadTarget = this.apiUrl + "/py/upload";
        const headers = {"X-PM-API-TOKEN": this.apiToken}
        const formData = new FormData();
        formData.append("file", file);
        formData.append("env", this.envValue);
        formData.append("projectId", ""+this.projectId);
        window.fetch(uploadTarget, {
          method: 'POST',
          headers,
          body: formData
        })
        .then(response => {
          if (response.status !== 201) {
            response.text().then(text => {
              this.error = text;
              this.showTextOnNextButton();
            });
            return;
          }
          window.location.href = response.headers.get("Location");
        })
        .catch(error => {
          this.error = error;
          this.showTextOnNextButton();
          console.error('Error:', error);
        });

      });

      this.upload.addEventListener('upload-before', event => {
        this.error = "";
      });

      this.upload.addEventListener('upload-success', event => {
        this.error = "";
        this.setAllowNextStep();
      });

      this.upload.addEventListener('file-remove', event => {
        this.error = "";
        this.setAllowNextStep();
      });
    });
  }

  showTextOnNextButton() {
    const uploadButton = document.querySelector('#nextButton');
    uploadButton.querySelector("span").removeAttribute("hidden");
    uploadButton.querySelector("loading-spinner").setAttribute("hidden", "true");
  }

  createRenderRoot() {
    return this;
  }
}

customElements.define("upload-python-model-instructions", UploadPythonModelInstructions);