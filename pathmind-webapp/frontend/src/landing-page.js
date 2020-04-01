import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-ordered-layout/src/vaadin-vertical-layout.js";

class LandingPage extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;
          height: 100%;
        }
      </style>
      <vaadin-vertical-layout style="width: 100%; height: 100%;">
        <h3>Landing Page</h3>
      </vaadin-vertical-layout>
    `;
  }

  static get is() {
    return "landing-page";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(LandingPage.is, LandingPage);
