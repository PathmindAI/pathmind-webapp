import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js";

class AccountUpgradeView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;
          height: 100%;
        }
      </style>
      <div id="header" style="width: 100%;"></div>
      <div class="content">
        <vaadin-horizontal-layout
          class="content"
          style="width: 100%; height: 100%;"
        >
          UPGRADE
        </vaadin-horizontal-layout>
      </div>
    `;
  }

  static get is() {
    return "account-upgrade-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(AccountUpgradeView.is, AccountUpgradeView);
