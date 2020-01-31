import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class DoneConditionViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Define "Done" Condition</h1>
        <p>
          You need to define the length of your simulation’s run and when it
          finishes. That length is known as an episode. Some episodes are
          defined in terms of time spent (e.g. end the episode after a “day”),
          while others end when the simulation meets a certain condition. In the
          Pathmind Helper in AnyLogic, <b>isDone</b> sets the length of your
          simulation.
        </p>
        <p>
          <a href$="[[readMoreLink]]" target="_blank"
            >Learn more about defining "Done" Condition</a
          >
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperDone.png" />
        </div>
        <vaadin-button id="nextBtn" theme="secondary">
          “Done” Condition Defined
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Triggering Actions
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
