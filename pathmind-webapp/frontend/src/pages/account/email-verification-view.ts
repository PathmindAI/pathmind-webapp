import { LitElement, html, property } from "lit-element";
import "../../components/organisms/public-header-menu.ts";

/**
 * `email-verification-view`
 *
 * EmailVerificationView element.
 *
 * @customElement
 */
class EmailVerificationView extends LitElement {

  @property({type: Boolean})
  error = false;
  @property({type: String})
  contactLink = "";

  render() {
    return html`
      <style>
        email-verification-view p {
            width: 100%;
        }
      </style>
      <public-header-menu contactlink="${this.contactLink}" linktowebapp></public-header-menu>
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
          <p ?hidden="${this.error}">
            Thanks for verifying your email.
          </p>
          <p class="error-message" ?hidden="${!this.error}">
            Verification link is no longer valid.
          </p>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="backToApp" theme="primary">Get Started with Pathmind</vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </div>
    </vaadin-horizontal-layout>`;
  }

  createRenderRoot() {
      return this;
  }

}

customElements.define("email-verification-view", EmailVerificationView);
