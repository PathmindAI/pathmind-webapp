import { LitElement, html, css, property } from "lit-element";

class FavoriteStar extends LitElement {
    @property({type: Boolean, reflect: true, attribute: "is-favorite"})
    isFavorite = false;

    attributeChangedCallback(name, oldval, newval) {
      super.attributeChangedCallback(name, oldval, newval);
      if (name === "is-favorite") {
        this._isFavoriteChanged(newval);
      }
    }

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
            <vaadin-button theme="tertiary-inline" title="Favorite">
                <iron-icon icon="vaadin:star-o"></iron-icon>
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
        this.toggleFavorite();
    }

    toggleFavorite() {
        // for server-side event handler
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

customElements.define("favorite-star", FavoriteStar);