import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class EmptyDashboardPlaceholder extends PolymerElement {
    static get is() {
        return "empty-dashboard-placeholder";
    }

    static get template() {
        return html`
            <style include="shared-styles pathmind-dialog-view">
                a {
                    text-decoration: none;
                }
                .panel-wrapper {
                    justify-content: flex-start;
                    align-items: center;
                }
                vaadin-horizontal-layout {
                    flex-wrap: wrap;
                    justify-content: center;
                    align-items: center;
                    margin: var(--lumo-space-m);
                }
                .logo {
                    margin-bottom: calc(var(--lumo-space-xxl) * 2);
                }
                .button-link {
                    margin: var(--lumo-space-m);
                }
                .create-project-link {
                    color: var(--lumo-primary-text-color);
                    margin-left: 0.4em;
                }
                .create-project-link:hover {
                    text-decoration: underline;
                }
            </style>
            <vaadin-vertical-layout class="panel-wrapper">
              <span class="welcome-text">Welcome to</span>
              <img
                class="logo"
                src="frontend/images/pathmind-logo.svg"
                alt="Pathmind"
              />
                <vaadin-horizontal-layout>
                    <span class="section-title-label">Let's begin by opening the</span>
                    <a class="button-link" href="https://help.pathmind.com/en/articles/4004788-getting-started" target="_blank">Getting Started Guide</a>
                </vaadin-horizontal-layout>
                <vaadin-horizontal-layout>
                    <span>or skip ahead to </span>
                    <a class="create-project-link" router-link="" href="newProject">create your first project.</a>
                </vaadin-horizontal-layout>
            </vaadin-vertical-layout>
        `;
    }
}

customElements.define(EmptyDashboardPlaceholder.is, EmptyDashboardPlaceholder);