import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "../components/atoms/status-icon.js";
import "../components/atoms/goals-reached-status.js";

class ExperimentNavbarItem extends PolymerElement {
    static get is() {
        return "experiment-navbar-item";
    }

    static get template() {
        return html`
            <style>
                :host {
                    box-sizing: border-box;
                    width: 100%;
                    padding: 0.45rem var(--lumo-space-s);
                    border-top: 1px solid transparent;
                    border-bottom: 1px solid transparent;
                    margin-top: -1px;
                }
                :host([is-current]),
                :host(:hover) {
                    background-color: white;
                    border-color: var(--pm-grey-color-lighter);
                }
                a {
                    box-sizing: border-box;
                    display: flex;
                    align-items: center;
                    flex-shrink: 0;
                    width: 100%;
                	color: var(--lumo-body-text-color);
                	text-decoration: none;
                }
                .experiment-name {
                    font-size: var(--lumo-font-size-s);
                    line-height: 1em;
                    margin-left: var(--lumo-space-xs);
                }
                .experiment-name p {
                    margin: 0;
                }
                .experiment-name p:nth-child(2) {
                    font-size: var(--lumo-font-size-xs);
                    font-family: var(--lumo-font-family-header); /* This font should usually be used on a header. This is an exception. */
                    color: var(--pm-grey-color-dark);
                }
                favorite-star {
                    display: inline-block;
                }
                vaadin-button {
                    margin-left: auto;
                }
                vaadin-button[title="Compare"].action-button iron-icon {
                    width: var(--lumo-font-size-s);
                    height: var(--lumo-font-size-s);
                    padding: 0;
                }
                :host([is-current-comparison-experiment]) vaadin-button[title="Compare"].action-button iron-icon {
                    color: var(--lumo-primary-color);
                }
                :host([is-draft]) vaadin-button[title="Compare"],
                :host([is-current]) vaadin-button[title="Compare"] {
                    display: none;
                }
                :host([is-draft]) goals-reached-status {
                    display: none;
                }
            </style>
            <a id="experimentLink" on-click="handleRowClicked">
                <status-icon status=[[status]] status-text=[[statusText]]></status-icon>
                <div class="experiment-name">
                    <p>Experiment #[[experimentName]]<favorite-star is-favorite="{{isFavorite}}"></favorite-star></p>
                    <p>Created <slot></slot></p>
                    <goals-reached-status reached=[[goalsReached]] hidden></goals-reached-status>
                </div>
                <vaadin-button
                    id="compareButton"
                    class="action-button"
                    theme="tertiary-inline icon"
                    title="Compare"
                    on-click="onCompareButtonClicked"
                    hidden="[[isOnDraftExperimentView]]"
                >
                    <iron-icon icon="vaadin:pin"></iron-icon>
                </vaadin-button>
            </a>
        `;
    }

    static get properties() {
        return {
            experimentName: {
                type: String,
            },
            isCurrent: {
                type: Boolean,
                value: false,
                notify: true,
                reflectToAttribute: true,
            },
            isCurrentComparisonExperiment: {
                type: Boolean,
                value: false,
                reflectToAttribute: true,
            },
            isDraft: {
                type: Boolean,
                reflectToAttribute: true,
            },
            isFavorite: {
                type: Boolean,
                value: false,
                notify: true,
                reflectToAttribute: true,
            },
            isOnDraftExperimentView: {
                type: Boolean,
            },
            status: {
                type: String,
            },
            statusText: {
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

    handleRowClicked(event) {
        event.preventDefault();
    }

    onCompareButtonClicked(event) {
        event.preventDefault();
        event.stopPropagation();
        setTimeout(function() {
            window.dispatchEvent(new Event('resize'));
        }, 600);
    }
}

customElements.define(ExperimentNavbarItem.is, ExperimentNavbarItem);
