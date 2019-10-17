import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-text-field/src/vaadin-email-field.js";

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

        .notes {
          font-size: 0.9em;
          font-weight: 700;
          color: #979797;
          padding-left: 15px;
          padding-top: 5px;
        }

        .notes span:last-child {
          padding-bottom: 20px;
        }

        .notes .secondary {
          color: #e7e7e7;
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

        vaadin-text-field,
        vaadin-email-field,
        vaadin-password-field {
          width: 100%;
        }

        #buttonsCont {
          margin-top: 60px;
          width: 100%;
        }

        vaadin-button {
          margin: 5px auto;
        }

        .positive-action-btn {
          width: 250px;
        }

        .section-title {
          color: #666;
          font-size: 1.1em;
          font-weight: 500;
          margin-bottom: 20px;
          margin-top: 20px;
        }

        .error-message {
          color: var(--pm-danger-color);
          padding: 0 10px 10px;
          font-size: 0.9em;
        }
      </style>
      <div class="content">
        <vaadin-tabs>
          <vaadin-tab>
            Reset Password
          </vaadin-tab>
        </vaadin-tabs>
        <vaadin-vertical-layout id="prePart" style="width: 100%;" class="inner-content">
          <div class="section-title">
            Please enter the user email and reset link will be sent
          </div>
          <div class="error-message">{{message}}</div>
          <vaadin-email-field
            id="email"
            label="Email"
            name="email"
            error-message="Please enter a valid email address"
            clear-button-visible
            required
          ></vaadin-email-field>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="sendBtn" theme="primary" class="positive-action-btn">
              Send 
            </vaadin-button>
            <vaadin-button id="cancelBtn" theme="tertiary">
              Cancel
            </vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>

        <vaadin-vertical-layout
          style="width: 100%;"
          class="inner-content"
          id="postPart"
        >
          <vaadin-password-field
            id="newPassword"
            label="New Password"
          ></vaadin-password-field>
          <vaadin-vertical-layout
            id="newPassNotes"
            class="notes"
            style="width: 100%;"
          ></vaadin-vertical-layout>
          <vaadin-password-field
            id="confirmNewPassword"
            label="Confirm New Password"
          ></vaadin-password-field>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="changePassword" theme="primary"  class="positive-action-btn">Save Password</vaadin-button>
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
