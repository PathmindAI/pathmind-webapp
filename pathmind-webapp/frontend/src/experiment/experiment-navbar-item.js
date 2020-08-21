import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../components/atoms/icon-stopped.js";
import "../components/atoms/loading-spinner.js";

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
                iron-icon[icon="vaadin:pencil"] {
                    --iron-icon-width: 1rem;
                    --iron-icon-height: 1rem;
                    --iron-icon-fill-color: var(--lumo-contrast-90pct);
                    margin: 0.125rem;
                }
                iron-icon[icon="vaadin:check-circle"] {
                    --iron-icon-width: var(--lumo-font-size-l);
                    color: var(--pm-green-color);
                }
                iron-icon[icon="vaadin:exclamation-circle-o"] {
                    --iron-icon-width: var(--lumo-font-size-l);
                    color: var(--pm-danger-color);
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
            <div id="status"></div>
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

    constructor() {
        super();
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
            statusIcon: {
                type: String,
                observer: '_statusIconChanged',
                notify: true,
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

    _statusIconChanged(status) {
        switch(status) {
            case "pencil":
                this.$.status.innerHTML = `<iron-icon icon="vaadin:pencil"></iron-icon>`;
                break;
            case "icon-loading-spinner":
                this.$.status.innerHTML = `<loading-spinner></loading-spinner>`;
                break;
            case "comments":
                this.$.status.innerHTML = `<iron-icon icon="vaadin:check-circle"></iron-icon>`;
                break;
            case "icon-stopped":
                this.$.status.innerHTML = `<icon-stopped></icon-stopped>`;
                break;
            case "exclamation":
            default:
                this.$.status.innerHTML = `<iron-icon icon="vaadin:exclamation-circle-o"></iron-icon>`;
                break;
        }
    }
}

customElements.define(ExperimentNavbarItem.is, ExperimentNavbarItem);
