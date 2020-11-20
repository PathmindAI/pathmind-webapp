import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class EmptyDashboardPlaceholder extends PolymerElement {
    static get is() {
        return "empty-dashboard-placeholder";
    }

    static get template() {
        return html`
            <style>
                empty-dashboard-placeholder {
                    width: 100%;
                }
                empty-dashboard-placeholder .panel-wrapper {
                    justify-content: flex-start;
                    align-items: center;
                    margin-top: var(--lumo-space-l);
                }
                empty-dashboard-placeholder .panel-wrapper h3 {
                    margin: 0;
                }
                empty-dashboard-placeholder vaadin-horizontal-layout {
                    flex-wrap: wrap;
                    justify-content: center;
                    align-items: center;
                    margin: var(--lumo-space-xxl) auto;
                }
                empty-dashboard-placeholder .logo {
                    margin-bottom: calc(var(--lumo-space-xxl) + var(--lumo-space-m));
                }
                empty-dashboard-placeholder .button-link {
                    font-size: var(--lumo-font-size-l);
                    padding: var(--lumo-space-l);
                    margin: var(--lumo-space-m);
                    border: 1px solid transparent;
                }
                empty-dashboard-placeholder li {
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
                    <a class="button-link" id="newProjectButton" router-link href="newProject">Create Your First Project Now</a>
                </vaadin-horizontal-layout>
            </vaadin-vertical-layout>
        `;
    }

    _attachDom(dom) {
      this.appendChild(dom);
    }
}

customElements.define(EmptyDashboardPlaceholder.is, EmptyDashboardPlaceholder);