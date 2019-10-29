import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";
import "@vaadin/vaadin-text-field/src/vaadin-text-field.js";
import "@power-elements/stripe-elements"

class StripeView extends PolymerElement {

    static get template() {
        return html`
    <style include="shared-styles">
        :host {
            display: block;
            height: 100%;
          padding: 1em;
//            --stripe-elements-base-color: var(--paper-grey-700);
//            --stripe-elements-base-text-transform: uppercase;
//            --stripe-elements-base-font-family: 'Georgia';
//            --stripe-elements-base-font-style: italic;
//            --stripe-elements-element-padding: 14px;
//            --stripe-elements-element-background: #c0fefe;
//            --stripe-elements-invalid-color: yellow;
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

    <stripe-elements id="stripe"
        stripe-ready="{{isReady}}"
        is-complete="{{isComplete}}"
        publishable-key="[[key]]"
        card-data="[[cardData]]"
        hide-postal-code
        token="{{token}}"></stripe-elements>
        
    <vaadin-button id="submit"
        disabled="[[!isComplete]]"
        on-click="submit"
    >Submit</vaadin-button>

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
        this.stripe = Stripe(this.key);
        this.cardData = {}
    }

    submit() {
        if (this.isReady && this.isComplete) {
            this.$.stripe.submit();
        }
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
            isReady: Boolean,
            isComplete: Boolean
        };
    }
}

customElements.define(StripeView.is, StripeView);
