import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js';

class StripeTest extends PolymerElement {

    static get template() {
        return html`
<script src="https://js.stripe.com/v3/"></script>
<style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
<vaadin-vertical-layout style="width: 100%; height: 100%;">hello</vaadin-vertical-layout>
`;
    }

    static get is() {
        return 'stripe-test';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(StripeTest.is, StripeTest);
