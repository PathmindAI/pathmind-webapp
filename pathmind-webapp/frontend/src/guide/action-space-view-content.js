import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class ActionSpaceViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Actions</h1>
        <p>
          Determine what actions are possible for an agent to take within your
          model and how many exist. Fill in the action function accordingly.
        </p>
        <h4>Example from Finding the Cheese Tutorial</h4>
        <p>
          There are only four possible actions that the agent can perform in
          this model: moving left, right, up, and down.
        </p>
        <pre><code>switch (action){
    case 0:
        PUSH_UP();
        break;
    case 1:
        PUSH_RIGHT();
        break;
    case 2:
        PUSH_DOWN();
        break;
    case 3:
        PUSH_LEFT();
        break;
}</code></pre>
        <p>
          <a
            href="https://help.pathmind.com/en/articles/3640124-3-build-action-space"
            rel="nofollow noopener noreferrer"
            target="_blank"
          >
            Learn more about Actions
          </a>
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          Mark Step 1 as complete
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "action-space-view-content";
  }

  static get properties() {
    return {};
  }
}

customElements.define(ActionSpaceViewContent.is, ActionSpaceViewContent);
