import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `change-password-view`
 *
 * ChangePasswordView element.
 *
 * @customElement
 * @polymer
 */
class ChangePasswordView extends PolymerElement {
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
        #updateBtn {
          width: 250px;
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
      </style>
      <vaadin-tabs>
        <vaadin-tab>
          Change Password
        </vaadin-tab>
      </vaadin-tabs>
      <!-- <img src="/frontend/images/pathmind-logo.png">
      <img src="frontend/images/pathmind-logo.png">
      <img src="images/pathmind-logo.png"> -->
      <vaadin-vertical-layout style="width: 100%;" id="account-content">
        <vaadin-vertical-layout style="width: 100%; height: 100%;">
          <vaadin-password-field
            id="currentPassword"
            label="Current Password"
          ></vaadin-password-field>
          <vaadin-vertical-layout
            id="currentPassNotes"
            class="notes"
            style="width: 100%;"
          ></vaadin-vertical-layout>
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
            <vaadin-button id="updateBtn" theme="primary">Change Password</vaadin-button>
            <vaadin-button id="cancelBtn" theme="tertiary">Cancel</vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </vaadin-vertical-layout>
    `;
  }

  static get is() {
    return "change-password-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(ChangePasswordView.is, ChangePasswordView);
