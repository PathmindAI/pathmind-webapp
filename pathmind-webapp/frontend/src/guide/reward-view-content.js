import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class RewardViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Reward Variables</h1>
        <p>
          Reward Variables help determine how well an agent performed by giving
          a reward, point, or score based on the agent’s actions. Pathmind
          trains the agent to earn as many points as possible.
        </p>
        <h4>Example from Finding the Cheese Tutorial</h4>
        <p>
          The goal of this tutorial is simple: find the cheese. The mouse
          receives one reward when it reaches that goal.
        </p>
        <a
          href="https://help.pathmind.com/en/articles/3640175-6-define-reward-variables"
          rel="nofollow noopener noreferrer"
          target="_blank"
        >
          Learn more about Reward Variables
        </a>
        <vaadin-button id="nextBtn" theme="secondary">
          Mark Step 4 as complete
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "reward-view-content";
  }

  static get properties() {
    return {};
  }
}

customElements.define(RewardViewContent.is, RewardViewContent);
