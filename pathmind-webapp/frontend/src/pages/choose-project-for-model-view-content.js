import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class ChooseProjectForModelViewContent extends PolymerElement {
  static get template() {
    return html`
    <style include="shared-styles pathmind-dialog-view">
        vaadin-vertical-layout {
            align-items: stretch;
        }
        p {
            margin: var(--lumo-space-xs) 0;
        }
        .panel-wrapper {
            margin-top: var(--lumo-space-l);
        }
        .panel-wrapper h3 {
            margin: 0 0 var(--lumo-space-l);
        }
        .inner-content {
            text-align: left;
        }
        .inner-content > vaadin-vertical-layout {
            margin-bottom: var(--lumo-space-xl);
        }
        vaadin-select,
        vaadin-text-field {
            margin-bottom: var(--lumo-space-xl);
        }
        vaadin-text-field {
            padding-top: 0;
        }
    </style>
    <vaadin-horizontal-layout class="panel-wrapper">
      <div class="content">
        <vaadin-vertical-layout class="inner-content">
          <h3>Choose the Project for Your Model</h3>
          <p>Model uploaded. Now let's assign it to a project.</p>
          <vaadin-vertical-layout hidden="{{showCreateNewProjectSection}}">
            <vaadin-combo-box
                id="projectDropdown"
                placeholder="Choose an existing project"
                error-message="Please choose a project"
                required
                on-change="selectOnChange"
            ></vaadin-combo-box>
            <p>Alternatively, you may choose to create a new project for this model.</p>
            <vaadin-button id="switchToCreateProjectButton" theme="tertiary-inline" on-click="switchToCreateProjectButtonClicked">
              Add to New Project
            </vaadin-button>
          </vaadin-vertical-layout>
          <vaadin-vertical-layout hidden="{{!showCreateNewProjectSection}}">
            <vaadin-text-field
                id="projectName"
                label="New Project Name"
                required
            ></vaadin-text-field>
            <vaadin-button id="switchToExistingProjectButton" theme="tertiary-inline" on-click="switchToExistingProjectButtonClicked">
                Add to Existing Project
            </vaadin-button>
          </vaadin-vertical-layout>
        <vaadin-button id="submitButton" theme="primary" on-click="handleSubmitButtonClicked">
            Next
        </vaadin-button>
        </vaadin-vertical-layout>
      </div>
    </vaadin-horizontal-layout>`;
  }

  ready() {
      super.ready();
      const projectNameTextField = this.$.projectName;
      projectNameTextField.focus();
      projectNameTextField.addEventListener("keyup", event => {
          if (event.keyCode === 13) { // Enter key
            event.preventDefault();
            this.$.submitButton.click();
          }
      });
  }

  switchToCreateProjectButtonClicked() {
    this.showCreateNewProjectSection = true;
  }

  switchToExistingProjectButtonClicked() {
    this.showCreateNewProjectSection = false;
  }

  static get is() {
    return "choose-project-for-model-view-content";
  }

  static get properties() {
    return {
      showCreateNewProjectSection: {
        type: Boolean,
        value: false,
      },
    };
  }
}

customElements.define(ChooseProjectForModelViewContent.is, ChooseProjectForModelViewContent);
