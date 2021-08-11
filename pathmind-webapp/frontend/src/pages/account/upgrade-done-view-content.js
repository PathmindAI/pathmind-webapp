import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

class UpgradeDoneViewContent extends PolymerElement {
  static get template() {
    return html`
      <style>
        upgrade-done-view-content .inner-content {
          margin-top: var(--lumo-space-xxl);
        }
        upgrade-done-view-content h2 {
          margin: 0 auto var(--lumo-space-m);
        }
        upgrade-done-view-content iron-icon {
          --iron-icon-height: 40px;  
          --iron-icon-width: 40px;
          color: var(--pm-green-color);   
          margin: 25px auto 15px;
        }
      </style>
      <div class="content">
        <vaadin-vertical-layout
          class="inner-content"
        >
        <iron-icon icon="vaadin:check-circle"></iron-icon>
          <h2>Upgraded to {{plan}}!</h2>
          <div>
            A confirmation email will be sent after payment is processed.
          </div>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button
              id="done"
              theme="primary"
              >Done</vaadin-button
            >
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>
      </div>
`;
  }

  static get is() {
    return "upgrade-done-view-content";
  }

  static get properties() {
      return {
          sessionId: {
              type: String,
              value: "",
          }
      };
  }

  _attachDom(dom) {
    this.appendChild(dom);
  }

  ready() {
      super.ready();
      const SESSION_ID = "session_id=";
      if (window.location.href.indexOf(SESSION_ID)) {
        this.sessionId = window.location.href.split(SESSION_ID)[1];
        this.dispatchEvent(new Event("sessionidchange"));
      }
  }

}

customElements.define(UpgradeDoneViewContent.is, UpgradeDoneViewContent);
