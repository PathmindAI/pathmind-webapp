import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class GuideOverviewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="pathmind-dialog-view guide-view"></style>
      <div class="content">
        <h1>Guide to preparing your simulation for Pathmind</h1>
        <p>
          Dictumst vestibulum rhoncus est pellentesque elit ullamcorper.
          Ullamcorper dignissim cras tincidunt lobortis feugiat vivamus at.
          Lacus laoreet non curabitur gravida. Nibh sed pulvinar proin gravida.
          Eget gravida cum sociis natoque penatibus et.
        </p>
        <dom-repeat items="[[checklist]]">
          <template>
            <p class="checklist-item"><strong>{{item}}</strong></p>
          </template>
        </dom-repeat>
        <p>
          Tortor id aliquet lectus proin nibh nisl condimentum. Ut pharetra sit
          amet aliquam id diam maecenas ultricies mi. Faucibus scelerisque
          eleifend donec pretium vulputate sapien nec sagittis aliquam.
          Ultricies leo integer malesuada nunc vel risus. Massa tincidunt dui ut
          ornare lectus sit. Vulputate sapien nec sagittis aliquam malesuada
          bibendum arcu vitae.
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
    return "guide-overview-content";
  }

  static get properties() {
    return {
      checklist: {
        value() {
          return [
            "Key Concept of Pathmind",
            "Install Pathmind Helper",
            "Build Observation Space",
            "Build Action Space",
            "Triggering Actions",
            'Define "Done" Condition',
            "Define Reward Variables"
          ];
        }
      }
    };
  }
}

customElements.define(GuideOverviewContent.is, GuideOverviewContent);
