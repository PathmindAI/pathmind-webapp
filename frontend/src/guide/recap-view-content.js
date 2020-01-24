import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class RecapViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view">
        p {
          font-size: var(--lumo-font-size-xl);
        }
      </style>
      <div class="content">
        <h1>Conclusion / Recap</h1>
        <p>
          We’ve covered the Pathmind Key Concept and definitions dictumst
          vestibulum rhoncus est pellentesque elit ullamcorper. Ullamcorper
          dignissim cras tincidunt lobortis feugiat vivamus at. Est ante in nibh
          mauris cursus mattis molestie a iaculis. Faucibus scelerisque eleifend
          donec pretium vulputate sapien nec sagittis aliquam. Ultricies leo
          integer malesuada nunc vel risus. Massa tincidunt dui ut ornare lectus
          sit. Vulputate sapien nec sagittis aliquam malesuada bibendum arcu
          vitae. Faucibus scelerisque eleifend donec pretium vulputate. Platea
          dictumst vestibulum rhoncus est pellentesque elit ullamcorper
          dignissim cras.
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
