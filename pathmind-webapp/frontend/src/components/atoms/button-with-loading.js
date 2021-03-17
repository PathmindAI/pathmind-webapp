import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ButtonWithLoading extends PolymerElement {
    static get is() {
        return "button-with-loading";
    }

    static get properties() {
        return {
            loading: {
                type: Boolean,
                value: false,
                reflectToAttribute: true,
            },
            rectangularStyle: {
                type: Boolean,
                reflectToAttribute: true,
            },
            disabled: {
                type: Boolean,
                observer: "_disabledStateChange",
            },
        }
    }

    static get template() {
        return html`
        <style>
            :host {
                position: relative;
            }
            :host([rectangular-style]) vaadin-button {
                min-width: 7.5rem;
                height: var(--lumo-size-s);
                font-size: var(--lumo-font-size-s);
                border-radius: 0;
                margin: 0;
            }
            :host([loading]) span {
                display: none;
            }
            :host([loading]) loading-spinner {
                display: block;
            }
            loading-spinner {
                display: none;
                margin: auto;
            }
        </style>
        <vaadin-button id="button" on-click="onClick" disabled="{{disableButtons}}">
            <span>[[text]]</span>
            <loading-spinner></loading-spinner>
        </vaadin-button>`;
    }

    onClick() {
        this.loading = true;
        this.disabled = true;
    }

    _disabledStateChange(newValue) {
        this.$.button.disabled = newValue;
    }
}

customElements.define(ButtonWithLoading.is, ButtonWithLoading);