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
          the environment.
        </p>
        <p>
          For example, the barista in the Coffee Shop can:
        </p>
        <ol>
          <li>See the number of people waiting at the door</li>
          <li>See how dirty the kitchen is</li>
        </ol>
        <p>
          Code example:
        </p>
        <pre><code>Double obs [] = new Double[] {<br>  getOrder.size(), // number of people waiting at the door<br>  kitchenCleanliness // Kitchen cleanliness level<br>}</code></pre>
        <p>
          <a href$="[[helpArticleLink]]" target="_blank"
            >Learn more about building an Observation Space</a
          >
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
          return "https://app.intercom.io/a/apps/byklc7u3/articles/articles/3631436/show";
        }
      }
    };
  }
}

customElements.define(ObservationViewContent.is, ObservationViewContent);
