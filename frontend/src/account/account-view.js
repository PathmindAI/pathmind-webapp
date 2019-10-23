import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-tabs/src/vaadin-tabs.js";
import "@vaadin/vaadin-tabs/src/vaadin-tab.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";

class AccountView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view">
      </style>
      <div id="header" style="width: 100%;"></div>
      <div class="content">
        <vaadin-tabs>
          <vaadin-tab>
            Account Information
          </vaadin-tab>
        </vaadin-tabs>

        <vaadin-vertical-layout style="width: 100%;" class="inner-content">
          <vaadin-horizontal-layout style="width: 100%;" class="block">
            <vaadin-vertical-layout class="info">
              <div class="title">User Email</div>
              <div class="data">{{email}}</div>
              <div class="title">First Name</div>
              <div class="data">{{firstName}}</div>
              <div class="title">Last Name</div>
              <div class="data">{{lastName}}</div>
            </vaadin-vertical-layout>
            <vaadin-button id="editInfoBtn">
              Edit
            </vaadin-button>
          </vaadin-horizontal-layout>
          <vaadin-horizontal-layout
            style="width: 100%;"
            class="block border-top"
          >
            <vaadin-vertical-layout class="info">
              <div class="title">Password</div>
              <div class="data">* * * * * * * * *</div>
            </vaadin-vertical-layout>
            <vaadin-button id="changePasswordBtn">
              Change
            </vaadin-button>
          </vaadin-horizontal-layout>
          <vaadin-horizontal-layout
            style="width: 100%;"
            class="block border-top"
          >
            <vaadin-vertical-layout class="info">
              <div class="title">Current Subscription</div>
              <div class="data">{{subscription}}</div>
            </vaadin-vertical-layout>
            <vaadin-button id="upgradeBtn">
              Upgrade
            </vaadin-button>
          </vaadin-horizontal-layout>
          <vaadin-horizontal-layout
            style="width: 100%;"
            class="block border-top"
          >
            <vaadin-vertical-layout class="info">
              <div class="title">Payment</div>
              <div class="data">{{billingInfo}}</div>
            </vaadin-vertical-layout>
            <vaadin-button id="editPaymentBtn">
              Edit
            </vaadin-button>
          </vaadin-horizontal-layout>
        </vaadin-vertical-layout>
        <div class="support-cont"></div>
        <a class="support" href="mailto:contact@pathmind.io">Contact Support</a>
      </div>
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
