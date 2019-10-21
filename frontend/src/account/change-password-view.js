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
            Change Password
          </vaadin-tab>
        </vaadin-tabs>
        <vaadin-vertical-layout style="width: 100%;" class="inner-content">
          <div style="width: 100%;">
            <vaadin-password-field
              id="currentPassword"
              label="Current Password"
            ></vaadin-password-field>
            <vaadin-vertical-layout
              id="currentPassNotes"
              class="notes"
              style="width: 100%;"
            ></vaadin-vertical-layout>
          </div>
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
            <vaadin-button id="updateBtn" theme="primary"
              >Change Password</vaadin-button
            >
            <vaadin-button id="cancelBtn" theme="tertiary"
              >Cancel</vaadin-button
            >
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </div>
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
