import { LitElement, html, css, property } from "lit-element";

class RewardCodeErrorPanel extends LitElement {
    @property({type: String})
    errorText = "";
    @property({type: Boolean, reflect: true, attribute: "has-error"})
    hasError = false;

    static get styles() {
        return css`
            :host {
              flex: 1 0 auto;
              box-sizing: border-box;
              max-height: 6rem;
              height: auto;
              width: 100%;
              color: var(--pm-friendly-color);
              font-family: var(--code-font-family);
              line-height: 1.3em;
              background-color: var(--pm-app-bg-color);
              border: 1px solid var(--pm-grey-color);
              border-top: none;
              border-radius: 0 0 var(--lumo-border-radius-m) var(--lumo-border-radius-m);
              overflow: hidden;
            }
            ::-webkit-scrollbar {
              width: 8px;
            }
            ::-webkit-scrollbar-track {
            background-color: var(--pm-app-bg-color);
            }
            ::-webkit-scrollbar-thumb {
            background-color: var(--pm-grey-color-light);
            }
            :host([has-error]) {
              color: var(--pm-danger-color);
            }
            div {
              box-sizing: border-box;
              width: 100%;
              height: 100%;
              padding: var(--lumo-space-xs);
              overflow-y: auto;
            }
            .has-error {
              white-space: pre-line;
            }
            iron-icon {
              --iron-icon-width: 14px;
              --iron-icon-height: 14px;
            }
        `;
    }

    render() {
        return html`
            ${this.hasError ?
                html`<div class="has-error">${this.errorText}</div>` :
                this.errorText.length == 0 ? html`
                  <div class="no-error">
                    <iron-icon icon="vaadin:check"></iron-icon>
                    <span>No Errors</span>
                  </div>
                ` : ""
              }
        `;
    }

    updated(changedProperties) {
      changedProperties.forEach((oldValue, name) => {
          if (name === "errorText") {
            this.hasError = this.errorText.length > 0;
          }
      });
    }
}

customElements.define("reward-code-error-panel", RewardCodeErrorPanel);
