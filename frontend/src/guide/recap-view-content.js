import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class RecapViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Conclusion / Recap</h1>
        <p>
          Once you have completed the steps above, you are ready to upload and
          train your model with reinforcement learning. You will probably want
          to experiment with different reward variables, reward shapes, and
          observations. Pathmind allows you to track and compare those
          experiments.
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          Let’s upload a model!
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Define Reward Variables
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "recap-view-content";
  }

  static get properties() {
    return {};
  }
}

customElements.define(RecapViewContent.is, RecapViewContent);
