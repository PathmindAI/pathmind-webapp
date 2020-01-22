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
          top: 0;
          background-color: white;
          padding: var(--lumo-space-xl);
        }
        a {
          display: block;
          color: var(--pm-text-color);
          line-height: 1.3em;
          margin: 0 0 1em;
          text-decoration: none;
          opacity: 0.25;
        }
        a:last-of-type {
          margin-bottom: 0;
        }
        a:hover,
        .current {
          font-weight: 700;
          opacity: 1;
        }
        vaadin-button {
          font-size: var(--lumo-font-size-s);
          padding: 0;
          margin: var(--lumo-space-xl) 0 0;
        }
      </style>
      <div class="content">
        <dom-repeat items="[[checklist]]">
          <template>
            <a class$="{{item.state}}" href$="{{item.path}}">{{item.name}}</a>
          </template>
        </dom-repeat>
        <vaadin-button id="skipToUploadModelBtn" theme="tertiary">
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
