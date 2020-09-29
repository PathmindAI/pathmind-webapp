import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class GoalsReachedStatus extends PolymerElement {
    static get is() {
        return "goals-reached-status";
    }

    static get properties() {
        return {
            reached: {
                type: Boolean,
                reflectToAttribute: true,
            }
        }
    }

    static get template() {
        return html`
            <style>
                :host {
                    display: flex;
                    align-items: center;
                    font-size: var(--lumo-font-size-xs);
                    line-height: 1;
                    margin-top: .125rem;
                }
                :host([hidden]) {
                    display: none;
                }
                iron-icon {
                    --iron-icon-width: var(--lumo-font-size-xs);
                    --iron-icon-height: var(--lumo-font-size-xs);
                    display: none;
                    margin-right: 2px;
                }
                :host([size~="large"]) iron-icon {
                    --iron-icon-width: var(--lumo-font-size-m);
                    --iron-icon-height: var(--lumo-font-size-m);
                    margin-right: 4px;
                }
                :host(:not([reached])) {
                    color: var(--pm-secondary-text-color);
                }
                :host(:not([reached])) iron-icon[icon~="vaadin:close"] {
                    display: block;
                }
                :host([reached]) {
                    color: var(--pm-friendly-color);
                }
                :host([reached]) iron-icon[icon~="vaadin:check"] {
                    display: block;
                }
            </style>
            <iron-icon icon="vaadin:check"></iron-icon>
            <iron-icon icon="vaadin:close"></iron-icon>
            Goals
        `;
    }
}

customElements.define(GoalsReachedStatus.is, GoalsReachedStatus);