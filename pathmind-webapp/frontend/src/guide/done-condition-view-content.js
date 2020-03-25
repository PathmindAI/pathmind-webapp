import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class DoneConditionViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>isDone</h1>
        <p>
          A simulation has reached the end when isDone occurs. Some models will
          complete after a specified amount of time while others will end only
          when certain conditions are met. Determine what elements should
          trigger your model to end, and update the Done field.
        </p>
        <h4>Example from Finding the Cheese Tutorial</h4>
        <p class="screenshot-description">
          The simulation uses a checkIfGoalReach function to end at the moment
          that the mouse finds the cheese.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/isDone_code.png" />
        </div>
        <a href$="[[readMoreLink]]" target="_blank">Learn more about isDone</a>
        <vaadin-button id="nextBtn" theme="secondary">
          Mark Step 5 as complete
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "done-condition-view-content";
  }

  static get properties() {
    return {
      readMoreLink: {
        value() {
          return "https://help.pathmind.com/en/articles/3644763-5-define-done-condition";
        }
      }
    };
  }
}

customElements.define(DoneConditionViewContent.is, DoneConditionViewContent);
