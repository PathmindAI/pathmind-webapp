import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../components/atoms/status-icon.js";

class ExperimentNavbarItem extends PolymerElement {
    static get is() {
        return "experiment-navbar-item";
    }

    static get template() {
        return html`
            <style>
                :host {
                    box-sizing: border-box;
                    display: flex;
                    align-items: center;
                    flex-shrink: 0;
                    width: 100%;
                    padding: 0.45rem var(--lumo-space-s);
                    border: 1px solid transparent;
                    border-right: none;
                    margin-top: -1px;
                    cursor: pointer;
                }
                :host([is-current]),
                :host(:hover) {
                    background-color: white;
                    border-color: var(--pm-gray-color-lighter);
                }
                .experiment-name {
                    font-size: var(--lumo-font-size-s);
                    line-height: 1em;
                    margin-left: var(--lumo-space-xs);
                }
                .experiment-name p {
                    margin: 0;
                }
                .experiment-name p:last-child {
                    font-size: var(--lumo-font-size-xs);
                    font-family: var(--lumo-font-family-header); /* This font should usually be used on a header. This is an exception. */
                    color: var(--pm-gray-color-dark);
                }
                favorite-star {
                    display: inline-block;
                }
                vaadin-button {
                    margin-left: auto;
                }
                vaadin-button[title="Archive"].action-button iron-icon {
                    width: var(--lumo-font-size-xs);
                    height: var(--lumo-font-size-xs);
                    padding: 0;
                }
            </style>
            <status-icon status=[[status]]></status-icon>
            <div class="experiment-name">
                <p>Experiment #[[experimentName]]<favorite-star is-favorite="{{isFavorite}}"></favorite-star></p>
                <p>Created [[createdDate]]</p>
            </div>
            <vaadin-button
                class="action-button"
                theme="tertiary-inline icon"
                title="Archive"
                on-click="onArchiveButtonClicked"
            >
                <iron-icon icon="vaadin:archive" slot="prefix"></iron-icon>
            </vaadin-button>
        `;
    }

    static get properties() {
        return {
            experimentName: {
                type: String,
            },
            createdDate: {
                type: String,
            },
            isCurrent: {
                type: Boolean,
                value: false,
                notify: true,
                reflectToAttribute: true,
            },
            isDraft: {
                type: Boolean
            },
            isFavorite: {
                type: Boolean,
                value: false,
                notify: true,
                reflectToAttribute: true,
            },
            status: {
                type: String,
            }
        }
    }
    
    ready() {
        super.ready();
        this.addEventListener("click", this.handleRowClicked);
        this.shadowRoot.querySelector("favorite-star").toggleFavorite = this.onFavoriteToggled;
    }

    onArchiveButtonClicked(event) {
        event.stopPropagation();
    }
}

customElements.define(ExperimentNavbarItem.is, ExperimentNavbarItem);
