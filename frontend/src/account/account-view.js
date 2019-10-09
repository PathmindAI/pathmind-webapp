import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-tabs/src/vaadin-tabs.js";
import "@vaadin/vaadin-tabs/src/vaadin-tab.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";

class AccountView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;
          height: 100%;
          width: 600px;
          margin: 50px auto 0;
        }
        .info {
          width: 70%;
        }
      </style>
      <vaadin-tabs>
        <vaadin-tab>
          Account Information
        </vaadin-tab>
      </vaadin-tabs>
      <vaadin-vertical-layout style="width: 100%; height: 100%;">
        <vaadin-horizontal-layout style="width: 100%;">
          <vaadin-vertical-layout class="info">
            <div>User Email</div>
            <div>{{email}}</div>
            <div>First Name</div>
            <div>{{firstName}}</div>
            <div>Last Name</div>
            <div>{{lastName}}</div>
          </vaadin-vertical-layout>
          <vaadin-button id="editInfoBtn">
            Edit
          </vaadin-button>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout style="width: 100%;">
          <vaadin-vertical-layout class="info">
            <div>Password</div>
            <div>* * * * * * * * *</div>
          </vaadin-vertical-layout>
          <vaadin-button id="changePasswordBtn">
            Change
          </vaadin-button>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout style="width: 100%;">
          <vaadin-vertical-layout class="info">
            <div>Current Subscription</div>
            <div>{{subscription}}</div>
          </vaadin-vertical-layout>
          <vaadin-button id="upgradeBtn">
            Upgrade
          </vaadin-button>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout style="width: 100%;">
          <vaadin-vertical-layout class="info">
            <div>Payment</div>
            <div>{{billingInfo}}</div>
          </vaadin-vertical-layout>
          <vaadin-button id="editPaymentBtn">
            Edit
          </vaadin-button>
        </vaadin-horizontal-layout>
      </vaadin-vertical-layout>
    `;
  }

  static get is() {
    return "account-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(AccountView.is, AccountView);
