import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ExportPolicyViewContent extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles pathmind-dialog-view">
        :host {
            justify-content: center;
            background: white;
        }
        .panel-wrapper {
            max-width: 520px;
            padding-bottom: 0;
            margin: 0 auto;
        }
        .file {
            justify-content: center;
            align-items: center;
            margin: var(--lumo-space-l) auto;
        }
        .file img {
            width: 140px;
            border: 2px solid var(--pm-grey-color-light);
            border-radius: var(--lumo-border-radius);
        }
        .filename {
            display: block;
            margin-top: var(--lumo-space-xs);
        }
        h4 {
            width: 100%;
            margin: var(--lumo-space-xs) auto;
        }
        a {
            color: var(--lumo-primary-color);
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .policy-tutorial-link {
            display: inline-block;
            width: 100%;
            margin-bottom: var(--lumo-space-l);
        }
    </style>
    <vaadin-vertical-layout class="panel-wrapper">
        <div class="content">
            <span class="section-title-label">Export Policy</span>
            <vaadin-vertical-layout class="file">
                <img
                    class="policy-file-icon"
                    src="frontend/images/exportPolicyIcon.gif"
                    alt="Export Policy"
                />
                <span class="filename">{{filename}}</span>
            </vaadin-vertical-layout>
            <h4>To use your policy:</h4>
            <ol>
                <li>Download this file.</li>
                <li>Return to AnyLogic and open the Pathmind Helper properties in your simulation.</li>
                <ul>
                    <li>Change the 'Mode' to 'Use Policy'.</li>
                    <li>In 'policyFile', click 'Browse' and select the file you downloaded.</li>
                </ul>
                <li>Run the simulation to see the policy in action.</li>
            </ol>
            <a class="policy-tutorial-link" href="https://help.pathmind.com/en/articles/3655157-9-validate-trained-policy" target="_blank">
                Learn how to validate your policy
            </a>
        </div>
    </vaadin-vertical-layout>`;
  }

  ready() {
        super.ready();
  }

  static get is() {
    return "export-policy-view-content";
  }

  static get properties() {
    return {
      filename: {
        type: String,
      },
    };
  }
}

customElements.define(ExportPolicyViewContent.is, ExportPolicyViewContent);
