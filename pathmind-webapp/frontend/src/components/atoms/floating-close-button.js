import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class FloatingCloseButton extends PolymerElement {
    static get is() {
        return "floating-close-button";
    }

    static get template() {
        return html`
            <style>
                :host {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 1.667rem;
                    font-size: var(--lumo-font-size-s);
                    color: var(--pm-grey-color-darker);
                    background-color: var(--pm-grey-color-lightest);
                    padding: var(--lumo-space-xxxs) var(--lumo-space-xs);
                    border-radius: var(--lumo-border-radius);
                    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16);
                    cursor: pointer;
                }
                iron-icon {
                    --icon-size: var(--lumo-font-size-s);
                    width: var(--icon-size);
                    height: var(--icon-size);
                }
                span {
                    width: 0;
                    white-space: nowrap;
                    overflow: hidden;
                    transition: width 0.3s;
                }
                :host(:hover) span {
                    width: 8.85rem;
                    margin-left: var(--lumo-space-xs);
                }
            </style>
            <iron-icon icon="vaadin:close"></iron-icon>
            <span>{{text}}</span>`;
    }

    ready() {
        super.ready();
        this.addEventListener("click", this.onClick);
    }

    click() {
        this.dispatchEvent(new CustomEvent("click"));
    }

    static get properties() {
        return {
            text: {
                type: String,
            },
        }
    }

}

customElements.define(FloatingCloseButton.is, FloatingCloseButton);
