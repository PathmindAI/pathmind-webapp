import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class GuideOverviewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles pathmind-dialog-view">
        :host {
          height: auto;
          background-color: white;
          flex-grow: 1;
        }
        h1 {
          line-height: 1.3em;
          margin-top: 0;
        }
        p {
          width: 100%;
          line-height: 1.3em;
          margin: 0 0 1em;
        }
        p:last-of-type {
          margin-bottom: 0;
        }
        .content {
          max-width: 700px;
          padding: 60px 0 0 0;
          margin-top: 0;
        }
        vaadin-button {
          margin: 0 auto 40px;
        }
        vaadin-button:nth-of-type(1) {
          margin-top: 80px;
        }
      </style>
      <div class="content">
        <h1>{{title}}</h1>
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
            "Trigger Actions",
            'Define "Done" Condition',
            "Define Reward Variables"
          ];
        }
      }
    };
  }
}

customElements.define(GuideOverviewContent.is, GuideOverviewContent);
