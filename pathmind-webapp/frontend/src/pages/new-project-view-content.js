import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class NewProjectView extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles pathmind-dialog-view">
        p {
            text-align: left;
        }
    </style>
    <vaadin-horizontal-layout class="panel-wrapper">
      <div class="content">
        <span class="welcome-text">Welcome to</span>
        <img
          class="logo"
          src="frontend/images/pathmind-logo.svg"
          alt="Pathmind logo"
        />
        <vaadin-vertical-layout class="inner-content">
          <h3>Start a New Project!</h3>
          <p>Projects organize your Pathmind Experiments based on your AnyLogic model</p>
          <vaadin-text-field
            id="projectName"
            label="Give your project a name"
          ></vaadin-text-field>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button id="createProject" theme="primary" on-click="handleNewProjectClicked">
              Create Project
            </vaadin-button>
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </div>
    </vaadin-horizontal-layout>`;
  }

  ready() {
      super.ready();
      const projectNameTextField = this.shadowRoot.querySelector("#projectName");
      const createProjectButton = this.shadowRoot.querySelector("#createProject");
      projectNameTextField.focus();
      projectNameTextField.addEventListener("keyup", event => {
          if (event.keyCode === 13) { // Enter key
            event.preventDefault();
            createProjectButton.click();
          }
      });
  }

  static get is() {
    return "new-project-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(NewProjectView.is, NewProjectView);
