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
      <style include="pathmind-dialog-view sign-up-view-styles">
        .content {
          width: 460px;
        }
        h3 {
          margin-bottom: 0;
        }
        h3 + p {
          margin-top: 0;
        }
      </style>
      <div class="content">
        <span class="welcome-text">Welcome to</span>
        <img
          class="logo"
          src="frontend/images/pathmind-logo.png"
          alt="Pathmind"
        />
        <vaadin-vertical-layout id="prePart" class="inner-content">
          <h3>Reset Your Password</h3>
          <p>
           Enter your work email and we'll send you a link to set a new password.
          </p>
          <div class="error-message">{{message}}</div>
          <vaadin-email-field
            id="email"
            label="Email"
            error-message="Please enter a valid email address"
            clear-button-visible
            required
          ></vaadin-email-field>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="sendBtn" theme="primary">
              Send
            </vaadin-button>
            <vaadin-button id="cancelBtn" theme="tertiary">
              Cancel
            </vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>

        <vaadin-vertical-layout class="inner-content" id="postPart">
          <h3>Set a New Password</h3>
          <p>Choose a new password</p>
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
            <vaadin-button id="changePassword" theme="primary"
              >Save Password</vaadin-button
            >
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
        <a class="support" href="{{contactLink}}">Contact Support</a>
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
