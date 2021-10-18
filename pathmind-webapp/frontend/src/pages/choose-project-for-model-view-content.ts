import { LitElement, html, property } from "lit-element";
import { ComboBoxElement, ComboBoxItemModel, ComboBoxRenderer } from '@vaadin/vaadin-combo-box/vaadin-combo-box';

class ChooseProjectForModelViewContent extends LitElement {

  @property({type: Boolean})
  isCreateNewProject = false;
  @property({type: Boolean, reflect: true, attribute: "is-dialog"})
  isDialog = false;
  @property({type: String})
  projectNotes = "";

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
        choose-project-for-model-view-content[is-dialog] .panel-wrapper {
          min-width: 400px;
          background: transparent;
          padding: 0;
          margin-top: 0; 
        }
        choose-project-for-model-view-content .panel-wrapper h3 {
            margin: 0 auto var(--lumo-space-l);
        }
        choose-project-for-model-view-content .inner-content {
            text-align: left;
        }
        choose-project-for-model-view-content[is-dialog] .inner-content {
          padding: 0;
          margin-top: 0;
          box-shadow: none;
        }
        choose-project-for-model-view-content vaadin-combo-box {
            margin-bottom: var(--lumo-space-m);
        }
        choose-project-for-model-view-content vaadin-text-field {
          padding-top: 0;
          margin-bottom: var(--lumo-space-m);
        }
        choose-project-for-model-view-content #submitButton {
            margin-top: var(--lumo-space-xxl);
        }
        choose-project-for-model-view-content[is-dialog] #submitButton {
          margin-top: var(--lumo-space-l);
        }
        choose-project-for-model-view-content .project-notes-wrapper p {
          max-height: 10rem;
          overflow: auto;
        }
        choose-project-for-model-view-content h4 {
          margin-top: 0;
        }
    </style>
    <vaadin-horizontal-layout class="panel-wrapper">
      <div class="content">
        <vaadin-vertical-layout class="inner-content">
          <h3>${
            this.isDialog ? 
            "Move Model to Project"
            : "Choose a Project for your Model"
          }</h3>
          <p ?hidden="${this.isDialog}">Model uploaded. Now let's assign it to a project.</p>
          <vaadin-combo-box
              id="projectDropdown"
              placeholder="Choose a project"
              error-message="Please choose a project"
              required
          ></vaadin-combo-box>
          <vaadin-text-field
              id="projectName"
              label="New Project Name"
              ?hidden="${!this.isCreateNewProject}"
          ></vaadin-text-field>
          <vaadin-vertical-layout class="project-notes-wrapper" ?hidden="${this.projectNotes.length <= 0}">
            <h4>Project Notes</h4>
            <p>${this.projectNotes}</p>
          </vaadin-vertical-layout>
          <vaadin-button id="submitButton" theme="primary" @click="${this.handleSubmitButtonClicked}">
            ${this.isDialog ? "Done" : "Next"}
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
          if (event.detail.value == 2) { // Create a New Project
              this.isCreateNewProject = true;
          } else {
              this.isCreateNewProject = false;
          }
          (this as any).$server.selectedProjectChanged();
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

  handleSubmitButtonClicked() {
      (this as any).$server.handleSubmitButtonClicked();
  }
}

customElements.define("choose-project-for-model-view-content", ChooseProjectForModelViewContent);
