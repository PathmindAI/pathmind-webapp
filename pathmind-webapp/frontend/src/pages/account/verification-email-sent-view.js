import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../../components/organisms/public-header-menu.ts";

/**
 * `verification-email-sent-view`
 *
 * VerificationEmailSentView element.
 *
 * @customElement
 * @polymer
 */
class VerificationEmailSentView extends PolymerElement {
  static get template() {
    return html`
      <style>
        verification-email-sent-view .icon {
          --iron-icon-height: var(--lumo-font-size-xxxl);
          --iron-icon-width: var(--lumo-font-size-xxxl);
          margin: 0 auto var(--lumo-space-xl);
          color: var(--lumo-success-color);
        }
      </style>
      <public-header-menu contactlink="{{contactLink}}" linktowebapp></public-header-menu>
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

  _attachDom(dom) {
    this.appendChild(dom);
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

customElements.define(VerificationEmailSentView.is, VerificationEmailSentView);
