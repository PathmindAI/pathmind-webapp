import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class DoneConditionViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Define "Done" Condition</h1>
        <div class="spacing"></div>
        <p>
          <b>isDone</b> sets the length of your simulation. In reinforcement
          learning terminology, the length of your simulation is also known as
          an "episode".
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperDone.png" />
        </div>
        <p>
          Some episodes are defined in terms of time spent (e.g. end the episode
          after a "day"), while others end when the simulation meets a certain
          condition (e.g. the character in a video game dies).
        </p>
        <p>
          Shorter episodes tend to be better as they provide faster feedback,
          which helps the reinforcement learning algorithm learn. We recommend
          limiting episodes to less than 100,000 steps if possible. That said,
          some use cases require more than 100,000 steps per episode.
        </p>
        <p>
          Follow <a href="javascript:void(0);">these instructions</a> to audit
          your episode length.
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          Install Pathmind Helper
        </vaadin-button>
        <vaadin-button id="skipToUploadModelBtn" theme="tertiary">
          Skip to Upload Model
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "done-condition-view-content";
  }

  static get properties() {
    return {};
  }
}

customElements.define(DoneConditionViewContent.is, DoneConditionViewContent);
