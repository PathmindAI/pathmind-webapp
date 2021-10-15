import { LitElement, html, property } from "lit-element";

class UploadModelErrorView extends LitElement {

  @property({type: String})
  contactLink = "";
  @property({type: String})
  error = "";

  render() {
    return html`
        <style>
            upload-model-error-view {
                max-width: 46rem;
                line-height: 2em;
                padding: var(--lumo-space-m);
                text-align: justify;
            }
            upload-model-error-view .section-title-label {
                margin-bottom: var(--lumo-space-l);
            }
            upload-model-error-view .error-label:not(:empty) {
                box-sizing: border-box;
                width: 100%;
                line-height: 1;
                padding: var(--lumo-space-s);
                margin: var(--lumo-space-xs) auto;
                border-radius: var(--lumo-border-radius);
            }
        </style>
        <vaadin-vertical-layout class="panel-wrapper">
            <div class="content">
                <span class="section-title-label">There's an issue with your model</span>
                <span class="error-label">${this.error}</span>
                <p>
                    The model you uploaded from AnyLogic has issues that prevent it from running in Pathmind.<br>
                    Use this <a href="https://help.pathmind.com/en/articles/3747446-8-confirm-models-are-working-in-anylogic" target="_blank">troubleshooting guide</a>
                    to fix the issues and try exporting to Pathmind again or <a href="${this.contactLink}">contact Pathmind for support</a>.
                </p>
            </div>
        </vaadin-vertical-layout>  
    `;
  }

  createRenderRoot() {
    return this;
  }
}

customElements.define("upload-model-error-view", UploadModelErrorView);
