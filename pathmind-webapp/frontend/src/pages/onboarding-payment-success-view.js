import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `onboarding-payment-success-view`
 *
 * OnboardingPaymentSuccessView element.
 *
 * @customElement
 * @polymer
 */
class OnboardingPaymentSuccessView extends PolymerElement {
  static get template() {
    return html`
      <style>
      onboarding-payment-success-view .inner-content {
          max-width: 430px;
          font-size: var(--lumo-font-size-m);
          text-align: justify;
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
                <h3>You have paid for the onboarding service</h3>
                <p>You should have received the payment receipt email. Our Customer Success Specialist will contact you shortly. If you have any questions, do not hesitate to message us.</p>
            </vaadin-vertical-layout>
        </div>
      </vaadin-horizontal-layout>`;
  }

  _attachDom(dom) {
    this.appendChild(dom);
  }

  static get is() {
    return "onboarding-payment-success-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(OnboardingPaymentSuccessView.is, OnboardingPaymentSuccessView);
