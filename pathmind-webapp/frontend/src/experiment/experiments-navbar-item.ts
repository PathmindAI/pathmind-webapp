import { LitElement, html, css, property } from "lit-element";
import "../components/atoms/status-icon.ts";

class ExperimentsNavbarItem extends LitElement {
    @property({type: String})
    experimentName = "";
    @property({type: String})
    status = "";
    @property({type: String})
    statusText = "";
    @property({type: Boolean})
    isOnDraftExperimentView = false;
    @property({type: Boolean})
    isFavorite = false;
    @property({type: Boolean, reflect: true, attribute: "is-draft"})
    isDraft = false;
    @property({type: Boolean, reflect: true, attribute: "is-current-comparison-experiment"})
    isCurrentComparisonExperiment = false;
    @property({type: Boolean, reflect: true, attribute: "is-current"})
    isCurrent = false;

    static get styles() {
        return css`
            :host {
                box-sizing: border-box;
                width: 100%;
                padding: 0.4rem var(--lumo-space-xs);
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
                white-space: nowrap;
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
        `;
    }

    render() {
        return html`
            <a id="experimentLink" @click="${this.handleRowClicked}">
                <status-icon status="${this.status}" status-text="${this.statusText}"></status-icon>
                <div class="experiment-name">
                    <p>Experiment #${this.experimentName}<favorite-star ?is-favorite="${this.isFavorite}"></favorite-star></p>
                    <p>Created <slot></slot></p>
                </div>
                <vaadin-button
                    id="compareButton"
                    class="action-button"
                    theme="tertiary-inline icon"
                    title="Compare"
                    @click="${this.onCompareButtonClicked}"
                    ?hidden="${this.isOnDraftExperimentView}"
                >
                    <iron-icon icon="vaadin:pin"></iron-icon>
                </vaadin-button>
            </a>
        `;
    }
    
    firstUpdated() {
        (this.shadowRoot.querySelector("favorite-star") as any).toggleFavorite = () => (this as any).$server.onFavoriteToggled();
    }

    handleRowClicked(event) {
        event.preventDefault();
        (this as any).$server.handleRowClicked();
    }

    onCompareButtonClicked(event) {
        event.preventDefault();
        event.stopPropagation();
        (this as any).$server.onCompareButtonClicked();
        setTimeout(function() {
            window.dispatchEvent(new Event('resize'));
        }, 600);
    }
}

customElements.define("experiments-navbar-item", ExperimentsNavbarItem);
