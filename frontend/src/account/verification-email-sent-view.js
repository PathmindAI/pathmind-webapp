import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `verification-email-sent-view`
 *
 * VerificationEmailSentView element.
 *
 * @customElement
 * @polymer
 */
class VerificationEmailViewSent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view sign-up-view-styles">
        .inner-content {
          padding: var(--lumo-space-l) var(--lumo-space-l);
        }
        .icon {
          --iron-icon-height: 40px;
          --iron-icon-width: 40px;
          margin: 0 auto var(--lumo-space-xl);
          color: var(--lumo-success-color);
        }
      </style>
      <div class="content">
        <span class="welcome-text">Welcome to</span>
        <img
          class="logo"
          src="frontend/images/pathmind-logo.png"
          alt="Pathmind logo"
        />
        <vaadin-vertical-layout style="width: 100%;" class="inner-content">
          <h3>A verification email is sent.</h3>
          <iron-icon class="icon" icon="vaadin:check-circle"></iron-icon>
          <p>
            Follow the instructions in the email to verify your account.
          </p>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="backToLogin" theme="primary">Done</vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </div>
    `;
  }

  static get is() {
    return "verification-email-sent-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(VerificationEmailViewSent.is, VerificationEmailViewSent);
