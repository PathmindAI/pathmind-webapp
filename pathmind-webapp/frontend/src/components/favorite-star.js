import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class FavoriteStar extends PolymerElement {
    static get is() {
        return "favorite-star";
    }

    constructor() {
        super();
    }

    static get properties() {
        return {
            isFavorite: {
                type: Boolean,
                observer: '_isFavoriteChanged'
            }
        }
    }

    static get template() {
        return html`
            <style>
                :host {
                    margin: -.1em 0 0 .4em;
                }
                vaadin-button {
                    color: var(--pm-gold-color);
                }
                iron-icon {
                    width: var(--lumo-font-size-m);
                    height: var(--lumo-font-size-m);
                    padding: 0;
                }
            </style>
            <vaadin-button theme="tertiary-inline" on-click="toggleFavorite">
                <iron-icon icon="vaadin:star-o"></iron-icon>
            </vaadin-button>
        `;
    }

    toggleFavorite(event) {
        event.stopPropagation();
    }

    _isFavoriteChanged(newValue) {
        const iconElement = this.shadowRoot.querySelector("iron-icon");
        const iconType = newValue ? "vaadin:star" : "vaadin:star-o";
        iconElement.setAttribute("icon", iconType);
    }
}

customElements.define(FavoriteStar.is, FavoriteStar);