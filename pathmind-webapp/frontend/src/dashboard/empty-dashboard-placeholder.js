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
                .panel-wrapper h3 {
                    margin: 0;
                }
                vaadin-horizontal-layout {
                    flex-wrap: wrap;
                    justify-content: center;
                    align-items: center;
                    margin: var(--lumo-space-xxl) auto;
                }
                .logo {
                    margin-bottom: calc(var(--lumo-space-xxl) * 2);
                }
                .button-link {
                    font-size: var(--lumo-font-size-l);
                    padding: var(--lumo-space-l);
                    margin: var(--lumo-space-m);
                    border: 1px solid transparent;
                }
                a {
                    color: var(--lumo-primary-text-color);
                }
                a:hover {
                    text-decoration: underline;
                }
                li {
                    margin: var(--lumo-space-s);
                }
            </style>
            <vaadin-vertical-layout class="panel-wrapper">
              <span class="welcome-text">Welcome to</span>
              <img
                class="logo"
                src="frontend/images/pathmind-logo.svg"
                alt="Pathmind"
              />
                <vaadin-vertical-layout>
                    <h3>Using AI may be easier than you think:</h3>
                    <ul>
                        <li>Upload a zip file of the simulation model to Pathmind. <a href="https://s3.amazonaws.com/public-pathmind.com/SimpleStochasticPathmindDemo.zip" download>Download zip file</a></li>
                        <li>Write a reward function (It's simple, just copy and paste this: reward = after.goalReached - 0.1; ).</li>
                        <li>Once training is complete, click on “Export Policy”.</li>
                        <li>Load the trained AI into AnyLogic to see it perform.</li>
                    </ul>
                    <i>(For more detailed information, please see <a href="http://help.pathmind.com/en/articles/4540076-getting-started-with-simple-stochastic" target="_blank">our tutorial</a>.)</i>
                </vaadin-vertical-layout>
                <vaadin-horizontal-layout>
                    <a class="button-link" router-link href="newProject">Create Your First Project Now</a>
                </vaadin-horizontal-layout>
            </vaadin-vertical-layout>
        `;
    }
}

customElements.define(EmptyDashboardPlaceholder.is, EmptyDashboardPlaceholder);