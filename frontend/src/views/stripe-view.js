import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";

class StripeView extends PolymerElement {

    static get template() {
        return html`
    <style include="shared-styles">
        stripe-view {
            display: block;
            height: 100%;
          padding: 1em;
        }
        
    </style>


    <vaadin-text-field id="name"
        label="Name on card"
        value="{{cardData.name}}"></vaadin-text-field>

    <vaadin-text-field id="address1"
      label="Billing Address"
      value="{{cardData.address_line1}}"></vaadin-text-field>

    <vaadin-text-field id="city"
        label="City"
        value="{{cardData.address_city}}"></vaadin-text-field>

    <vaadin-text-field id="state"
        label="State"
        value="{{cardData.address_state}}"></vaadin-text-field>

    <vaadin-text-field id="zip"
        label="Zip/Postal code"
        value="{{cardData.address_zip}}"></vaadin-text-field>
       
    <div id="card-element"></div>
    <div id="card-errors"></div>

    <vaadin-button id="card-button" 
        disabled="[[!isComplete]]"
        on-click="submit"
    >Sign Up</vaadin-button>

    <vaadin-notification
        duration="4000"
        opened="[[token]]"
    >
        <template>Token received for 💳 [[token.card.last4]]! 🤑</template>
    </vaadin-notification>

`;
    }

    ready() {
        super.ready();

        var style = {
            base: {
                fontSize: '17px',
            }
        };

        this.cardData = {};

        this.stripe = Stripe(this.key);
        var elements = this.stripe.elements();
        this.cardElement = elements.create('card', {style: style});
        this.cardElement.mount('#card-element');

        this.cardElement.addEventListener('change', (event) => {
            const displayError = document.getElementById('card-errors');
            if (event.error) {
                displayError.textContent = error.message;
            } else {
                displayError.textContent = '';
            }
            this.isComplete = event.complete;
        });

        var cardButton = document.getElementById('card-button');

    }

    submit() {
        if (this.isComplete) {
            var stripeView = this;
            this.stripe.createToken(this.cardElement, this.cardData).then(function(result) {
                const displayError = document.getElementById('card-errors');
                if (result.error) {
                   displayError.textContent = result.error.message;
                } else {
                    stripeView.setToken(result.token);
                    // do real form submit
                }
            });
        }
    }

    setToken(newToken) {
        this.token = newToken;
    }

    static get is() {
        return 'stripe-view';
    }

    _attachDom(dom) {
        this.appendChild(dom);
    }

    static get properties() {
        return {
            stripe: Object,
            token: {
                type: Object,
                reflectToAttribute: true
            },
            isComplete: {
                type: Boolean,
                value: false
            }
        };
    }
}

customElements.define(StripeView.is, StripeView);
