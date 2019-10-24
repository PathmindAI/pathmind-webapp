import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `email-verification-view`
 *
 * EmailVerificationView element.
 *
 * @customElement
 * @polymer
 */
class EmailVerificationView extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view">
          .error-message,
          .section-title {
            margin: 20px auto 0;
            font-size: 1.1em;
          }
      </style>
      <div class="content">
        <vaadin-tabs>
          <vaadin-tab>
            Email verification
          </vaadin-tab>
        </vaadin-tabs>
        <vaadin-vertical-layout style="width: 100%;" class="inner-content">
          <div class="section-title" hidden$="{{error}}">
            Your email was sucessfully verified.
          </div>

          <div class="error-message" hidden$="{{!error}}">Verification link is no longer valid.</div>
          
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button
              id="backToApp"
              theme="primary"
              class="positive-action-btn"
              >Back to Application</vaadin-button
            >
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </div>
    `;
  }

  static get is() {
    return "email-verification-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(EmailVerificationView.is, EmailVerificationView);
