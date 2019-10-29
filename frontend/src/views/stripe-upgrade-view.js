import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";

class StripeUpgradeView extends PolymerElement {

    static get template() {
        return html`
    <style include="shared-styles">
        :host {
            display: block;
            height: 100%;
        }
    </style>
    
    

`;
    }

    ready() {
        super.ready();
    }

    static get is() {
        return 'stripe-upgrade-view';
    }

    static get properties() {
        return {

        };
    }
}

customElements.define(StripeUpgradeView.is, StripeUpgradeView);
