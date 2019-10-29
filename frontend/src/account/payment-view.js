import { html, PolymerElement } from "@polymer/polymer/polymer-element.js";

/**
 * `payment-view`
 *
 * PaymentView element.
 *
 * @customElement
 * @polymer
 */
class PaymentView extends PolymerElement {
  static get template() {
    return html`
      <style include="shared-styles  pathmind-dialog-view">
        .content {
          margin: auto;
          max-width: 600px;
          width: 600px;
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
          margin-left: 20px;
          color: #676767;
        }
        .sub-title {
          margin-left: 20px;
          color: #878787;
        }
      </style>
      <div id="header" style="width: 100%;"></div>
      <div class="content">
        <vaadin-tabs>
          <vaadin-tab>
            Payment
          </vaadin-tab>
        </vaadin-tabs>
        <vaadin-vertical-layout
          style="width: 100%;"
          class="inner-content"
          id="emailPart"
        >
          <div class="title">Upgrade to {{plan}}</div>
          <div class="sub-title">
            Please fill in the information below. All fields are required.
          </div>

          <vaadin-horizontal-layout id="errorCont">
            <div class="error-message">{{message}}</div>
            <div class="error-message">Validation error can be ussed here</div>
          </vaadin-horizontal-layout>

          <vaadin-vertical-layout class="form-cont">
            <vaadin-text-field
              id="numberOnCard"
              label="Number on card"
              required
            ></vaadin-text-field>
            <vaadin-text-field
              id="billingAddress"
              label="Billing Address"
              required
            ></vaadin-text-field>
            <vaadin-horizontal-layout id="" style="width: 100%;">
              <vaadin-text-field
                id="city"
                label="City"
                required
              ></vaadin-text-field>
              <vaadin-text-field
                id="state"
                label="State"
                required
              ></vaadin-text-field>
              <vaadin-text-field
                id="zip"
                label="Zip/Postal code"
                required
              ></vaadin-text-field>
            </vaadin-horizontal-layout>

            <vaadin-vertical-layout style="width: 100%;">
              <label class="custom-label">Card number</label>
              <vaadin-horizontal-layout style="width: 100%;" id="cardNumber">
                <vaadin-text-field
                  id="cardNum1" required
                ></vaadin-text-field>
                <vaadin-text-field
                  id="cardNum2" required
                ></vaadin-text-field>
                <vaadin-text-field
                  id="cardNum3" required
                ></vaadin-text-field>
                <vaadin-text-field
                  id="cardNum4" required
                ></vaadin-text-field>
              </vaadin-horizontal-layout>
            </vaadin-vertical-layout>

            <vaadin-horizontal-layout id="expiresAndCVC" style="width: 100%;" >
              <vaadin-text-field
                id="expires"
                label="Expires"
                placeholder="MM-YY"
                required
              ></vaadin-text-field>
              <vaadin-text-field
                id="cvc"
                label="CVC"
                placeholder="XXX"
                required
              ></vaadin-text-field>
            </vaadin-horizontal-layout>
          </vaadin-vertical-layout>
          <vaadin-vertical-layout id="buttonsCont">
            <vaadin-button
              id="signUp"
              theme="primary"
              class="positive-action-btn"
              >Sign Up</vaadin-button
            >
            <vaadin-button id="cancelSignUpBtn" theme="tertiary"
              >Cancel</vaadin-button
            >
          </vaadin-vertical-layout>
        </vaadin-vertical-layout>

        </vaadin-vertical-layout>
        <a class="support" href="{{contactLink}}">Contact Support</a>
      </div>
    `;
  }

  static get is() {
    return "payment-view";
  }

  static get properties() {
    return {
      // Declare your properties here.
    };
  }
}

customElements.define(PaymentView.is, PaymentView);
