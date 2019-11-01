import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";

/**
 * `payment-view`
 *
 * PaymentView element.
 *
 * @customElement
 * @polymer
 */
class PaymentView extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles">
        #card-element {
            width: 100%;
            border-radius: var(--lumo-border-radius);
            background: var(--pm-highlight-medium);
        }
        
         .content {
          margin: auto;
          max-width: 650px;
          width: 650px;
        }
        .form-cont {
          width: 75%;
          margin: 0 auto 0;
        }

        #errorCont {
          margin: 10px 20px 0;
          width: 100%;
          height: 80px;
        }
        #errorCont .error-message {
          padding-top: 8px;
        }

        #cardNumber vaadin-text-field {
          width: 23%;
          margin-right: 2%;
        }

        #expiresAndCVC vaadin-text-field {
          width: 48%;
          margin-right: 2%;
        }
        #city {
          width: 48%;
          margin-right: 2%;
        }
        #state {
          width: 20%;
          margin-right: 2%;
        }
        #zip {
          width: 26%;
          margin-right: 2%;
        }
        #numberOnCard, 
        #billingAddress {
          width: 98%;
          margin-right: 2%;
        }

        .custom-label {
          color: var(--lumo-secondary-text-color);
          font-size: var(--lumo-font-size-s);
          font-weight: 500;
          margin-top: 15px;
        }

        .title {
          font-size: 1.5em;
          margin-left: 20px;
          color: #676767;
        }
        .sub-title {
          margin-left: 20px;
          color: #878787;
        }

        .payment-notes {
          margin: 15px auto 0;
          color: #878787;
          font-size: 0.8em;
        }
    </style>

    <div id="header" style="width: 100%;">
      <slot name="screen-title-panel"></slot>
    </div>
    <div class="content">
      <vaadin-tabs>
        <vaadin-tab>
          Payment
        </vaadin-tab>
      </vaadin-tabs>
      <vaadin-vertical-layout
        style="width: 100%;"
        class="inner-content"
        id="emailPart"
      >
        <div class="title">Upgrade to {{plan}}</div>
        <div class="sub-title">
          Please fill in the information below. All fields are required.
        </div>

        <vaadin-vertical-layout id="errorCont">
          <div class="error-message" hidden="[[!showValidationError]]">Please fill in all the fields</div>
          <div class="error-message">{{message}}</div>
        </vaadin-vertical-layout>

        <vaadin-vertical-layout class="form-cont">
          <vaadin-text-field
            id="name"
            label="Name on card"
            required
            on-change="formUpdated"
            value="{{name}}"
          ></vaadin-text-field>
          <vaadin-text-field
            id="address"
            label="Billing Address"
            required
            on-change="formUpdated"
            value="{{address}}"
          ></vaadin-text-field>
          <vaadin-horizontal-layout id="" style="width: 100%;">
            <vaadin-text-field
              id="city"
              label="City"
              required
              on-change="formUpdated"
              value="{{city}}"
            ></vaadin-text-field>
            <vaadin-text-field
              id="state"
              label="State"
              required
              on-change="formUpdated"
              value="{{state}}"
            ></vaadin-text-field>
            <vaadin-text-field
              id="zip"
              label="Zip/Postal code"
              required
              on-change="formUpdated"
              value="{{postal_code}}"
            ></vaadin-text-field>
          </vaadin-horizontal-layout>

          <vaadin-horizontal-layout style="width:100%; margin-top: 25px">
              <div id="card-element"></div>
          </vaadin-horizontal-layout>
        </vaadin-vertical-layout>
        <vaadin-vertical-layout id="buttonsCont">
          <vaadin-button
            id="signUp"
            theme="primary"
            class="positive-action-btn"
            disabled="[[!and(isStripeComplete, isFormComplete)]]"
             on-click="submit"
            >Sign Up</vaadin-button
          >
          <vaadin-button id="cancelSignUpBtn" theme="tertiary" on-click="cancelButtonClicked"
            >Cancel</vaadin-button
          >
          
        </vaadin-vertical-layout>

        <span class="payment-notes">Month-to-month @ $500 / month</span>
      </vaadin-vertical-layout>

      </vaadin-vertical-layout>
      <a class="support" href="{{contactLink}}">Contact Support</a>
    </div>
    
    
    <vaadin-notification 
        duration="4000" 
        opened="[[paymentError]]" 
    > 
        <template>There has been an error processing the payment</template> 
    </vaadin-notification> 
    
`;
  }

  static get is() {
    return "payment-view";
  }

  _attachDom(dom) {
    this.appendChild(dom);
  }

  static get properties() {
    return {
      stripe: Object,
      message: Object,
      isStripeComplete: {
        type: Boolean,
        value: false
      },
      isFormComplete: {
        type: Boolean,
        value: false
      },
      showValidationError: {
        type: Boolean,
        value: false
      }
    };
  }

  ready() {
    super.ready();

    const style = {
      base: {
        fontSize: '17px',
        iconColor: '#666EE8',
        color: '#31325F',
        lineHeight: '40px',
        fontWeight: 300,
        //'::placeholder': {
        //    color: '#CFD7E0',
        //},
      }
    };

    this.stripe = Stripe(this.key);
    const elements = this.stripe.elements();
    this.cardElement = elements.create('card', {hidePostalCode: true, style: style});
    this.cardElement.mount('#card-element');
    this.cardElement.addEventListener('change', event => this.isStripeComplete = event.complete);
    this.cardElement.addEventListener('change', ({error}) => this.message = error ? error.message : '');
  }

  and(a, b) {
    return a && b;
  }

  submit() {
    if (this.isStripeComplete) {
      const paymentView = this;
      this.stripe.createPaymentMethod('card', this.cardElement, {
        billing_details: {
          name: this.name,
          address: {
            line1: this.address,
            city:  this.city,
            state:  this.state,
            postal_code:  this.postal_code
          }
        }
      }).then(function(result) {
        if (result.error) {
          this.paymentError = result.error.message;
        } else {
          paymentView.setPaymentMethod(result.paymentMethod);
        }
      });
    }
  }

  setPaymentMethod(paymentMethod) {
    this.$server.paymentMethodCallback(paymentMethod);
  }

  addScreenTitlePanel(element) {
    let slotElement = this.querySelector(`slot[name="screen-title-panel"]`);
    if (slotElement) {
      slotElement.innerHTML = ''; // remove previous children
      slotElement.appendChild(element);
    }
  }

  formUpdated() {
    this.$server.validateForm(
        {
          billing_details: {
            name: this.name,
            address: {
              line1: this.address,
              city: this.city,
              state: this.state,
              postal_code: this.postal_code
            }
          }
        }
    );
  }

}

customElements.define(PaymentView.is, PaymentView);
