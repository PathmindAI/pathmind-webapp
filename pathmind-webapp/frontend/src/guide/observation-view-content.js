import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class ObservationViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Observations</h1>
        <p>
          Observations provide information on the current state of the
          environment. Note which elements need to be observed to determine
          success in your model and write the observation function.
        </p>
        <h4>Example from Finding the Cheese Tutorial</h4>
        <p>
          This tutorial has four observations that monitor each row and column
          and determine how far the agent is from the goal.
        </p>
        <pre><code>double obs [] = new double[] {<br>  mouse.row,<br>  mouse.col.<br>  abs(4-mouse.row), // distance between final goal<br>  abs(4-mouse.col)<br>}</code></pre>
        <a href$="[[readMoreLink]]" target="_blank"
          >Learn more about Observations</a
        >
        <vaadin-button id="nextBtn" theme="secondary">
          Mark Step 3 as Complete
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "observation-view-content";
  }

  static get properties() {
    return {
      readMoreLink: {
        value() {
          return "https://help.pathmind.com/en/articles/3631436-2-build-observation-space";
        }
      }
    };
  }
}

customElements.define(ObservationViewContent.is, ObservationViewContent);
