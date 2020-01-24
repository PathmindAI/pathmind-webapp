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
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperTriggerAction3.png" />
        </div>
        <p>
          The event trigger can be configured within the Pathmind Helper
          properties.
        </p>
        <p>
          <b>Use Pathmind Event Trigger </b>- Enable or disable with the check
          box.
        </p>
        <p>
          <b>Recurrence</b> - The frequency at which the event condition will be
          checked. One time per second is the default.
        </p>
        <p>
          <b>Event Condition</b> - Must return <code>true</code> or
          <code>false</code>.
        </p>
        <h3>Option 1 - Trigger Actions Cyclically</h3>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/triggerNextActionTrue.png" />
        </div>
        <p>
          Let's say that the event condition is set to <code>true</code> as
          shown in the screenshot above. This means that an agent will try to
          perform an action every second.
        </p>
        <h3>Option 2 - Trigger Actions Conditionally</h3>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperTriggerAction.png" />
        </div>
        <p>
          You can also trigger an action based on a condition (<code>true</code>
          or <code>false</code>). The recurrence time determines how often this
          condition is checked. An action is triggered when the criteria that
          you define here are met.
        </p>
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
    return {};
  }
}

customElements.define(TriggerActionsViewContent.is, TriggerActionsViewContent);
