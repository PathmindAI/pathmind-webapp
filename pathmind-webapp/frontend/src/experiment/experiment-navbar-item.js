import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../components/atoms/status-icon.js";
import "../components/atoms/goals-reached-status.js";

class ExperimentNavbarItem extends PolymerElement {
    static get is() {
        return "experiment-navbar-item";
    }

    static get template() {
        return html`
            <style include="styles">
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
                .experiment-name a {
                	color: var(--lumo-body-text-color);
                	text-decoration: none;
                }
                .experiment-name a:hover {
                    color: var(--lumo-primary-color);
                    text-decoration: none;
                }
                .experiment-name p:nth-child(2) {
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
                status-icon[status~="pencil"] ~ div goals-reached-status {
                    display: none;
                }
            </style>
            <status-icon status=[[status]]></status-icon>
            <div class="experiment-name">
                <p><a id="experimentLink">Experiment #[[experimentName]]</a><favorite-star is-favorite="{{isFavorite}}"></favorite-star></p>
                <p>Created [[createdDate]]</p>
                <goals-reached-status reached=[[goalsReached]] hidden=[[!showGoals]]></goals-reached-status>
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
            },
            showGoals: {
                type: Boolean,
                value: false,
            },
            goalsReached: {
                type: Boolean,
            }
        }
    }
    
    ready() {
        super.ready();
        this.shadowRoot.querySelector("favorite-star").toggleFavorite = this.onFavoriteToggled;
    }

    onArchiveButtonClicked(event) {
        event.stopPropagation();
    }
}

customElements.define(ExperimentNavbarItem.is, ExperimentNavbarItem);