import { LitElement, html, css, property } from "lit-element";

class FloatingCloseButton extends LitElement {
    @property({type: String})
    text = "";

    static get styles() {
        return css`
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
        `;
    }

    render() {
        return html`
            <iron-icon icon="vaadin:close"></iron-icon>
            <span>${this.text}</span>`;
    }
}

customElements.define("floating-close-button", FloatingCloseButton);
