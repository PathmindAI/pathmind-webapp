import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class GuideOverviewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Preparing your simulation for Pathmind</h1>
        <p>
          This checklist helps you confirm that your simulation models are ready
          for reinforcement learning, by providing structured reminders of the
          main steps in that preparation process. At each step, we link to more
          detailed instructions.
        </p>
        <p>
          <em
            >Note: Examples in this checklist are taken from the
            <a href$="[[findingTheCheeseTutorial]]" target="_blank">
              Finding the Cheese tutorial</a
            >.
          </em>
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          I've read the overview
        </vaadin-button>
        <vaadin-button id="skipToUploadModelBtn" theme="tertiary">
          Skip to Upload Model
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "guide-overview-content";
  }

  static get properties() {
    return {
      findingTheCheeseTutorial: {
        value() {
          return "https://help.pathmind.com/en/articles/3750911-2-finding-the-cheese";
        }
      }
    };
  }
}

customElements.define(GuideOverviewContent.is, GuideOverviewContent);
