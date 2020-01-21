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
      <style include="pathmind-dialog-view">
        .title {
          margin: 20px auto 0;
          font-size: 1.1em;
        }
        .icon {
          --iron-icon-height: 40px;
          --iron-icon-width: 40px;
          margin: 25px auto 15px;
          color: var(--lumo-success-color);
        }
        .note {
          margin: 25px auto 15px;
        }
      </style>
      <div class="content">
        <vaadin-vertical-layout style="width: 100%;" class="inner-content">
          <div class="title">
            A verification email is sent.
          </div>
          <iron-icon class="icon" icon="vaadin:check-circle"></iron-icon>
          <div class="note">
            Follow the instructions in the email to verify your account.
          </div>

          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button
              id="backToLogin"
              theme="primary"
              class="positive-action-btn"
              >Done</vaadin-button
            >
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
