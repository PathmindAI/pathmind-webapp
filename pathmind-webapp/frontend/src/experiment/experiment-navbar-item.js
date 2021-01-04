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
                vaadin-button[title="Archive"],
                vaadin-button[title="Compare"] {
                    display: none;
                }
                vaadin-context-menu vaadin-button iron-icon,
                vaadin-button[title="Archive"].action-button iron-icon,
                vaadin-button[title="Compare"].action-button iron-icon {
                    width: var(--lumo-font-size-xs);
                    height: var(--lumo-font-size-xs);
                    padding: 0;
                }
                :host([is-draft]) goals-reached-status {
                    display: none;
                }
                :host([is-draft]) vaadin-context-menu,
                :host([is-on-draft-experiment-view]) vaadin-context-menu {
                    display: none;
                }
                :host([is-draft]) vaadin-button[title="Archive"],
                :host([is-on-draft-experiment-view]) vaadin-button[title="Archive"] {
                    display: block;
                }
                vaadin-context-menu {
                    margin: 0;
                    margin-left: auto;
                }
                vaadin-context-menu vaadin-button,
                vaadin-context-menu vaadin-button iron-icon {
                    color: var(--lumo-secondary-text-color);
                    padding: 0;
                    margin: 0;
                }
                vaadin-context-menu-item {
                    min-height: auto;
                    font-size: var(--lumo-font-size-xs);
                    padding: var(--lumo-space-xxs) var(--lumo-space-xs);
                }
                vaadin-context-menu-item iron-icon {
                    vertical-align: initial;
                    width: var(--lumo-font-size-xs);
                    height: var(--lumo-font-size-xs);
                    color: var(--lumo-secondary-text-color);
                    margin-right: var(--lumo-space-xs);
                }
            </style>
            <a id="experimentLink" on-click="handleRowClicked">
                <status-icon status=[[status]] status-text=[[statusText]]></status-icon>
                <div class="experiment-name">
                    <p>Experiment #[[experimentName]]<favorite-star is-favorite="{{isFavorite}}"></favorite-star></p>
                    <p>Created <slot></slot></p>
                    <goals-reached-status reached=[[goalsReached]] hidden></goals-reached-status>
                </div>
                <vaadin-context-menu id="navbarItemMenu" hidden="{{isOnDraftExperimentView}}">
                    <template>
                        <vaadin-context-menu-list-box>
                            <vaadin-context-menu-item class="vaadin-menu-item" on-click="triggerArchiveBtn">
                                <iron-icon icon="vaadin:archive"></iron-icon>
                                <span>Archive</span>
                            </vaadin-context-menu-item>
                            <vaadin-context-menu-item class="vaadin-menu-item" on-click="triggerCompareBtn">
                                <iron-icon icon="vaadin:split-h"></iron-icon>
                                <span>Compare</span>
                            </vaadin-context-menu-item>
                        </vaadin-context-menu-list-box>
                    </template>
                    <vaadin-button id="small-menu" theme="tertiary small">
                        <iron-icon icon="vaadin:ellipsis-dots-h"></iron-icon>
                    </vaadin-button>
                </vaadin-context-menu>
                <vaadin-button
                    id="archiveButton"
                    title="Archive"
                    class="action-button"
                    theme="tertiary-inline icon"
                    on-click="onArchiveButtonClicked"
                >
                    <iron-icon icon="vaadin:archive"></iron-icon>
                </vaadin-button>
                <vaadin-button
                    id="compareButton"
                    title="Compare"
                    on-click="onCompareButtonClicked"
                ></vaadin-button>
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
                reflectToAttribute: true,
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
        this.$.navbarItemMenu._setProperty("openOn", "click");
        this.shadowRoot.querySelector("favorite-star").toggleFavorite = this.onFavoriteToggled;
    }

    triggerArchiveBtn() {
        this.$.archiveButton.click();
    }

    triggerCompareBtn() {
        this.$.compareButton.click();
    }

    handleRowClicked(event) {
        event.preventDefault();
    }

    onArchiveButtonClicked(event) {
        event.preventDefault();
        event.stopPropagation();
    }

    onCompareButtonClicked(event) {
        event.preventDefault();
        event.stopPropagation();
        setTimeout(function() {
            window.dispatchEvent(new Event('resize'));
        }, 500);
    }
}

customElements.define(ExperimentNavbarItem.is, ExperimentNavbarItem);
