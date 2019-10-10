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
          color: #696969;
        }

        #account-content {
          margin-top: 5px;
          background: #fff;
          border: 1px solid #ccc;
          border-radius: 0.3em;
          padding: 30px 40px;
        }

        .block {
          justify-content: space-between;
        }

        .border-top {
          border-top: 1px solid #f2f2f2;
          padding-top: 10px;
        }

        vaadin-button {
          width: 120px;
        }

        .title {
          font-weight: 700;
        }

        .data {
          padding-bottom: 30px;
        }

        .support-cont {
          text-align: center
        }
        .support {
          margin: auto;
          display: inline-block;
          color: #696969;
          padding-top: 10px;
        }

      </style>
      <vaadin-tabs>
        <vaadin-tab>
          Account Information
        </vaadin-tab>
      </vaadin-tabs>
      <!-- <img src="/frontend/images/pathmind-logo.png">
      <img src="frontend/images/pathmind-logo.png">
      <img src="images/pathmind-logo.png"> -->
      <vaadin-vertical-layout style="width: 100%;" id="account-content">
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
        <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
          <vaadin-vertical-layout class="info">
            <div class="title">Password</div>
            <div class="data">* * * * * * * * *</div>
          </vaadin-vertical-layout>
          <vaadin-button id="changePasswordBtn">
            Change
          </vaadin-button>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
          <vaadin-vertical-layout class="info">
            <div class="title">Current Subscription</div>
            <div class="data">{{subscription}}</div>
          </vaadin-vertical-layout>
          <vaadin-button id="upgradeBtn">
            Upgrade
          </vaadin-button>
        </vaadin-horizontal-layout>
        <vaadin-horizontal-layout style="width: 100%;" class="block border-top">
          <vaadin-vertical-layout class="info">
            <div class="title">Payment</div>
            <div class="data">{{billingInfo}}</div>
          </vaadin-vertical-layout>
          <vaadin-button id="editPaymentBtn">
            Edit
          </vaadin-button>
        </vaadin-horizontal-layout>
      </vaadin-vertical-layout>
      <div class="support-cont">
        <a class="support" href="/support">Contact Support</a>
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
