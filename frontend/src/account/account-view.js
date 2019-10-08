import {html, PolymerElement} from '@polymer/polymer/polymer-element.js';

/**
 * `account-view`
 *
 * AccountView element.
 *
 * @customElement
 * @polymer
 */
class AccountView extends PolymerElement {

    static get template() {
        return html`
            <style include="shared-styles">
                :host {
                    display: block;
                    height: 100%;
                }
            </style>
            <h3>Hello</h3>
            <div id="container"></div> 
        `;
    }

    static get is() {
        return 'account-view';
    }

    static get properties() {
        return {
            // Declare your properties here.
        };
    }
}

customElements.define(AccountView.is, AccountView);
