import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class ActionSpaceViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Build Action Space</h1>
        <p>
          You have built a simulation model in which agents can take actions
          (for example, these might be
          <a
            href="https://help.anylogic.com/index.jsp?topic=%2Fcom.anylogic.help%2Fhtml%2Fstatecharts%2FStatecharts.html"
            rel="nofollow noopener noreferrer"
            target="_blank"
            >events that lead to a transition in a state chart</a
          >.)
        </p>
        <p>For example, the barista in the coffee shop can:</p>
        <ol>
          <li>Take an order</li>
          <li>Prepare the order</li>
          <li>Clean the kitchen</li>
          <li>Take payment</li>
        </ol>
        <h4>Code example</h4>
        <pre><code>if (server(0).actionstate.getState() == server(0).idle) {<br>    if (action[0] == 0 && placeOrder.queueSize(placeQueue) > 0) {<br>        send(TAKE, server(0));<br>    }<br>    else if(action[0] == 1 && collectOrder.queueSize(waitToCollectArea) > 0) {<br>        send(PREP, server(0));<br>    } else if(action[0] == 2 && payBill.queueSize(waitToPayArea) > 0) {<br>        send(BILL, server(0));<br>    }<br>    else {<br>        send(CLEAN, server(0));<br>    }<br>}</code></pre>
        <p>
          <a
            href="https://help.pathmind.com/en/articles/3640124-3-build-action-space"
            rel="nofollow noopener noreferrer"
            target="_blank"
          >
            Learn more about building an Action Space
          </a>
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
    return {};
  }
}

customElements.define(ActionSpaceViewContent.is, ActionSpaceViewContent);
