import { LitElement, html, css } from "lit-element";

import "./chart-placeholder.ts";

class TrainingStartingPlaceholder extends LitElement {
    static get styles() {
        return css`
            :host {
                box-sizing: border-box;
                justify-content: center;
                align-items: center;
                flex: 1 1 calc(40vh + 3.7rem);
                position: relative;
                width: 100%;
                background-color: var(--pm-highlight-light);
                padding: var(--lumo-space-m) var(--lumo-space-xxl);
            }
            chart-placeholder {
                position: absolute;
                width: 100%;
                height: 100%;
                top: 0;
                left: 0;
                opacity: 0.4;
            }
            vaadin-vertical-layout {
                justify-content: center;
                position: relative;
                max-width: 640px;
                width: 100%;
                height: 100%;
                margin: auto;
            }
            span {
                display: flex;
                position: relative;
                font-size: var(--lumo-font-size-l);
                font-weight: bold;
                line-height: 1;
                margin-bottom: 0.75em;
            }
            loading-spinner {
                margin-right: .25em;
            }`;
    }
    render() {
        return html`
            <chart-placeholder></chart-placeholder>
            <vaadin-vertical-layout>
                <span><loading-spinner></loading-spinner>Starting the training…</span>
                <p>You’ll see the first results here within a few minutes.</p>
                <p>The Pathmind training process starts a cluster to explore multiple combinations of hyperparameters automatically. We train the policy for your simulation using the best configuration.</p>
                <p>Training may take up to a few hours. We’ll send you an email when it’s complete!</p>
            </vaadin-vertical-layout>
        `;
    }
}

customElements.define("training-starting-placeholder", TrainingStartingPlaceholder);