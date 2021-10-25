import { LitElement, html, css, property } from "lit-element";

class SharedByUsername extends LitElement {
    @property({type: String})
    username = "";

    static get styles() {
        return css`
            :host {
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: var(--lumo-font-size-xs);
                color: var(--pm-grey-color-darker);
                line-height: 1;
                padding: var(--lumo-space-xxs) var(--lumo-space-xs);
                border: 1px solid;
                border-radius: 30px;
                margin-left: var(--lumo-space-xs);
            }
            iron-icon {
                --iron-icon-width: .75rem;
                --iron-icon-height: .75rem;
                margin-right: var(--lumo-space-xxs);
            }
        `;
    }

    render() {
        return html`<iron-icon icon="vaadin:user"></iron-icon><span>${this.username}</span>`;
    }
}

customElements.define("shared-by-username", SharedByUsername);