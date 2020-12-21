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
        vaadin-combo-box {
            margin-bottom: var(--lumo-space-l);
        }
        vaadin-text-field {
            padding-top: 0;
        }
        #submitButton {
            margin-top: var(--lumo-space-xxl);
        }
    </style>
    <vaadin-horizontal-layout class="panel-wrapper">
      <div class="content">
        <vaadin-vertical-layout class="inner-content">
          <h3>Choose the Project for Your Model</h3>
          <p>Model uploaded. Now let's assign it to a project.</p>
          <vaadin-vertical-layout>
            <vaadin-combo-box
                id="projectDropdown"
                placeholder="Choose a project"
                error-message="Please choose a project"
                required
                on-change="selectOnChange"
            ></vaadin-combo-box>
          </vaadin-vertical-layout>
          <vaadin-vertical-layout hidden="{{!isCreateNewProject}}">
            <vaadin-text-field
                id="projectName"
                label="New Project Name"
            ></vaadin-text-field>
          </vaadin-vertical-layout>
        <vaadin-button id="submitButton" theme="primary" on-click="handleSubmitButtonClicked">
            Next
        </vaadin-button>
        </vaadin-vertical-layout>
      </div>
    </vaadin-horizontal-layout>`;
  }

  constructor() {
      super();

      customElements.whenDefined('vaadin-combo-box').then(() => {
          const comboBox = this.$.projectDropdown;
          comboBox.renderer = function(root, owner, model) {
              if (model.item.label === "Create a New Project") {
                root.innerHTML = `<vaadin-button theme='primary small' style='width: 100%;'>${model.item.label}</vaadin-button>`;
              } else {
                root.innerHTML = model.item.label;
              }
          }
          comboBox.addEventListener("value-changed", (event) => {
            if (event.target.value == 1) { // Create a New Project
                this.isCreateNewProject = true;
            } else {
                this.isCreateNewProject = false;
            }
          });
      });
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

  selectOnChange(event) {
  }

  static get is() {
    return "choose-project-for-model-view-content";
  }

  static get properties() {
    return {
      isCreateNewProject: {
        type: Boolean,
        value: false,
      },
    };
  }
}

customElements.define(ChooseProjectForModelViewContent.is, ChooseProjectForModelViewContent);
