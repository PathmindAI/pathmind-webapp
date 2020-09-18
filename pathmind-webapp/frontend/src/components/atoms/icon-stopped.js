import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class IconStopped extends PolymerElement {
    static get is() {
        return "icon-stopped";
    }

    static get template() {
        return html`
            <style>
                :host {
                    --icon-size: var(--lumo-font-size-l);
                    display: block;
                    position: relative;
                    width: var(--icon-size);
                    height: var(--icon-size);
                    background: var(--pm-gray-color);
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
                }
            </style>
            <div title="Stopped"></div>
        `;
    }
}

customElements.define(IconStopped.is, IconStopped);
