import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class ObservationViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Build Observation Space</h1>
        <p>
          An observation space provides information about the current state of
          the environment. For example, if you were monitoring the state of a
          drone in flight, you might track its speed, acceleration, altitude,
          x-y coordinates and remaining battery life.
        </p>
        <p>
          There are many ways to construct your observation space in AnyLogic.
          Below is a simple case for the purpose of demonstration.
        </p>
        <p>
          First, create a new function and assign it the name
          <code>getObservation</code>.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/getObservation.png" />
        </div>
        <p>
          Next, click the <code>getObservation</code> function to inspect its
          properties.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/getObservationProperties.png" />
        </div>
        <h3>
          Step 1
          <span class="slim"
            >- Set the return value to a two-dimensional array of doubles.</span
          >
        </h3>
        <ul>
          <li>Select the <code>Returns value</code> radio button.</li>
          <li>
            Set the <code>Type</code> to <code>Other...</code> and input
            <code>double[][]</code>
          </li>
        </ul>
        <h3>
          Step 2 <span class="slim">- Write your observation function.</span>
        </h3>
        <p>
          In the example below, we construct <code>getObservation</code> to
          return observations for a model containing two agents.
        </p>
        <pre><code>// Initialize your 2-dimensional double array<br>double[][] obs = new double[2][4]; //[2] = 2 agents, [4] = 4 observations per agent<br><br>// Observations for Agent 0<br>obs[0][0] = obs1;<br>obs[0][1] = obs2;<br>obs[0][2] = obs3;<br>obs[0][3] = obs4;<br><br>// Observations for Agent 1<br>obs[1][0] = obs1;<br>obs[1][1] = obs2;<br>obs[1][2] = obs3;<br>obs[1][3] = obs4;<br><br>return obs;</code></pre>
        <p>
          This should output an array that looks something like:
          <code>[[0.0, 0.0, 0.0, 0.0], [1.0, 1.0, 1.0, 1.0]]</code> where index
          0 (<code>[0.0, 0.0, 0.0, 0.0]</code> ) corresponds with Agent 0 and
          index 1 (<code>[1.0, 1.0, 1.0, 1.0]</code> ) corresponds with Agent 1.
          You may extend this format to as many agents as you need by
          reinitializing the <code>obs</code> array with the correct number of
          agents.
        </p>
        <p>
          The values <code>0.0</code> and <code>1.0</code> are just
          placeholders; in actuality, they will contain varied values from your
          observations. The variables <code>obs0</code> ,
          <code>obs1, obs2</code> and so forth would each hold information about
          your state.
        </p>
        <h3>
          Step 3
          <span class="slim"
            >- Add <code>getObservation</code> to the Pathmind Helper</span
          >
        </h3>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/getObservationPathmindHelper.png" />
        </div>
        <p>
          Finally, you can inspect your <code>getObservation</code> by following
          the instructions
          <a href$="[[helpArticleLink]]" target="_blank">here</a>. <br />
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          Completed Building Observation Space
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Pathmind Helper
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "observation-view-content";
  }

  static get properties() {
    return {
      helpArticleLink: {
        value() {
          return "https://help.pathmind.com/en/articles/3631367-troubleshooting-pathmind-helper";
        }
      }
    };
  }
}

customElements.define(ObservationViewContent.is, ObservationViewContent);
