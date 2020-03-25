import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class RunTestViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Run a Test</h1>
        <p>
          Now that all the Pathmind Helper properties have been defined,
          complete a test run to ensure that everything is set up properly.
        </p>
        <ul>
          <li>Open the Pathmind Helper properties</li>
          <li>Check Enabled</li>
          <li>Check Use Random Actions</li>
          <li>Check Debug Mode</li>
          <li>Run the simulation, checking for compilation errors</li>
          <li>Open the developer panel</li>
          <li>
            Observe the debugging logs and make sure they match your
            expectations
          </li>
          <li>Enable virtual speed to check for errors</li>
        </ul>
        <vaadin-button id="nextBtn" theme="secondary">
          Mark Step 8 as complete
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "run-test-view-content";
  }

  static get properties() {
    return {};
  }
}

customElements.define(RunTestViewContent.is, RunTestViewContent);
