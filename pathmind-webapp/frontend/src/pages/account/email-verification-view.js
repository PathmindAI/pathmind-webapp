import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../../components/organisms/public-header-menu.js";

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
      <style>
        email-verification-view p {
            width: 100%;
        }
      </style>
      <public-header-menu contactlink="{{contactLink}}" linktowebapp></public-header-menu>
      <vaadin-horizontal-layout class="panel-wrapper">
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
    </vaadin-horizontal-layout>`;
  }

  _attachDom(dom) {
    this.appendChild(dom);
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
