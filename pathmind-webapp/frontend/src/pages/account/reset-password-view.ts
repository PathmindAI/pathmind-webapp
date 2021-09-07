import { LitElement, html, property } from "lit-element";
import "@vaadin/vaadin-text-field/src/vaadin-email-field.js";
import "../../components/organisms/public-header-menu.ts";

/**
 * `forgot-password-view`
 *
 * ResetPasswordView element.
 *
 * @customElement
 */
class ResetPasswordView extends LitElement {

  @property({type: String})
  contactLink = "";

  @property({type: String})
  message = "";

  render() {
    return html`
      <public-header-menu contactlink="${this.contactLink}" linktowebapp></public-header-menu>
      <vaadin-horizontal-layout class="panel-wrapper">
        <div class="content">
        <vaadin-vertical-layout id="prePart" class="inner-content">
          <h3>Reset Your Password</h3>
          <p>
            Enter your work email and we'll send you a link to set a new
            password.
          </p>
          <div class="error-message">${this.message}</div>
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
      </div>
    </vaadin-horizontal-layout>`;
  }

  createRenderRoot() {
      return this;
  }
}

customElements.define("reset-password-view", ResetPasswordView);
