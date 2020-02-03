import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class GuideOverviewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view guide-view">
        .key-concept-box {
          padding: var(--lumo-space-xl);
          border: 1px solid var(--pm-gray-color-darker);
          border-radius: 6px;
        }
        .key-concept-box p {
          line-height: var(--pm-line-height-xl);
          margin-bottom: 0;
        }
        .step {
          opacity: 0.25;
          cursor: pointer;
        }
        .step.current {
          opacity: 1;
        }
        .step span {
          font-style: italic;
          font-weight: bold;
        }
        .color-steps {
          color: var(--pm-gray-color-dark);
        }
        .color-state {
          color: var(--pm-blue-color);
        }
        .color-trigger {
          color: var(--pm-primary-color);
        }
        .color-observation {
          color: var(--pm-green-color);
        }
        .color-reward {
          color: var(--pm-orange-color);
        }
      </style>
      <div class="content">
        <h1>Preparing your simulation for Pathmind</h1>
        <p>
          This checklist helps you confirm that your simulation models are ready
          for reinforcement learning, by providing structured reminders of the
          main steps in that preparation process. At each step, we link to more
          detailed instructions.
        </p>
        <h4>Key Concept:</h4>
        <div class="key-concept-box">
          <div class="diagram"></div>
          <p on-click="stepOnClick">
            <span class="step current"
              >Your simulation model has a start and a finish. In between, there
              are <span class="color-steps">steps</span>. At the beginning of
              each step, there is a
              <span class="color-state">state</span>.</span
            >
            <span class="step"
              >When you want to start a step, you want to take an action, so you
              have to <span class="color-trigger">trigger</span> the action.
              Triggering an action is either based on time or when something is
              done.</span
            >
            <span class="step"
              >Before the action is taken, you’d take an
              <span class="color-observation">observation</span> to decide what
              action to take.</span
            >
            <span class="step"
              >Once an observation is taken, an
              <span class="color-action">action</span> follows.</span
            >
            <span class="step"
              >When the action is completed, a
              <span class="color-reward">reward</span> is given. A reward is a
              score indicating how well the action worked toward your
              goal.</span
            >
          </p>
        </div>
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
    return {};
  }

  stepOnClick(event) {
    const targetStep =
      event.target.classList.toString().indexOf("color") > -1
        ? event.target.parentNode
        : event.target;
    const currentStep = targetStep.parentNode.querySelector(".step.current");

    if (targetStep.tagName.toLowerCase() !== "p") {
      if (currentStep) {
        currentStep.classList.remove("current");
      }
      targetStep.classList.add("current");
    }
  }
}

customElements.define(GuideOverviewContent.is, GuideOverviewContent);
