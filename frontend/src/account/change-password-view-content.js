import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `change-password-view`
 *
 * ChangePasswordView element.
 *
 * @customElement
 * @polymer
 */
class ChangePasswordViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view"></style>
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
            <vaadin-button
              id="updateBtn"
              theme="primary"
              class="positive-action-btn"
              >Change Password</vaadin-button
            >
            <vaadin-button id="cancelBtn" theme="tertiary"
              >Cancel</vaadin-button
            >
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
        <a class="support" href="{{contactLink}}">Contact Support</a>
      </div>
    `;
  }

  static get is() {
    return "change-password-view-content";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(ChangePasswordViewContent.is, ChangePasswordViewContent);
