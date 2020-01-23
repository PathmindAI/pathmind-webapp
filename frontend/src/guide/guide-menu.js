import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";
import "@vaadin/vaadin-button/src/vaadin-button.js";

class GuideMenu extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          align-self: flex-start;
          flex: 0 0 auto;
          position: sticky;
          top: 31px;
          padding: var(--lumo-space-xl);
        }
        a {
          display: block;
          color: var(--pm-blue-color-darkest-25pct);
          line-height: 1.3em;
          margin: 0 0 1em;
          text-decoration: none;
        }
        a:last-of-type {
          margin-bottom: 0;
        }
        a:hover {
          color: var(--pm-blue-color-darkest-50pct);
        }
        .current {
          color: var(--pm-blue-color-darkest);
          font-weight: 700;
        }
        .completed {
          position: relative;
        }
        .completed::before {
          content: "";
          position: absolute;
          width: 11px;
          height: 11px;
          top: 50%;
          left: -15px;
          background-image: url("/frontend/icons/checkmark.svg");
          background-size: cover;
          transform: translateY(-50%);
        }
        vaadin-button {
          font-size: var(--lumo-font-size-s);
          margin: var(--lumo-space-xl) 0 0;
        }
      </style>
      <div class="content">
        <dom-repeat items="[[checklist]]">
          <template>
            <a class$="{{item.state}}" href$="{{item.path}}">{{item.name}}</a>
          </template>
        </dom-repeat>
        <vaadin-button id="skipToUploadModelBtn" theme="tertiary-inline">
          Skip to Upload Model
        </vaadin-button>
      </div>
    `;
  }

  static get is() {
    return "guide-menu";
  }

  static get properties() {
    return {
      checklist: {
        value() {
          return [
            {
              name: "Key Concept of Pathmind",
              path: "guide/key-concept",
              state: "completed"
            },
            {
              name: "Install Pathmind Helper",
              path: "guide/install",
              state: "current"
            },
            {
              name: "Build Observation Space",
              path: "",
              state: "todo"
            },
            {
              name: "Build Action Space",
              path: "",
              state: "todo"
            },
            {
              name: "Trigger Actions",
              path: "",
              state: "todo"
            },
            {
              name: 'Define "Done" Condition',
              path: "",
              state: "todo"
            },
            {
              name: "Define Reward Variables",
              path: "",
              state: "todo"
            }
          ];
        }
      }
    };
  }
}

customElements.define(GuideMenu.is, GuideMenu);
