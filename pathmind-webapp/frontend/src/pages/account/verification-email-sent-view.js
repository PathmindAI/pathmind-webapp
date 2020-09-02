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
      <style include="shared-styles pathmind-dialog-view">
        .icon {
          --iron-icon-height: var(--lumo-font-size-xxxl);
          --iron-icon-width: var(--lumo-font-size-xxxl);
          margin: 0 auto var(--lumo-space-xl);
          color: var(--lumo-success-color);
        }
      </style>
      <vaadin-horizontal-layout class="panel-wrapper">
        <div class="content">
            <span class="welcome-text">Welcome to</span>
            <img
            class="logo"
            src="frontend/images/pathmind-logo.svg"
            alt="Pathmind"
            />
            <vaadin-vertical-layout class="inner-content">
                <h3>We sent you a verification email.</h3>
                <iron-icon class="icon" icon="vaadin:check-circle"></iron-icon>
                <p>Follow the instructions in the email to verify your account.</p>
                <vaadin-vertical-layout id="buttonsCont">
                    <vaadin-button id="backToLogin" theme="primary">Done</vaadin-button>
                </vaadin-vertical-layout>
            </vaadin-vertical-layout>
        </div>
      </vaadin-horizontal-layout>`;
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
