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
          optimize.
        </p>
        <p class="screenshot-description">
          First, create a new function and name it <code>rewardVariables</code>.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/RewardVariables1.png" />
        </div>
        <p class="screenshot-description">
          Next, click the <code>rewardVariables</code> function to inspect it's
          properties.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/RewardVariables2.png" />
        </div>
        <h4>
          Step 1
          <span class="slim"
            >- Set the return value to a two-dimensional array of doubles.</span
          >
        </h4>
        <ul>
          <li>Select the <code>Returns value</code> radio button.</li>
          <li>
            Set the <code>Type to Other...</code> and input
            <code>double[][]</code>.
          </li>
        </ul>
        <h4>
          Step 2 <span class="slim">- Define your reward variables.</span>
        </h4>
        <p>
          In the example below, we define <code>rewardVariables</code> to return
          rewards for a model containing two agents.
        </p>
        <pre><code>// Initialize your 2-dimensional double array<br>double[][] reward = new double[2][4]; //[2] = 2 agents, [4] = 4 rewards per agent<br><br>// Observations for Agent 0<br>reward[0][0] = rew1;<br>reward[0][1] = rew2;<br>reward[0][2] = rew3;<br>reward[0][3] = rew4;<br><br>// Observations for Agent 1<br>reward[1][0] = rew1;<br>reward[1][1] = rew2;<br>reward[1][2] = rew3;<br>reward[1][3] = rew4;<br><br>return reward;</code></pre>
        <p>
          This should output an array that looks something like:
          <code>[[0.0, 0.0, 0.0, 0.0], [1.0, 1.0, 1.0, 1.0]]</code> where index
          0 (<code>[0.0, 0.0, 0.0, 0.0]</code> ) corresponds with Agent 0 and
          index 1 (<code>[1.0, 1.0, 1.0, 1.0]</code> ) corresponds with Agent 1.
        </p>
        <p>
          The values <code>0.0</code> and <code>1.0</code> are just
          placeholders; in actuality, they will contain varied values from your
          rewards. The variables <code>rew0</code>, <code>rew1 </code>,
          <code>rew2</code> and so forth would each hold information about your
          rewards.
        </p>
        <h4>
          Step 3
          <span class="slim"
            >- Add <code>rewardVariables</code> to the Pathmind Helper.</span
          >
        </h4>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperFunctionsReward.png" />
        </div>
        <p>
          Finally, you can inspect your <code>rewardVariables</code> by
          following the instructions
          <a href$="[[helpArticleLink]]" target="_blank">here</a>.
        </p>
        <h2>Why do we need reward variables?</h2>
        <p>
          Reward variables alone serve no purpose. These reward variables will
          become the building blocks for your reward function in Pathmind.
        </p>
        <p>
          For example, imagine that you have defined the following reward
          variables in AnyLogic. <br />
        </p>
        <pre><code>reward[0][0] = revenue;<br>reward[0][1] = cost;</code></pre>
        <p>
          Using these variables, you would write a reward function in Pathmind
          to maximize revenue and reduce costs.
        </p>
        <pre><code>reward[0] = after[0][0] - before[0][0]; // Maximize Revenue<br>reward[0] -= after[0][1] - before[0][1]; // Minimize Costs</code></pre>
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
      helpArticleLink: {
        value() {
          return "https://help.pathmind.com/en/articles/3631367-troubleshooting-pathmind-helper";
        }
      }
    };
  }
}

customElements.define(RewardViewContent.is, RewardViewContent);
