import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-layout.js";
import "@vaadin/vaadin-form-layout/src/vaadin-form-item.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class AccountEditView extends PolymerElement {
  static get template() {
    return html`
      <style>
        :host {
          padding: 40px;
          background: var(--pm-app-bg-color);
          height: 100%;
          width: 100%;
          display: flex;
          flex-direction: column;
          box-sizing: border-box;
          align-items: center;
        }

        .inner-content {
          margin-top: 5px;
          background: #fff;
          border-radius: var(--lumo-border-radius);
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
        .content {
          max-width: 500px;
          margin-top: 20px;
          width: 100%;
          display: flex;
          flex-direction: column;
          align-items: center;
        }

        vaadin-tabs {
          width: 100%;
        }
      </style>
      <div class="content">
        <vaadin-tabs class="tabs">
          <vaadin-tab>
            Edit Account Information
          </vaadin-tab>
        </vaadin-tabs>

        <vaadin-vertical-layout style="width: 100%;" class="inner-content">
          <vaadin-vertical-layout style="width: 100%; height: 100%;">
            <vaadin-text-field id="email" label="Email"></vaadin-text-field>
            <vaadin-text-field
              id="firstName"
              label="First Name"
            ></vaadin-text-field>
            <vaadin-text-field
              id="lastName"
              label="Last Name"
            ></vaadin-text-field>
            <vaadin-vertical-layout id="buttonsCont">
              <vaadin-button id="updateBtn" theme="primary"
                >Update</vaadin-button
              >
              <vaadin-button id="cancelBtn" theme="tertiary"
                >Cancel</vaadin-button
              >
            </vaadin-vertical-layout>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </div>
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
