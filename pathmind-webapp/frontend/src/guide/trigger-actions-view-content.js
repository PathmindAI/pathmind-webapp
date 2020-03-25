import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class TriggerActionsViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Event Trigger</h1>
        <p>
          Next, create an event trigger to tell Pathmind when triggerNextAction
          should occur, or when an agent should move to the next action.
        </p>
        <p>
          Event triggers can be set to occur cyclically, conditionally, or
          within an AnyLogic block. Cyclic event triggers set actions to occur
          at a predetermined rate of time while conditional event triggers only
          fire when a specific condition becomes true. AnyLogic block event
          triggers allow actions to occur when an agent enters a specific block
          in the model.
        </p>
        <h4>
          Example from Finding the Cheese Tutorial
        </h4>
        <p class="screenshot-description">
          The mouse takes one step, or action, every one second.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/event_trigger.png" />
        </div>
        <p>
          Now that the actions and event trigger are defined, run a test to
          check your progress.
        </p>
        <ul>
          <li>Open the Pathmind Helper properties</li>
          <li>Check Enabled</li>
          <li>Check Use Random Actions</li>
          <li>Check Debug Mode</li>
          <li>Run your simulation</li>
          <li>Look for any syntax errors</li>
          <li>
            Open the developer panel and notice the defined actions firing
            randomly
          </li>
        </ul>
        <p>
          <a href$="[[readMoreLink]]" target="_blank"
            >Learn more about Event Trigger</a
          >
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          Mark Step 4 as complete
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back
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
