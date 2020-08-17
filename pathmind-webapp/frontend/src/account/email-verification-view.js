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
      <style include="pathmind-dialog-view sign-up-view-styles"></style>
      <div class="content">
        <span class="welcome-text">Welcome to</span>
        <img
          class="logo"
          src="frontend/images/pathmind-logo.svg"
          alt="Pathmind logo"
        />
        <vaadin-vertical-layout class="inner-content">
          <h3>Email Verified</h3>
          <p hidden$="{{error}}">
            Thanks for verifying your email.
          </p>
          <p class="error-message" hidden$="{{!error}}">
            Verification link is no longer valid.
          </p>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="backToApp" theme="primary"
              >Get Started with Pathmind</vaadin-button
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
