import { LitElement, html, property } from "lit-element";

class UploadModelInstructions extends LitElement {

    @property({type: Boolean})
    isZip = false;

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
                <li>
                    <a href="https://anylogic.help/anylogic/running/export-java-application.html" target="_blank">
                        Export your model as a standalone Java application.
                    </a>
                </li>
                <div ?hidden="${!this.isZip}">
                    <li>*Using the exported folder, Create a zip file that contains:</li>
                    <ul>
                        <li>model.jar</li>
                        <li>the "database" and "cache" folder if they exist</li>
                        <li>any excel sheets necessary for your AnyLogic simulation</li>
                    </ul>
                    <li>Upload the new zip file below.
                </div>
                <div ?hidden="${this.isZip}">
                    <li>Upload the exported folder.</li>
                </div>
            </ol>
            <p ?hidden="${!this.isZip}">*Note: If your AnyLogic simulation is composed of multiple .alp files, please upload the exported folder instead.</p>
        `;
    }

    createRenderRoot() {
      return this;
    }
}

customElements.define("upload-model-instructions", UploadModelInstructions);