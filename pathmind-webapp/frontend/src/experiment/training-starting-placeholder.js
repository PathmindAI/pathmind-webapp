import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

import "./chart-placeholder.js";

class TrainingStartingPlaceholder extends PolymerElement {
    static get is() {
        return "training-starting-placeholder";
    }

    static get template() {
        return html`
            <style>
                :host {
                    box-sizing: border-box;
                    justify-content: center;
                    align-items: center;
                    flex: 1 0 100%;
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
                    display: block;
                    position: relative;
                    font-size: var(--lumo-font-size-l);
                    font-weight: bold;
                    line-height: 1;
                    padding-left: 2rem;
                    margin-bottom: 0.75em;
                }
                /* TODO: change to loading-spinner */
                span::before { 
                    box-sizing: border-box;
                    content: "";
                    position: absolute;
                    width: var(--lumo-size-xs);
                    height: var(--lumo-size-xs);
                    top: -0.2rem !important;
                    left: 0;
                    border: 2px solid var(--pm-grey-color-light);
                    border-radius: 50%;
                    border-top: 2px solid var(--pm-primary-color);
                    -webkit-animation: spin 2s linear infinite; /* Safari */
                    animation: spin 2s linear infinite;
                }
                /* Safari */
                @-webkit-keyframes spin {
                    0% {
                        -webkit-transform: rotate(0deg);
                    }
                    100% {
                        -webkit-transform: rotate(360deg);
                    }
                }
                @keyframes spin {
                    0% {
                        transform: rotate(0deg);
                    }
                    100% {
                        transform: rotate(360deg);
                    }
                }
            </style>
            <chart-placeholder></chart-placeholder>
            <vaadin-vertical-layout>
                <span>Starting the training…</span>
                <p>You’ll see the first results here within a few minutes.</p>
                <p>The Pathmind training process starts a cluster to explore multiple combinations of hyperparameters automatically. We train the policy for your simulation using the best configuration.</p>
                <p>Training may take up to a few hours. We’ll send you an email when it’s complete!</p>
            </vaadin-vertical-layout>
        `;
    }
}

customElements.define(TrainingStartingPlaceholder.is, TrainingStartingPlaceholder);