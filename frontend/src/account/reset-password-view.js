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
    <style include="pathmind-dialog-view"></style>
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
          <div class="section-title">
            Please enter your new password
          </div>
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
