import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";
import "@power-elements/stripe-elements"

class StripeView extends PolymerElement {

    static get template() {
        return html`
    <style include="shared-styles">
        :host {
            display: block;
            height: 100%;
//            --stripe-elements-base-color: var(--paper-grey-700);
//            --stripe-elements-base-text-transform: uppercase;
//            --stripe-elements-base-font-family: 'Georgia';
//            --stripe-elements-base-font-style: italic;
//            --stripe-elements-element-padding: 14px;
//            --stripe-elements-element-background: #c0fefe;
//            --stripe-elements-invalid-color: yellow;
        }
        
    </style>
            
            
    <stripe-elements id="stripe"
        stripe-ready="{{ready}}"
        publishable-key="[[key]]"
        token="{{token}}"
    ></stripe-elements>
    
    <vaadin-button id="submit"
        disabled="[[!ready]]"
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
    }

    submit() {
        if (this.ready) {
            this.$.stripe.submit();
        }
    }

    static get is() {
        return 'stripe-view';
    }

    static get properties() {
        return {
            stripe: Object
        };
    }
}

customElements.define(StripeView.is, StripeView);
