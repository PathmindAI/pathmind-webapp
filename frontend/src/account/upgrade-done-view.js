import {html, PolymerElement} from "@polymer/polymer/polymer-element.js";

/**
 * `upgrade-done-view`
 *
 * UpgradeDoneView element.
 *
 * @customElement
 * @polymer
 */
class UpgradeDoneView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles  pathmind-dialog-view">
        .content {
          margin: auto;
          max-width: 650px;
          width: 650px;
        }
        .form-cont {
          width: 75%;
          margin: 25px auto 0;
        }

        #errorCont {
          margin: 10px 20px 0;
          width: 100%;
        }
        #errorCont .error-message {
          padding-top: 8px;
        }

        #cardNumber vaadin-text-field {
          width: 23%;
          margin-right: 2%;
        }

        #expiresAndCVC vaadin-text-field {
          width: 48%;
          margin-right: 2%;
        }
        #city {
          width: 48%;
          margin-right: 2%;
        }
        #state {
          width: 20%;
          margin-right: 2%;
        }
        #zip {
          width: 26%;
          margin-right: 2%;
        }
        #numberOnCard, 
        #billingAddress {
          width: 98%;
          margin-right: 2%;
        }

        .custom-label {
          color: var(--lumo-secondary-text-color);
          font-size: var(--lumo-font-size-s);
          font-weight: 500;
          margin-top: 15px;
        }

        .title {
          font-size: 1.5em;
          margin: 0 auto 15px;
          color: #676767;
        }
        .sub-title {
          margin: 0 auto 15px;
          color: #878787;
        }

        .payment-notes {
          margin: 15px auto 0;
          color: #878787;
          font-size: 0.8em;
        }
        .icon {
          --iron-icon-height: 40px;  
          --iron-icon-width: 40px;   
          margin: 25px auto 15px;
        }
        
      </style>
      <div id="header" style="width: 100%;"></div>
      <div class="content">
        <vaadin-tabs>
          <vaadin-tab>
            Upgrade
          </vaadin-tab>
        </vaadin-tabs>
        <vaadin-vertical-layout
          style="width: 100%; height: 600px;"
          class="inner-content"
          id="emailPart"
        >
        <iron-icon class="icon" icon="vaadin:check-circle"></iron-icon>
          <div class="title">Upgrade to {{plan}}!</div>
          <div class="sub-title">
            A confirmation email will be sent after payment is processed.
          </div>

          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button
              id="done"
              theme="primary"
              class="positive-action-btn"
              >Done</vaadin-button
            >
          </vaadin-vertical-layout>

        </vaadin-vertical-layout>

        </vaadin-vertical-layout>
        <a class="support" href="{{contactLink}}">Contact Support</a>
      </div>
`;
  }

  static get is() {
    return "upgrade-done-view";
  }

  static get properties() {
  }

}

customElements.define(UpgradeDoneView.is, UpgradeDoneView);
