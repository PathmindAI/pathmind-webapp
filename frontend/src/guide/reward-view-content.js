import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class RewardViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Define Reward Variables</h1>
        <p>
          Reward variables are the building blocks for the reward function.
          Reward variables can embody important simulation metrics such as
          revenue and cost. These metrics are likely what you directly seek to
          optimize.In the AnyLogic palette, create a new function and name it
          rewardVariables. Inspect its properties. Set the return value to a
          two-dimensional array of doubles. Define your reward variables. Add
          rewardVariables to the Pathmind Helper.
        </p>
        <p>
          <a href$="[[readMoreLink]]" target="_blank"
            >Learn more about Reward Variables</a
          >
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/RewardVariables1.png" />
        </div>
        <vaadin-button id="nextBtn" theme="secondary">
          Reward Variables Defined
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Define “Done” Condition
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "reward-view-content";
  }

  static get properties() {
    return {
      readMoreLink: {
        value() {
          return "https://help.pathmind.com/en/articles/3640175-6-define-reward-variables";
        }
      }
    };
  }
}

customElements.define(RewardViewContent.is, RewardViewContent);
