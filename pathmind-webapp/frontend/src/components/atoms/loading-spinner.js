import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class LoadingSpinner extends PolymerElement {
    static get is() {
        return "loading-spinner";
    }

    static get template() {
        return html`
            <style>
                :host {
                    --icon-size: var(--lumo-font-size-l);
                    display: block;
                    position: relative;
                    width: var(--icon-size);
                    height: var(--icon-size);
                }
                div {
                    box-sizing: border-box;
                    content: "";
                    position: absolute;
                    width: var(--icon-size);
                    height: var(--icon-size);
                    top: 0;
                    left: 0;
                    border: 2px solid var(--pm-gray-color-light);
                    border-radius: 50%;
                    border-top: 2px solid var(--pm-primary-color);
                    -webkit-animation: spin 2s linear infinite; /* Safari */
                    animation: spin 2s linear infinite;
                }
                /* Safari */
                @-webkit-keyframes spin {
                    0% {
                        -webkit-transform: rotate(0deg);
                    }
                    100% {
                        -webkit-transform: rotate(360deg);
                    }
                }
                @keyframes spin {
                    0% {
                        transform: rotate(0deg);
                    }
                    100% {
                        transform: rotate(360deg);
                    }
                }
            </style>
            <div></div>
        `;
    }

    constructor() {
        super();
    }

    static get properties() {
        return {
        }
    }
}

customElements.define(LoadingSpinner.is, LoadingSpinner);
