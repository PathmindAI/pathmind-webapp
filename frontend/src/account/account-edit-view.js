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
          color: #696969;
        }

        #account-content {
          margin-top: 5px;
          background: #fff;
          border: 1px solid #ccc;
          border-radius: 0.3em;
          padding: 50px 80px 30px;
        }

        vaadin-text-field {
          width: 100%;
        }

        #buttonsCont {
          margin-top: 60px;
          width: 100%;
        }

        vaadin-button {
          margin: 5px auto;
        }
        #updateBtn {
          width: 250px;
        }

      </style>
       <vaadin-tabs>
        <vaadin-tab>
          Edit Account Information
        </vaadin-tab>
      </vaadin-tabs>
      <!-- <img src="/frontend/images/pathmind-logo.png">
      <img src="frontend/images/pathmind-logo.png">
      <img src="images/pathmind-logo.png"> -->
      <vaadin-vertical-layout style="width: 100%;" id="account-content">
        <vaadin-vertical-layout style="width: 100%; height: 100%;">
          <vaadin-text-field id="email" label="Email"></vaadin-text-field>
          <vaadin-text-field
            id="firstName"
            label="First Name"
          ></vaadin-text-field>
          <vaadin-text-field id="lastName" label="Last Name"></vaadin-text-field>
          <vaadin-vertical-layout id="buttonsCont">
              <vaadin-button id="updateBtn" theme="primary">Update</vaadin-button>
              <vaadin-button id="cancelBtn" theme="tertiary">Cancel</vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
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
