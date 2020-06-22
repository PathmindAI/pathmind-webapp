import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ExperimentSelectItem extends PolymerElement {
    static get is() {
        return "experiment-select-item";
    }

    constructor() {
        super();
    }

    ready() {
        super.ready();
    }

    static get properties() {}

    static get template() {
        return html`
            <style>
                :host {
                    display: flex;
                    flex-wrap: wrap;
                    width: 100%;
                    max-width: 25rem;
                }
                span:nth-of-type(1) {
                    width: 100%;
                    font-size: var(--lumo-font-size-s);
                    color: var(--lumo-secondary-text-color);
                }
                span:nth-of-type(2) {
                    flex: 0 0 6rem;
                    margin-right: var(--lumo-space-m);
                }
            </style>
            <span>[[projectName]]</span>
            <span>Model #[[modelName]]</span>
            <span>Experiment #[[experimentName]]</span>
        `;
    }
}

customElements.define(ExperimentSelectItem.is, ExperimentSelectItem);
