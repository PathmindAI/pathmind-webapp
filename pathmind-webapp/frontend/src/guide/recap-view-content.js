import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class RecapViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Export as a Standalone Java App</h1>
        <p>
          After a successful test run,
          <a
            href="https://help.anylogic.com/index.jsp?topic=%2Fcom.anylogic.help%2Fhtml%2Fstandalone%2FExport_Java_Application.html"
            target="_blank"
          >
            export the model</a
          >
          as a standalone Java application.
        </p>
        <h4>Example from Finding the Cheese Tutorial</h4>
        <p class="screenshot-description">
          Exported folders should contain the following items.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/export_files.png" />
        </div>
        <vaadin-button id="nextBtn" theme="secondary">
          Let’s upload a model!
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back
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
