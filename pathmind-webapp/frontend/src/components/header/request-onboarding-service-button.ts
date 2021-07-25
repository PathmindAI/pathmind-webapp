import { LitElement, html, property } from "lit-element";
class RequestOnboardingServiceButton extends LitElement {

  @property({type: Object})
  stripeObj;

  @property({type: String})
  key = "";

  @property({type: String})
  userAPIKey = "";

  @property({type: String})
  apiUrl = "";

  render() {
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
      <vaadin-button @click="${this.handleClick}" theme="small">
          Request Onboarding Service
      </vaadin-button>`;
  }

  // This allows us to keep the element in the light DOM.
  // Without this Stripe Elements won't work at all.
  createRenderRoot() {
    return this;
  }

  updated(changedProperties) {
    changedProperties.forEach((oldValue, name) => {
        if (name === "key") {
            (this.stripeObj as stripe.Stripe) = window.Stripe(this.key);
        }
    });
  }

  handleClick() {
    fetch(`${this.apiUrl}/create-checkout-session`, {
      method: "POST",
      headers: {
        'X-PM-API-TOKEN': this.userAPIKey
      },
    })
    .then(response => {
      if (!response.ok) {
        throw new Error(response.statusText);
      }
      return response.json();
    })
    .then(session => 
      (this.stripeObj as stripe.Stripe).redirectToCheckout({ sessionId: session.id })
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
      window.location.pathname = "/page-not-found";
    });
  }

}

customElements.define("request-onboarding-service-button", RequestOnboardingServiceButton);
