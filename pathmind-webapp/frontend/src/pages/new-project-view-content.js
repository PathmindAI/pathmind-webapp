import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class NewProjectView extends PolymerElement {
  static get template() {
    return html`
    <style>
        new-project-view p,
        new-project-view vaadin-text-field {
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
            required
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

  _attachDom(dom) {
    this.appendChild(dom);
  }

  ready() {
      super.ready();
      const projectNameTextField = this.$.projectName;
      projectNameTextField.focus();
      projectNameTextField.addEventListener("keyup", event => {
          if (event.keyCode === 13) { // Enter key
            event.preventDefault();
            this.$.createProject.click();
          }
      });
  }

  static get is() {
    return "new-project-view";
  }
}

customElements.define(NewProjectView.is, NewProjectView);
