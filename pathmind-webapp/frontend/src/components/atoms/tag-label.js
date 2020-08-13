import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class TagLabel extends PolymerElement {
    static get is() {
        return "tag-label";
    }

    constructor() {
        super();
    }

    ready() {
        super.ready();
        this.setOutlineStyle();
        this.setSize();
    }

    static get properties() {
        return {
            text: {
                type: String,
                value: "",
                observer: '_textChanged'
            },
            outline: {
                type: Boolean,
                value: false,
            },
            size: {
                type: String,
                value: "",
            }
        }
    }

    static get template() {
        return html`
            <style>
                :host {
                    box-sizing: border-box;
                    display: inline-block;
                    color: var(--pm-gray-color-dark);
                    font-size: var(--lumo-font-size-s);
                    line-height: 1.3;
                    padding: var(--lumo-space-xxxs) var(--lumo-space-xs);
                    background: var(--lumo-contrast-10pct);
                    border: 1px solid transparent;
                    border-radius: var(--lumo-border-radius);
                    margin: 0 var(--lumo-space-xxxs);
                }
                :host([outline="true"]) {
                    background: transparent;
                    border: 1px solid;
                }
                :host([size="small"]) {
                    padding: 0 var(--lumo-space-xxs);
                    border-radius: var(--lumo-border-radius-s);
                }
            </style>
            [[text]]
        `;
    }

    setSize() {
        if (this.size) {
            this.setAttribute("size", this.size);
        } else {
            this.removeAttribute("size");
        }
    }

    setOutlineStyle() {
        if (this.outline) {
            this.setAttribute("outline", true);
        } else {
            this.setAttribute("outline", false);
        }
    }

    _textChanged(newValue) {
        if (newValue.length === 0) {
            this.setAttribute("hidden", true);
        } else {
            this.removeAttribute("hidden");
        }
    }
}

customElements.define(TagLabel.is, TagLabel);