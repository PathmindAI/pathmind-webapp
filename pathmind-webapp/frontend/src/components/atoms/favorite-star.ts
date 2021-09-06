import { LitElement, html, css, property } from "lit-element";

class FavoriteStar extends LitElement {
    @property({type: Boolean, reflect: true, attribute: "is-favorite"})
    isFavorite = false;

    static get styles() {
        return css`
            :host {
                margin: 0 0 0 .4em;
            }
            vaadin-button {
                width: var(--lumo-font-size-m);
                height: var(--lumo-font-size-m);
                color: var(--pm-gold-color);
            }
            iron-icon {
                width: var(--lumo-font-size-m);
                height: var(--lumo-font-size-m);
                padding: 0;
            }
        `;
    }

    render() {
        return html`
            <vaadin-button theme="tertiary-inline" title="${this.isFavorite ? 'Unfavorite' : 'Favorite'}">
                <iron-icon icon="${this.isFavorite ? 'vaadin:star' : 'vaadin:star-o'}"></iron-icon>
            </vaadin-button>
        `;
    }

    constructor() {
        super();
        this.addEventListener("click", this._onClick);
    }

    _onClick(event) {
        event.preventDefault();
        event.stopPropagation();
        if ((this as any).$server && (this as any).$server.toggleFavorite) {
            (this as any).$server.toggleFavorite();
        } else {
            this.toggleFavorite();
        }
    }

    toggleFavorite() {
        // for experiments-navbar-item
    }
}

customElements.define("favorite-star", FavoriteStar);