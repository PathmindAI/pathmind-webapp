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
          padding-left: var(--lumo-space-xxl);
        }
        a {
          display: block;
          position: relative;
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
        a::before {
          content: "";
          position: absolute;
          width: 18px;
          height: 18px;
          top: 50%;
          left: -30px;
          border: 1px solid var(--pm-gray-color);
          transform: translateY(-50%);
        }
        .current {
          color: var(--pm-blue-color-darkest);
          font-weight: 700;
        }
        .completed::after {
          content: "";
          position: absolute;
          width: 23px;
          height: 17px;
          top: 50%;
          left: -26px;
          background-image: url("/frontend/icons/checkmark_grey.svg");
          background-size: cover;
          transform: translateY(-60%);
        }
        a:not(.current)::before,
        a:not(.current)::after {
          opacity: 0.25;
        }
        a:hover::before,
        a:hover::after {
          opacity: 0.5;
        }
        vaadin-button {
          font-size: var(--lumo-font-size-s);
          margin: var(--lumo-space-xl) 0 0;
        }
      </style>
      <div>
        <dom-repeat items="[[checklist]]">
          <template>
            <a
              router-link
              class$="[[item.state]][[isCurrent(item.path)]]"
              href$="[[item.path]]"
            >
              [[item.name]]
            </a>
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
    return {};
  }

  isCurrent(targetPath) {
    return targetPath === window.location.pathname.substr(1) ? " current" : "";
  }
}

customElements.define(GuideMenu.is, GuideMenu);
