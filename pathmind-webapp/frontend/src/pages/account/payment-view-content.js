import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `payment-view-content`
 *
 * PaymentViewContent manages the integration with Stripe Elements.
 *
 * For more information please refer to the following links:
 * https://stripe.com/en-fi/payments/elements
 * https://stripe.com/docs/stripe-js
 *
 * NOTE: ONLY USE STRIPE ELEMENTS FOR MANAGING CREDIT CARD INFORMATION!
 *
 * @customElement
 * @polymer
 */
class PaymentViewContent extends PolymerElement {
  static get template() {
    return html`
    <style>
        payment-view-content #card-element {
          width: 100%;
          padding: 0 var(--lumo-space-s);
          border: 1px solid var(--pm-grey-color);
          border-radius: var(--lumo-border-radius);
          background: var(--pm-highlight-medium);
        }
        payment-view-content .inner-content {
          max-width: 460px;
        }
        payment-view-content .form-cont {
          width: 100%;
          text-align: left;
          margin: var(--lumo-space-m) auto 0;
        }
        payment-view-content #errorCont {
          margin: 10px 0;
          width: 100%;
        }
        payment-view-content #errorCont .error-message {
          padding-top: 8px;
          margin-left: calc(var(--lumo-border-radius-m) / 4);
          font-size: var(--lumo-font-size-xs);
          line-height: var(--lumo-line-height-xs);
          color: var(--lumo-error-text-color);
          will-change: max-height;
          transition: 0.4s max-height;
          max-height: 5em;
        }
        payment-view-content #city {
          width: 48%;
          margin-right: 2%;
        }
        payment-view-content #state {
          width: 20%;
          margin-right: 2%;
        }
        payment-view-content #zip {
          width: 26%;
          margin-right: 2%;
        }
        payment-view-content #numberOnCard, 
        payment-view-content #billingAddress {
          width: 98%;
          margin-right: 2%;
        }
        payment-view-content .custom-label {
          color: var(--lumo-secondary-text-color);
          font-size: var(--lumo-font-size-s);
          font-weight: 500;
          margin-top: 15px;
        }
        payment-view-content .title {
          font-size: 1.5em;
          margin: 0 auto;
        }
        payment-view-content .sub-title {
            color: var(--lumo-secondary-text-color);
            margin: 0 auto;
        }
        payment-view-content #buttonsCont {
            margin-top: 0;
        }
    </style>

    <div class="content">
      <vaadin-vertical-layout
        style="width: 100%;"
        class="inner-content"
        id="emailPart"
      >
        <div class="title">Upgrade to {{plan}}</div>
        <p class="sub-title">
          Please fill in the information below. All fields are required.
        </p>

        <vaadin-vertical-layout class="form-cont">
          <vaadin-text-field
            id="name"
            label="Name on card"
            required
            on-change="formUpdated"
            error-message="Please enter your name here"
            value="{{name}}"
          ></vaadin-text-field>
          <vaadin-text-field
            id="address"
            label="Billing Address"
            required
            on-change="formUpdated"
            error-message="Your address is missing"
            value="{{address}}"
          ></vaadin-text-field>
          <vaadin-horizontal-layout id="" style="width: 100%;">
            <vaadin-text-field
              id="city"
              label="City"
              required
              on-change="formUpdated"
              error-message="Please fill in your city"
              value="{{city}}"
            ></vaadin-text-field>
            <vaadin-text-field
              id="state"
              label="State"
              required
              on-change="formUpdated"
              error-message="State is missing"
              value="{{state}}"
            ></vaadin-text-field>
            <vaadin-text-field
              id="zip"
              label="Zip/Postal code"
              required
              on-change="formUpdated"
              error-message="Postal code is missing"
              value="{{postal_code}}"
            ></vaadin-text-field>
          </vaadin-horizontal-layout>

          <vaadin-horizontal-layout style="width:100%; margin-top: 25px">
            <div id="card-element"></div>
          </vaadin-horizontal-layout>
          <vaadin-horizontal-layout id="errorCont">
            <div class="error-message">{{errorMessage}}</div>
          </vaadin-horizontal-layout>
        </vaadin-vertical-layout>
        <vaadin-vertical-layout id="buttonsCont">
          <vaadin-button
            id="signUp"
            theme="primary"
            disabled="[[!and(isStripeComplete, isFormComplete)]]"
             on-click="submit"
            >Upgrade</vaadin-button
          >
        </vaadin-vertical-layout>

      </vaadin-vertical-layout>
    </div>`;
  }

  static get is() {
    return "payment-view-content";
  }

  // This allows us to keep the payment-view element in the light DOM.
  // Without this Stripe Elements won't work at all.
  _attachDom(dom) {
    this.appendChild(dom);
  }

  static get properties() {
    return {
      stripe: Object,
      errorMessage: Object,
      isStripeComplete: {
        type: Boolean,
        value: false,
      },
      isFormComplete: {
        type: Boolean,
        value: false,
      },
      showValidationError: {
        type: Boolean,
        value: false,
      },
    };
  }

  ready() {
    super.ready();

    const style = {
      base: {
        fontSize: "16px",
        color: "#1a2949",
        lineHeight: "32px"
      }
    };

    // Initialize Stripe with the key from the backend
    this.stripe = Stripe(this.key);
    // Initialize Stripe Elements as per Stripe documentation
    const elements = this.stripe.elements();
    // Create a new credit card field with Stripe, hide the postal code because we have a Vaadin field for that.
    // Also override the styles
    this.cardElement = elements.create("card", {
      hidePostalCode: true,
      style: style
    });
    // Find an element with #card-element id and mount that with the new Stripe credit card element
    // This creates an iFrame to Stripe services.
    this.cardElement.mount("#card-element");
    // Add a listener to the card element to know when it's ready.
    this.cardElement.addEventListener(
      "change",
      event => (this.isStripeComplete = event.complete)
    );
    // If there's an error coming from Stripe set it visible
    this.cardElement.addEventListener(
      "change",
      ({ error }) => (this.errorMessage = error ? error.message : "")
    );
  }

  and(a, b) {
    return a && b;
  }

  // submit is called when the user clicks the submit button. They are only able to do that once all the data has been
  // validated by us and by Stripe (Stripe naturally only validates the credit card information on their side)
  submit() {
    if (this.isStripeComplete) {
      const paymentView = this;
      this.stripe
        .createPaymentMethod("card", this.cardElement, {
          billing_details: {
            name: this.name,
            address: {
              line1: this.address,
              city: this.city,
              state: this.state,
              postal_code: this.postal_code
            }
          }
        })
        .then(result => {
          if (result.error) {
            this.errorMessage = result.error.message;
          } else {
            paymentView.setPaymentMethod(result.paymentMethod);
          }
        });
    }
  }

  // This method passes the payment method id to the backend which will be used to create the subscription for the user
  setPaymentMethod(paymentMethod) {
    this.$server.paymentMethodCallback(paymentMethod);
  }

  // Called every time the form changes. Validation for the Vaadin elements happens on the server side as well.
  // NOTE: DO NOT SEND ANY CREDIT CARD RELATED INFORMATION TO THE BACKEND. ONLY STRIPE SHOULD MANAGE THAT!
  formUpdated() {
    this.$server.validateForm({
      billing_details: {
        name: this.name,
        address: {
          line1: this.address,
          city: this.city,
          state: this.state,
          postal_code: this.postal_code
        }
      }
    });
  }
}

customElements.define(PaymentViewContent.is, PaymentViewContent);
