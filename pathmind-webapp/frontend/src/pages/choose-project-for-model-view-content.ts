import { LitElement, html, property } from "lit-element";
import { ComboBoxElement, ComboBoxItemModel, ComboBoxRenderer } from '@vaadin/vaadin-combo-box/vaadin-combo-box';

class ChooseProjectForModelViewContent extends LitElement {

  @property({type: Boolean})
  isCreateNewProject = false;

  render() {
    return html`
    <style>
        choose-project-for-model-view-content vaadin-vertical-layout {
            align-items: stretch;
        }
        choose-project-for-model-view-content p {
            margin: var(--lumo-space-xs) 0;
        }
        choose-project-for-model-view-content .panel-wrapper {
            margin-top: var(--lumo-space-l);
        }
        choose-project-for-model-view-content .panel-wrapper h3 {
            margin: 0 0 var(--lumo-space-l);
        }
        choose-project-for-model-view-content .inner-content {
            text-align: left;
        }
        choose-project-for-model-view-content vaadin-combo-box {
            margin-bottom: var(--lumo-space-m);
        }
        choose-project-for-model-view-content vaadin-text-field {
            padding-top: 0;
        }
        choose-project-for-model-view-content #submitButton {
            margin-top: var(--lumo-space-xxl);
        }
    </style>
    <vaadin-horizontal-layout class="panel-wrapper">
      <div class="content">
        <vaadin-vertical-layout class="inner-content">
          <h3>Choose a Project for your Model</h3>
          <p>Model uploaded. Now let's assign it to a project.</p>
        <vaadin-combo-box
            id="projectDropdown"
            placeholder="Choose a project"
            error-message="Please choose a project"
            required
            @change="${this.selectOnChange}"
        ></vaadin-combo-box>
        <vaadin-text-field
            id="projectName"
            label="New Project Name"
            ?hidden="${!this.isCreateNewProject}"
        ></vaadin-text-field>
        <vaadin-button id="submitButton" theme="primary" @click="${this.handleSubmitButtonClicked}">
            Next
        </vaadin-button>
        </vaadin-vertical-layout>
      </div>
    </vaadin-horizontal-layout>`;
  }

  createRenderRoot() {
    return this;
  }

  firstUpdated() {  

    customElements.whenDefined('vaadin-combo-box').then(() => {
        const comboBox: ComboBoxElement = this.querySelector("#projectDropdown");
        comboBox.renderer = function(root, owner, model: ComboBoxItemModel) {
          const itemLabel = (model.item as any).label;
          if (itemLabel === "Create a New Project") {
            root.innerHTML = `<vaadin-button theme='primary small' style='width: 100%;'>${itemLabel}</vaadin-button>`;
          } else {
            root.innerHTML = itemLabel;
          }
        }
        comboBox.addEventListener("value-changed", (event: CustomEvent) => {
          if (event.detail.value == 1) { // Create a New Project
              this.isCreateNewProject = true;
          } else {
              this.isCreateNewProject = false;
          }
        });
    });

      const projectNameTextField: HTMLElement = this.querySelector("#projectName");
      projectNameTextField.focus();
      projectNameTextField.addEventListener("keyup", (event: KeyboardEvent) => {
          if (event.keyCode === 13) { // Enter key
            event.preventDefault();
            (this.querySelector("#submitButton") as HTMLElement).click();
          }
      });
  }

  selectOnChange(event) {
      (this as any).$server.selectOnChange();
  }

  handleSubmitButtonClicked() {
      (this as any).$server.handleSubmitButtonClicked();
  }
}

customElements.define("choose-project-for-model-view-content", ChooseProjectForModelViewContent);
