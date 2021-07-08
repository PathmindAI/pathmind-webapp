import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "./icon-stopped.ts";
import "./loading-spinner.ts";

class StatusIcon extends PolymerElement {
    static get is() {
        return "status-icon";
    }

    static get template() {
        return html`
            <style>
                iron-icon[icon="vaadin:pencil"] {
                    --iron-icon-width: 1rem;
                    --iron-icon-height: 1rem;
                    --iron-icon-fill-color: var(--lumo-contrast-90pct);
                    margin: 0.125rem;
                }
                iron-icon[icon="vaadin:check-circle"] {
                    --iron-icon-width: var(--lumo-font-size-l);
                    --iron-icon-height: var(--lumo-font-size-l);
                    color: var(--pm-green-color);
                }
                iron-icon[icon="vaadin:exclamation-circle-o"] {
                    --iron-icon-width: var(--lumo-font-size-l);
                    --iron-icon-height: var(--lumo-font-size-l);
                    color: var(--pm-danger-color);
                }
            </style>
            <template is="dom-if" if="[[_isEqualTo(status, 'pencil')]]">
                <iron-icon icon="vaadin:pencil"></iron-icon>
            </template>
            <template is="dom-if" if="[[_isEqualTo(status, 'loading')]]">
                <loading-spinner></loading-spinner>
            </template>
            <template is="dom-if" if="[[_isEqualTo(status, 'check')]]">
                <iron-icon icon="vaadin:check-circle"></iron-icon>
            </template>
            <template is="dom-if" if="[[_isEqualTo(status, 'stopped')]]">
                <icon-stopped></icon-stopped>
            </template>
            <template is="dom-if" if="[[_isEqualTo(status, 'exclamation')]]">
                <iron-icon icon="vaadin:exclamation-circle-o"></iron-icon>
            </template>
        `;
    }

    static get properties() {
        return {
            status: {
                type: String,
                reflectToAttribute: true,
            },
            statusText: {
                type: String,
                reflectToAttribute: true,
                observer: '_isStatusTextChanged',
            },
        }
    }

    _isEqualTo(status, statusName) {
        return status === statusName;
    }

    _isStatusTextChanged(newValue) {
        this.setAttribute("title", newValue);
    }
}

customElements.define(StatusIcon.is, StatusIcon);