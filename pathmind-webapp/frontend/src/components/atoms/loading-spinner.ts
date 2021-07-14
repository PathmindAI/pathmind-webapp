import { LitElement, html, css } from "lit-element";

class LoadingSpinner extends LitElement {
    static get styles() {
        return css`
            :host {
                --icon-size: var(--lumo-font-size-l);
                --border-size: 2px;
                display: block;
                position: relative;
                width: var(--icon-size);
                height: var(--icon-size);
            }
            :host([hidden]) {
                display: none;
            }
            div {
                box-sizing: border-box;
                content: "";
                position: absolute;
                width: var(--icon-size);
                height: var(--icon-size);
                top: 0;
                left: 0;
                border: var(--border-size) solid var(--pm-grey-color-light);
                border-radius: 50%;
                border-top: var(--border-size) solid var(--pm-primary-color);
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
            }`;
    }
    render() {
        return html`<div></div>`;
    }
}

customElements.define("loading-spinner", LoadingSpinner);
