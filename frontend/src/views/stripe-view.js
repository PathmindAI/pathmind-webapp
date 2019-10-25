import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";

class StripeView extends PolymerElement {

    static get template() {
        return html`
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-vertical-layout style="width: 100%; height: 100%;">
    <div id="card-element"></div>
</vaadin-vertical-layout>
`;
    }

    ready() {
        super.ready();
        var stripe = Stripe('pk_test_52b1olVNNEYJ3425191xXgxl00FNHDa3KY');

        var elements = stripe.elements();
        var cardElement = elements.create('card');
        cardElement.mount('#card-element');
    }

    static get is() {
        return 'stripe-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(StripeView.is, StripeView);
