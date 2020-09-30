import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ConfirmPopup extends PolymerElement {
    static get is() {
        return "confirm-popup";
    }

    static get properties() {
        return {
            opened: {
                type: Boolean,
                reflectToAttribute: true,
            },
            headerText: {
                type: String,
            },
            message: {
                type: String,
            },
            cancelText: {
                type: String,
            },
            confirmText: {
                type: String,
            },
            confirmButtonThemes: {
                type: String,
                value: "primary",
                observer: "_confirmButtonThemesChanged",
            },
        }
    }

    static get template() {
        return html`
            <style>
                :host {
                    display: none;
                }
                :host([opened]) {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    position: fixed;
                    width: 100%;
                    height: 100%;
                    top: 0;
                    left: 0;
                    z-index: 10;
                }
                #overlay {
                    position: absolute;
                    width: 100%;
                    height: 100%;
                    top: 0;
                    left: 0;
                    background-color: rgba(0,0,0,0.08);
                }
                popup {
                    box-sizing: border-box;
                    position: relative;
                    max-width: 30rem;
                    line-height: 1.6em;
                    background-color: white;
                    padding: var(--lumo-space-l);
                    border-radius: var(--lumo-border-radius-l);
                    box-shadow: 0 3px 5px var(--lumo-shade-10pct);
                }
                h3 {
                    margin: 0;
                }
                .message {
                    margin: var(--lumo-space-l) 0;
                }
                .buttons-wrapper {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                }
                vaadin-button {
                    min-width: 46%;
                    margin: 0;
                }
                vaadin-button[hidden] ~ vaadin-button {
                    width: 100%;
                }
            </style>
            <div id="overlay" on-click="close"></div>
            <popup>
                <h3>[[headerText]]</h3>
                <div class="message">
                    <slot></slot>
                    [[message]]
                </div>
                <div class="buttons-wrapper">
                    <vaadin-button id="cancel" tabindex="0" role="button" on-click="onCancel" hidden="[[_isEmptyStringOrUnset(cancelText)]]">[[cancelText]]</vaadin-button>
                    <vaadin-button id="confirm" theme="[[confirmButtonThemes]]" tabindex="0" role="button" on-click="onConfirm">[[confirmText]]</vaadin-button>
                </div>
            </popup>
        `;
    }

    _isEmptyStringOrUnset(prop) {
        return prop == null || prop === "";
    }

    _confirmButtonThemesChanged(newValue, oldValue) {
        this.$.confirm.setAttribute("theme", newValue);
    }
}

customElements.define(ConfirmPopup.is, ConfirmPopup);