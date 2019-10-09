import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-layout.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-item.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class AccountEditView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
       :host {
          display: block;
          height: 100%;
          width: 600px;
          margin: 50px auto 0;
        }
      </style>
      <vaadin-vertical-layout style="width: 100%; height: 100%;">
        <vaadin-text-field id="email" label="Email"></vaadin-text-field>
        <vaadin-text-field
          id="firstName"
          label="First Name"
        ></vaadin-text-field>
        <vaadin-text-field id="lastName" label="Last Name"></vaadin-text-field>
        <vaadin-horizontal-layout>
            <vaadin-button id="cancelBtn">Cancel</vaadin-button>
            <vaadin-button id="updateBtn">Update</vaadin-button>
        </vaadin-horizontal-layout>
      </vaadin-vertical-layout>
    `;
  }

  static get is() {
    return "account-edit-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(AccountEditView.is, AccountEditView);
