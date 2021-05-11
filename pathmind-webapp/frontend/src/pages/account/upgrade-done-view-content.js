import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `upgrade-done-view-content`
 *
 * UpgradeDoneView element.
 *
 * @customElement
 * @polymer
 */
class UpgradeDoneViewContent extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles  pathmind-dialog-view">
        .inner-content {
          margin-top: var(--lumo-space-xxl);
        }
        .title {
          margin: 0 auto var(--lumo-space-m);
        }
        iron-icon {
          --iron-icon-height: 40px;  
          --iron-icon-width: 40px;
          color: var(--pm-green-color);   
          margin: 25px auto 15px;
        }
      </style>
      <div class="content">
        <vaadin-vertical-layout
          class="inner-content"
          id="emailPart"
        >
        <iron-icon icon="vaadin:check-circle"></iron-icon>
          <h2 class="title">Upgraded to {{plan}}!</h2>
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

}

customElements.define(UpgradeDoneViewContent.is, UpgradeDoneViewContent);
