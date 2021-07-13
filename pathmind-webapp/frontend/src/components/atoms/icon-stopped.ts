import { LitElement, html, css } from "lit-element";

class IconStopped extends LitElement {
    static get styles() {
        return css`
            :host {
                --icon-size: var(--lumo-font-size-l);
                display: block;
                position: relative;
                width: var(--icon-size);
                height: var(--icon-size);
                background: var(--pm-grey-color);
                border-radius: 50%;
            }
            div {
                box-sizing: border-box;
                position: absolute;
                width: calc(var(--icon-size) / 2);
                height: calc(var(--icon-size) / 2);
                top: calc(var(--icon-size) / 4);
                left: calc(var(--icon-size) / 4);
                border: 1px solid white;
            }`;
    }

    render() {
        return html`<div title="Stopped"></div>`;
    }
}

customElements.define("icon-stopped", IconStopped);
