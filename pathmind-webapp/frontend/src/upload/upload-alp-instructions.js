import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class UploadAlpInstructions extends PolymerElement {
    static get is() {
        return "upload-alp-instructions";
    }

    static get template() {
        return html`
            <p>Upload your model's ALP file to keep track of its version used for running experiments.</p>
            <p>Your ALP file should be in the original AnyLogic Project folder on your computer.</p>
            <p>You will be able to download this ALP file later to retrieve it.</p>
        `;
    }
}

customElements.define(UploadAlpInstructions.is, UploadAlpInstructions);