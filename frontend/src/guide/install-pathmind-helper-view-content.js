import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class InstallPathmindHelperViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <div class="logo-wrapper">
          <img
            src="/frontend/images/pathmind-logo-100x100.png"
            alt="Pathmind Helper"
          />
        </div>
        <h1>Install Pathmind Helper</h1>
        <p>
          The Pathmind Helper is an
          <a href="[[arrayItem(links.*, 0, 'url')]]" target="_blank"
            >[[arrayItem(links.*, 0, 'text')]]</a
          >
          that enables you to apply reinforcement learning to your simulations.
          It serves two purposes:
        </p>
        <ul>
          <li>
            Exposes simulation data and converts it into a format that a
            reinforcement learning algorithm can consume for training.
          </li>
          <li>Enables you to query a trained policy in AnyLogic.</li>
        </ul>
        <h2>Installation</h2>
        <p>
          <b>Step 1: </b
          ><a target="_blank" href="[[arrayItem(links.*, 1, 'url')]]"
            >[[arrayItem(links.*, 1, 'text')]]</a
          >
          (~200mb).
        </p>
        <p>
          <b>Step 2: </b>Add PathmindHelper.jar as an
          <a href="[[arrayItem(links.*, 0, 'url')]]" target="_blank"
            >[[arrayItem(links.*, 0, 'text')]]</a
          >.
        </p>
        <div class="video-wrapper">
          <iframe
            src$="[[videoLink]]"
            frameborder="0"
            webkitallowfullscreen
            mozallowfullscreen
            allowfullscreen
          ></iframe>
        </div>
        <p>
          <b>Step 3</b>: Drag and drop the Pathmind Helper palette item into
          your top-level agent (typically "Main" which is the AnyLogic
          default)*.
        </p>
        <p>
          <i
            >*There can only be a single instance of the Pathmind Helper per
            AnyLogic model. Adding more than one will result in an error.
          </i>
        </p>
        <h2>Pathmind Helper Properties</h2>
        <p>Click on the Pathmind Helper to view it's properties.</p>
        <h3>Top-Level Properties</h3>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperTop.png" />
        </div>
        <p><b>Single Agent vs Population of Agents</b></p>
        <p>
          These radio buttons are default AnyLogic options (i.e. they can't be
          removed). Although these options do nothing in the context of
          Pathmind,
          <b>you must always select "Single Agent" for Pathmind to work</b>.
        </p>
        <h4>Number of Actions</h4>
        <p>
          The total number of actions that your agent can perform. We explain
          how this number is derived below.
        </p>
        <h4>Action Type</h4>
        <p>
          <code>Use Random Actions</code> tells Pathmind to select actions at a
          random. This option serves two purposes:
        </p>
        <ol>
          <li>
            Confirm that your action space is configured correctly. If your
            agent does nothing, there is likely a logic error in your
            simulation.
          </li>
          <li>
            Construct a baseline to measure the performance of a trained policy.
            Better than random is generally a good starting point.
          </li>
        </ol>
        <p>
          <code>Use Policy</code> will execute your simulation using the trained
          policy obtained from Pathmind instead of random actions. When
          <code>Use Policy</code> is selected, the Pathmind Helper will query
          the policy to predict the next best action.
        </p>
        <h3>Reinforcement Learning Functions</h3>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperFunctions.png" />
        </div>
        <p>
          <b>Observations </b>should contain anything that is relevant for an
          agent to make an informed decision.
        </p>
        <p>
          <b>Reward Variables</b> determine what feedback an agent will receive
          after each action is taken.
        </p>
        <p>
          <b>Actions </b>are a list of all possible actions that an agent can
          perform.
        </p>
        <p>
          <b>isDone </b>sets the length of your simulation. In reinforcement
          learning terminology, the length of your simulation is also known as
          an "episode".
        </p>
        <h3>Event Trigger</h3>
        <div class="screenshot-wrapper">
          <img src="frontend/images/guide/PathmindHelperTrigger.png" />
        </div>
        <p>
          The event trigger tells an agent when it should execute an action. The
          event trigger must return <code>true</code> (execute action) or
          <code>false</code> (do not execute action).
        </p>
        <h3>Miscellaneous</h3>
        <p>Please ignore everything below Event Trigger.</p>
        <ul>
          <li>Dimensions and Movement</li>
          <li>Initial Location</li>
          <li>Statistics</li>
          <li>Advanced</li>
          <li>Description</li>
        </ul>
        <p>
          These are default AnyLogic options that cannot be removed.<br /><br /><i
            >For advanced users, there is also the option to call
            actionEffectIsObservable() to notify the trainer that the effect of
            the last action is now observable. But don’t worry if you dont know
            when this happens. We just assume that the effect of the last action
            is observable when you trigger the next action.</i
          >
        </p>
        <vaadin-button id="nextBtn" theme="secondary">
          Pathmind Helper Installed
        </vaadin-button>
        <vaadin-button id="backBtn" theme="tertiary">
          Back to Overview
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "install-pathmind-helper-view-content";
  }

  static get properties() {
    return {
      links: {
        value() {
          return [
            {
              text: "AnyLogic palette item",
              url:
                "https://help.anylogic.com/topic/com.anylogic.help/html/ui/Palette_View.html"
            },
            {
              text: "Download the Pathmind Helper",
              url:
                "https://pathminddownloads.blob.core.windows.net/pathmind-download/PathmindHelper.jar"
            }
          ];
        }
      },
      videoLink: {
        value() {
          return `https://fast.wistia.net/embed/iframe/zujiq74yfe`;
        }
      }
    };
  }

  arrayItem(change, index, path) {
    return this.get(path, change.base[index]);
  }
}

customElements.define(
  InstallPathmindHelperViewContent.is,
  InstallPathmindHelperViewContent
);
