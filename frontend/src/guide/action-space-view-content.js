import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class ActionSpaceViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Build Action Space</h1>
        <p>
          Before you read this, make you have built a simulation model in which
          agents can take actions (for example, these might be
          <a
            href="https://help.anylogic.com/index.jsp?topic=%2Fcom.anylogic.help%2Fhtml%2Fstatecharts%2FStatecharts.html"
            rel="nofollow noopener noreferrer"
            target="_blank"
            >events that lead to a transition in a state chart</a
          >.)
        </p>
        <p>
          Here, you'll learn how to set up your model to translate the action
          decisions prescribed by Pathmind into a format that your AnyLogic
          model can interpret. Don't worry, we'll explain what that means
          below!<br /><br />First, create a new function and assign it the name
          <code>doAction</code>.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/doAction.png" />
        </div>
        <p>
          Next, click the <code>doAction</code> function to inspect its
          properties.
        </p>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/doActionProperties.png" />
        </div>
        <h3>Step 1 - <span class="slim">Initialize an integer array.</span></h3>
        <p>
          First, select the <code>Just action (returns nothing)</code> radio
          button.
        </p>
        <p>Then inside "Arguments"...</p>
        <ul>
          <li>Set the Name to <code>actions</code>.</li>
          <li>Set the Type to <code>int[]</code>.</li>
        </ul>
        <h3>
          Step 2 <span class="slim">- Write your doAction function.</span>
        </h3>
        <p>
          Your <code>doAction</code> function will receive an array of integers
          from Pathmind. This array tells the agent what to do next.
        </p>
        <p>Examples:</p>
        <p>
          <code>[1]</code> - One agent instructed to execute action
          <code>1</code> .<br /><code>[5, 2]</code> - Two agents with Agent 0
          instructed to execute action <code>5</code> and Agent 1 instructed to
          execute action <code>2</code>.<br /><code>[0, 3, 1, 6]</code> - Four
          agents with Agent 0 = action <code>0</code> , Agent 1 = action
          <code>3</code> , Agent 2 = action <code>1</code>, and Agent 3 = action
          <code>6</code> . <br /><br />The job of <code>doAction</code> is to
          map this array into something that AnyLogic can interpret. Here are
          some examples using pseudo code.
        </p>
        <h4>Single Agent Using a Switch Statements</h4>
        <pre><code>switch (action[0]) { <br>   case 0: <br>      send(ACTION 0, agent(0))<br>      break; <br>   case 1: <br>      send(ACTION 1, agent(0))<br>      break; <br>}</code></pre>
        <p></p>
        <h4>Multiple Agents Using Switch Statements</h4>
        <pre><code>// Actions for Agent 0<br>if (action[0] == 0) {<br>   send(ACTION 0, agent(0))<br>}<br>else if (action[0] == 1) {<br>   send(ACTION 1, agent(0))<br>}<br><br>// Actions for Agent 1<br>if (action[1] == 0) {<br>   send(ACTION 0, agent(1))<br>}<br>else if (action[1] == 1) {<br>   send(ACTION 1, agent(1))<br>}</code></pre>
        <h3>
          Step 3
          <span class="slim"
            >- Add <code>doAction(action)</code> to the Pathmind Helper.</span
          >
        </h3>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperFunctionsActions.png" />
        </div>
        <p>
          Finally, you can inspect your <code>doAction</code> by following the
          instructions
          <a href$="[[helpArticleLink]]" target="_blank">here</a>.<br />
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          Completed Building Action Space
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Observation Space
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "action-space-view-content";
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

customElements.define(ActionSpaceViewContent.is, ActionSpaceViewContent);
