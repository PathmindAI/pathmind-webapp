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
                observer: '_isFavoriteChanged',
                notify: true,
                reflectToAttribute: true
            }
        }
    }

    static get template() {
        return html`
            <style>
                :host {
                    margin: 0 0 0 .4em;
                }
                vaadin-button {
                    width: var(--lumo-font-size-m);
                    height: var(--lumo-font-size-m);
                    color: var(--pm-gold-color);
                }
                iron-icon {
                    vertical-align: text-top;
                    width: var(--lumo-font-size-m);
                    height: var(--lumo-font-size-m);
                    padding: 0;
                }
            </style>
            <vaadin-button theme="tertiary-inline" title="Favorite">
                <iron-icon icon="vaadin:star-o"></iron-icon>
            </vaadin-button>
        `;
    }

    ready() {
        super.ready();
        this.addEventListener("click", this._onClick);
    }

    _onClick(event) {
        event.stopPropagation();
        this.toggleFavorite();
    }

    _isFavoriteChanged(newValue) {
        const buttonElement = this.shadowRoot.querySelector("vaadin-button");
        const iconElement = this.shadowRoot.querySelector("iron-icon");
        const iconType = newValue ? "vaadin:star" : "vaadin:star-o";
        const titleText = newValue ? "Unfavorite" : "Favorite";
        buttonElement.setAttribute("title", titleText);
        iconElement.setAttribute("icon", iconType);
    }
}

customElements.define(FavoriteStar.is, FavoriteStar);