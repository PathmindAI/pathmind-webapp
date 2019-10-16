import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `forgot-password-view`
 *
 * ResetPasswordView element.
 *
 * @customElement
 * @polymer
 */
class ResetPasswordView extends PolymerElement {
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
        .info {
          color: #696969;
        }

        .inner-content {
          margin-top: 5px;
          background: #fff;
          border-radius: var(--lumo-border-radius);
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
          text-align: center;
        }
        .support {
          margin: auto;
          display: inline-block;
          color: #696969;
          padding-top: 10px;
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
        <vaadin-tabs>
          <vaadin-tab>
            Reset Password
          </vaadin-tab>
        </vaadin-tabs>

        <vaadin-vertical-layout style="width: 100%;" class="inner-content">
          <h3>Enter the user email and reset link will be sent</h3>
          <vaadin-text-field
            id="firstName"
            label="First Name"
          ></vaadin-text-field>
          <vaadin-text-field id="lastName" label="Last Name"></vaadin-text-field>
          <vaadin-text-field id="email" label="Email"></vaadin-text-field>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="sendBtn" theme="primary">Send</vaadin-button>
            <vaadin-button id="cancelBtn" theme="tertiary">Cancel</vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
        <div class="support-cont"></div>
        <a class="support" href="/support">Contact Support</a>
      </div>
    `;
  }

  static get is() {
    return "reset-password-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(ResetPasswordView.is, ResetPasswordView);
