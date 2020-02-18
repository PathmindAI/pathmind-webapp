import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class TriggerActionsViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Triggering Actions</h1>
        <p class="screenshot-description">
          Once you define your action space, you will need to create an event
          trigger. The event trigger tells Pathmind when to compute the next
          action.
        </p>
        <p>
          <a href$="[[readMoreLink]]" target="_blank"
            >Learn more about Triggering Actions</a
          >
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperTriggerAction3.png" />
        </div>
        <vaadin-button id="nextBtn" theme="secondary">
          Completed triggerNextAction
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Build Action Space
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "trigger-actions-view-content";
  }

  static get properties() {
    return {
      readMoreLink: {
        value() {
          return "https://help.pathmind.com/en/articles/3634254-4-triggering-actions";
        }
      }
    };
  }
}

customElements.define(TriggerActionsViewContent.is, TriggerActionsViewContent);
