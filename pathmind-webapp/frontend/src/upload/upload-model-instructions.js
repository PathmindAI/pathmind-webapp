import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class UploadModelInstructions extends PolymerElement {
    static get is() {
        return "upload-model-instructions";
    }

    static get template() {
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
                    <a href="{{anylogicExportGuide}}" target="_blank">
                        Export your model as a standalone Java application.
                    </a>
                </li>
                <div hidden="{{!isZip}}">
                    <li>*Using the exported folder, Create a zip file that contains:</li>
                    <ul>
                        <li>model.jar</li>
                        <li>the "database" and "cache" folder if they exist</li>
                        <li>any excel sheets necessary for your AnyLogic simulation</li>
                    </ul>
                    <li>Upload the new zip file below.
                </div>
                <div hidden="{{isZip}}">
                    <li>Upload the exported folder.</li>
                </div>
            </ol>
            <p hidden="{{!isZip}}">*Note: If your AnyLogic simulation is composed of multiple .alp files, please upload the exported folder instead.</p>
        `;
    }

    static get properties() {
        return {
            anylogicExportGuide: {
                type: String,
                value: "https://help.anylogic.com/index.jsp?topic=%2Fcom.anylogic.help%2Fhtml%2Frunning%2Fexport-java-application.html",
            },
            isZip: {
                type: Boolean,
                value: false,
            },
        };
    }

    _attachDom(dom) {
      this.appendChild(dom);
    }
}

customElements.define(UploadModelInstructions.is, UploadModelInstructions);