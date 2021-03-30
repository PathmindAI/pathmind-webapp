import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class RequestOnboardingServiceButton extends PolymerElement {
  static get template() {
    return html`
    <style>
        request-onboarding-service-button {
            display: flex;
            align-items: center;
        }
        request-onboarding-service-button vaadin-button {
            height: var(--lumo-font-size-xl);
            font-size: var(--lumo-font-size-xs);
            font-weight: 600;
            font-family: var(--lumo-font-family-header);
            padding: 0 var(--lumo-space-xs);
            border-radius: 0;
        }
    </style>

    <vaadin-button on-click="handleClick" theme="small">
        Request Onboarding Service
    </vaadin-button>`;
  }

  static get is() {
    return "request-onboarding-service-button";
  }

  // This allows us to keep the payment-view element in the light DOM.
  // Without this Stripe Elements won't work at all.
  _attachDom(dom) {
    this.appendChild(dom);
  }

  static get properties() {
    return {
      stripe: Object,
    };
  }

  ready() {
    super.ready();

    this.stripe = Stripe(this.key);
  }

  handleClick() {
    fetch("http://localhost:8081/create-checkout-session", {
        method: "POST",
        headers: {
          'X-PM-API-TOKEN': this.userAPIKey
        },
      })
        .then(response => {
          if (!response.ok) {
            throw new Error(response.error);
          }
          return response.json();
        })
        .then(session => 
          this.stripe.redirectToCheckout({ sessionId: session.id })
        )
        .then(result => {
          // If redirectToCheckout fails due to a browser or network
          // error, you should display the localized error message to your
          // customer using error.message.
          if (result.error) {
            alert(result.error.message);
          }
        })
        .catch(error => {
          console.error("Error:", error);
          window.location = "http://localhost:8080/page-not-found";
        });
  }

}

customElements.define(RequestOnboardingServiceButton.is, RequestOnboardingServiceButton);
