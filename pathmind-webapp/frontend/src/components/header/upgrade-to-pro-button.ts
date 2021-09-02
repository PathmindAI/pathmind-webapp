import { LitElement, html, css } from "lit-element";
class UpgradeToProButton extends LitElement {
  static get styles() {
      return css`
        :host {
            display: flex;
            align-items: center;
        }
        vaadin-button {
            height: var(--lumo-font-size-xl);
            font-size: var(--lumo-font-size-xs);
            font-weight: 600;
            font-family: var(--lumo-font-family-header);
            padding: 0 var(--lumo-space-xs);
            border-radius: 0;
        }
      `;
  }

  render() {
    return html`
      <a href="/account/upgrade" router-link>
        <vaadin-button theme="small">
            Upgrade to Pro
        </vaadin-button>
      </a>`;
  }
}

customElements.define("upgrade-to-pro-button", UpgradeToProButton);
